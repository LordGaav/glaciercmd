/*
 * Copyright (c) 2012 Nick Douma < n.douma [at] nekoconeko . nl >
 *
 * This file is part of glaciercmd.
 *
 * glaciercmd is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * glaciercmd is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with glaciercmd. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.nekoconeko.glaciercmd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import nl.nekoconeko.glaciercmd.types.VaultInventory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class GlacierClient {
	private AWSCredentials credentials;
	private AmazonGlacierClient client;

	protected GlacierClient(String key, String secret) {
		this.credentials = new BasicAWSCredentials(key, secret);
		this.client = new AmazonGlacierClient(this.credentials);
	}

	protected void setEndpoint(String endpoint) {
		this.client.setEndpoint(endpoint);
	}

	protected List<DescribeVaultOutput> getVaults() {
		ListVaultsRequest lvr = new ListVaultsRequest("-");
		return this.client.listVaults(lvr).getVaultList();
	}

	protected DescribeVaultResult describeVault(String vault) {
		DescribeVaultRequest dvr = new DescribeVaultRequest(vault);
		return this.client.describeVault(dvr);
	}

	protected UploadResult uploadFile(String vault, String filename, String description) {
		ArchiveTransferManager atm = new ArchiveTransferManager(this.client, this.credentials);
		UploadResult res = null;
		try {
			res = atm.upload(vault, description, new File(filename));
		} catch (AmazonServiceException e) {
			System.err.println("An error occured at Amazon AWS: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (AmazonClientException e) {
			System.err.println("An error occured while executing the request: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("File does not exist: " + filename);
			System.exit(1);
		}
		return res;
	}

	protected void downloadFile(String vault, String identifier, String filename) {
		ArchiveTransferManager atm = new ArchiveTransferManager(this.client, this.credentials);
		try {
			atm.download(vault, identifier, new File(filename));
		} catch (AmazonServiceException e) {
			System.err.println("An error occured at Amazon AWS: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (AmazonClientException e) {
			System.err.println("An error occured while executing the request: " + e.getLocalizedMessage());
			System.exit(1);
		}
	}

	protected String initiateRetrieveVaultInventory(String vault) {
		InitiateJobRequest initJobRequest = new InitiateJobRequest();
		initJobRequest.setVaultName(vault);
		initJobRequest.setJobParameters(new JobParameters().withType("inventory-retrieval"));

		InitiateJobResult initJobResult = this.client.initiateJob(initJobRequest);
		return initJobResult.getJobId();
	}

	private GetJobOutputResult waitForJobCompletion(String vault, String jobId) throws InterruptedException {
		GetJobOutputRequest job = new GetJobOutputRequest();
		job.setVaultName(vault);
		job.setJobId(jobId);

		GetJobOutputResult res = null;
		boolean stop = false;
		while (!stop) {
			int code = 0;
			String error = "";
			try {
				res = this.client.getJobOutput(job);
				code = res.getStatus();
			} catch (AmazonServiceException e) {
				error = e.getMessage();
				code = e.getStatusCode();
			}

			switch (code) {
			case 200:
				stop = true;
				break;
			case 400:
				if (error.contains("InProgress")) {
					Formatter.printInfoLine("Job is not finished yet, waiting...");
				} else {
					Formatter.printErrorLine("AWS returned code 400. Body Malformed.");
					System.exit(1);
				}
				break;
			case 404:
				Formatter.printErrorLine("AWS returned code 404. Vault or Job could not be found.");
				System.exit(1);
			default:
				Formatter.printInfoLine(String.format("Job returned code %d, waiting 30 seconds", code));
				break;
			}
			if (!stop) {
				Thread.sleep(1000 * 60);
			}
		}

		return res;
	}

	protected VaultInventory retrieveVaultInventoryJob(String vault, String jobId) throws InterruptedException {
		GetJobOutputResult res = this.waitForJobCompletion(vault, jobId);

		String json = "";
		VaultInventory inv = null;
		try {
			json = new java.util.Scanner(res.getBody()).useDelimiter("\\A").next();
			inv = VaultInventory.loadFromJSON(json);
		} catch (java.util.NoSuchElementException e) {
			Formatter.printErrorLine("Amazon AWS response was empty");
			System.exit(1);
		} catch (IOException ioe) {
			Formatter.printErrorLine("Failed to deserialize JSON, dumping...");
			Formatter.printBorderedError(json);
			System.exit(1);
		}
		return inv;
	}

	protected String initiateDownload(String vault, String identifier) {
		InitiateJobRequest initJobRequest = new InitiateJobRequest();
		initJobRequest.setVaultName(vault);
		initJobRequest.setJobParameters(new JobParameters().withType("archive-retrieval").withArchiveId(identifier));

		InitiateJobResult initJobResult = this.client.initiateJob(initJobRequest);
		return initJobResult.getJobId();
	}

	protected void retrieveDownloadJob(String vault, String jobId, String filename) throws InterruptedException {
		GetJobOutputResult res = this.waitForJobCompletion(vault, jobId);

		InputStream input = new BufferedInputStream(res.getBody());
		OutputStream output = null;
		try {
			output = new BufferedOutputStream(new FileOutputStream(filename));

			byte[] buffer = new byte[1024 * 1024];

			int bytesRead = 0;
			do {
				bytesRead = input.read(buffer);
				if (bytesRead <= 0)
					break;
				output.write(buffer, 0, bytesRead);
			} while (bytesRead > 0);
		} catch (IOException e) {
			Formatter.printErrorLine(String.format("Unable to save archive: %s", e.getLocalizedMessage()));
			System.exit(1);
		} finally {
			try {
				input.close();
			} catch (Exception e) {
			}
			try {
				output.close();
			} catch (Exception e) {
			}
		}
		Formatter.printErrorLine("Retrieved archive to " + filename);
	}
}
