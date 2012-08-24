package nl.nekoconeko.glaciercmd;

import java.util.List;

import nl.nekoconeko.glaciercmd.config.ConfigModes;
import nl.nekoconeko.glaciercmd.config.Configuration;
import nl.nekoconeko.glaciercmd.types.ModeType;

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
				break;
			default:
				Formatter.printErrorLine("Invalid list type selected");
				System.exit(-1);
			}
		}
	}
}
