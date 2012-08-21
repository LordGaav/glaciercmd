package nl.nekoconeko.glaciercmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
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
}
