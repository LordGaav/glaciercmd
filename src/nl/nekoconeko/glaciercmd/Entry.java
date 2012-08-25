package nl.nekoconeko.glaciercmd;

import java.util.List;

import nl.nekoconeko.glaciercmd.config.ConfigModes;
import nl.nekoconeko.glaciercmd.config.Configuration;
import nl.nekoconeko.glaciercmd.types.ModeType;
import nl.nekoconeko.glaciercmd.types.VaultInventory;
import nl.nekoconeko.glaciercmd.types.VaultInventory.Archive;

import org.apache.commons.cli.CommandLine;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;

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
		} else {
			Formatter.printErrorLine("WHA-HUH!?");
			System.exit(-1);
		}
	}
}
