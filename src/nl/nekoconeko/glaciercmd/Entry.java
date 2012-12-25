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

import java.util.List;

import nl.nekoconeko.glaciercmd.config.ConfigModes;
import nl.nekoconeko.glaciercmd.config.Configuration;
import nl.nekoconeko.glaciercmd.types.ModeType;
import nl.nekoconeko.glaciercmd.types.VaultInventory;
import nl.nekoconeko.glaciercmd.types.VaultInventory.Archive;

import org.apache.commons.cli.CommandLine;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class Entry {

	public static void main(String[] args) {
		CommandLine cli = Configuration.parseCli(ModeType.ROOT, args);

		if (cli.hasOption("config")) {
			Configuration.loadFile(cli.getOptionValue("config"));
		}

		Configuration.load(cli);

		if (Configuration.getMode() == ModeType.HELP) {
			if (Configuration.hasHelpType()) {
				ConfigModes.printConfigModeHelp(Configuration.getHelpType());
			} else {
				ConfigModes.printConfigModeHelp(ModeType.ROOT);
			}
			System.exit(0);
		}

		if (Configuration.getMode() == ModeType.VERSION) {
			Formatter.printHeader();
			System.exit(0);
		}

		if (!Configuration.hasKey() || !Configuration.hasSecret() || !Configuration.hasRegion()) {
			Formatter.printErrorLine("Make sure key, secret and region are set!");
			System.exit(-1);
		}

		GlacierClient gc = new GlacierClient(Configuration.getKey(), Configuration.getSecret());
		gc.setEndpoint(Configuration.getRegion().getEndpoint());

		if (Configuration.getMode() == ModeType.LIST) {
			Configuration.load(ModeType.LIST, args);
			if (!Configuration.hasListType()) {
				Formatter.printErrorLine("Make sure you specify a list type!");
				System.exit(-1);
			}

			switch (Configuration.getListType()) {
			case VAULT:
				List<DescribeVaultOutput> vaults = gc.getVaults();
				for (DescribeVaultOutput vault : vaults) {
					Formatter.printInfoLine(String.format("[%s] %d items, %d bytes, %s last checked", vault.getVaultName(), vault.getNumberOfArchives(), vault.getSizeInBytes(), vault.getLastInventoryDate()));
				}
				break;
			case ARCHIVE:
				if (!Configuration.hasVault()) {
					Formatter.printErrorLine("A vault has to be specified to list archives");
					System.exit(-1);
				}
				DescribeVaultResult vault = gc.describeVault(Configuration.getVault());
				Formatter.printInfoLine(String.format("[%s] %d items, %d bytes, %s last checked", vault.getVaultName(), vault.getNumberOfArchives(), vault.getSizeInBytes(), vault.getLastInventoryDate()));
				break;
			default:
				Formatter.printErrorLine("Invalid list type selected");
				System.exit(-1);
			}
		} else if (Configuration.getMode() == ModeType.INITIATEINVENTORY) {
			Configuration.load(ModeType.INITIATEINVENTORY, args);

			Formatter.printInfoLine("Requesting an inventory from AWS Glacier...");
			Formatter.printInfoLine(String.format("Job has been created with id: %s", gc.initiateRetrieveVaultInventory(Configuration.getVault())));
		} else if (Configuration.getMode() == ModeType.GETINVENTORY) {
			Configuration.load(ModeType.GETINVENTORY, args);

			Formatter.printInfoLine(String.format("Retrieving inventory with id %s from AWS Glacier...", Configuration.getJobId()));
			VaultInventory inv = null;
			try {
				inv = gc.retrieveVaultInventoryJob(Configuration.getVault(), Configuration.getJobId());
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}

			Formatter.printInfoLine(String.format("Vault ARN: %s", inv.VaultARN));
			Formatter.printInfoLine(String.format("Last Inventory Date: %s", inv.InventoryDate.toString()));
			Formatter.printInfoLine(String.format("Vault contains %d archives:", inv.ArchiveList.size()));
			for (Archive arch : inv.ArchiveList) {
				StringBuilder a = new StringBuilder();
				a.append(String.format("Archive ID: %s\n", arch.ArchiveId));
				a.append(String.format("Archive Description: %s\n", arch.ArchiveDescription));
				a.append(String.format("Archive Size: %d bytes\n", arch.Size));
				a.append(String.format("Creation Date: %s\n", arch.CreationDate.toString()));
				a.append(String.format("SHA256 Hash: %s\n", arch.SHA256TreeHash));
				Formatter.printBorderedInfo(a.toString());
			}

		} else if (Configuration.getMode() == ModeType.UPLOAD) {
			Configuration.load(ModeType.UPLOAD, args);

			Formatter.printInfoLine(String.format("Uploading '%s' to vault '%s'", Configuration.getInputFile(), Configuration.getVault()));
			UploadResult res = gc.uploadFile(Configuration.getVault(), Configuration.getInputFile(), Configuration.getDescription());
			Formatter.printInfoLine(String.format("Upload completed, archive has ID: %s", res.getArchiveId()));
		} else if (Configuration.getMode() == ModeType.DOWNLOAD) {
			Configuration.load(ModeType.DOWNLOAD, args);

			Formatter.printInfoLine(String.format("Download '%s' from vault '%s', saving as '%s'", Configuration.getArchive(), Configuration.getVault(), Configuration.getOutputFile()));
			Formatter.printInfoLine("Note that this will take several hours, please be patient...");
			gc.downloadFile(Configuration.getVault(), Configuration.getArchive(), Configuration.getOutputFile());
			Formatter.printInfoLine("Download completed");

		} else if (Configuration.getMode() == ModeType.INITIATEDOWNLOAD) {
			Configuration.load(ModeType.INITIATEDOWNLOAD, args);
			Formatter.printInfoLine(String.format("Requesting download of archive '%s' from vault '%s'...", Configuration.getArchive(), Configuration.getVault()));
			Formatter.printInfoLine(String.format("Job has been created with id: %s", gc.initiateDownload(Configuration.getVault(), Configuration.getArchive())));
		} else if (Configuration.getMode() == ModeType.GETDOWNLOAD) {
			Configuration.load(ModeType.GETDOWNLOAD, args);
			Formatter.printInfoLine(String.format("Retrieving download with '%s' from AWS Glacier, and saving it to '%s'...", Configuration.getJobId(), Configuration.getOutputFile()));
			try {
				gc.retrieveDownloadJob(Configuration.getVault(), Configuration.getJobId(), Configuration.getOutputFile());
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			Formatter.printInfoLine("Download completed");
		} else if (Configuration.getMode() == ModeType.DELETEARCHIVE) {
			Configuration.load(ModeType.DELETEARCHIVE, args);
			Formatter.printInfoLine(String.format("Deleting archive with id '%s' from vault '%s'", Configuration.getArchive(), Configuration.getVault()));
			gc.deleteArchive(Configuration.getVault(), Configuration.getArchive());
		} else {
			Formatter.printErrorLine("WHA-HUH!?");
			System.exit(-1);
		}
	}
}
