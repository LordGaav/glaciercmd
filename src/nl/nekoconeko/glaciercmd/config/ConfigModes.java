package nl.nekoconeko.glaciercmd.config;

import java.util.HashMap;
import java.util.Map;

import nl.nekoconeko.glaciercmd.Version;
import nl.nekoconeko.glaciercmd.types.ModeType;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionGroup;

public class ConfigModes {

	private static Map<ModeType, ConfigMode> modeMap;

	static {
		ConfigModes.modeMap = new HashMap<ModeType, ConfigMode>(ModeType.values().length);

		// Configuration file
		ConfigParameter config = new ConfigParameter("c", "config", true, "FILE", "Use a configuration file");

		// Connection information
		ConfigParameter key = new ConfigParameter("k", "key", true, "KEY", "Amazon AWS user key");
		ConfigParameter secret = new ConfigParameter("s", "secret", true, "SECRET", "Amazon AWS user secret");
		ConfigParameter region = new ConfigParameter("r", "region", true, "REGION", "Amazon AWS region");

		// Modes
		ConfigParameter helpmode = new ConfigParameter("h", "help", true, "COMMAND", "Show help and examples");
		helpmode.setOptionalArg(true);
		ConfigParameter versionmode = new ConfigParameter("v", "version", false, "Show version information");
		ConfigParameter listmode = new ConfigParameter("l", "list", true, "TYPE", "List Glacier objects (vault|archive)");

		OptionGroup modes = new OptionGroup();
		modes.addOption(listmode);
		modes.addOption(helpmode);
		modes.addOption(versionmode);
		modes.setRequired(true);

		// Options for root
		ConfigMode root = new ConfigMode();
		root.addOptionGroup(modes);
		root.addOption(config);
		root.addOption(key);
		root.addOption(secret);
		root.addOption(region);

		// Options for list
		ConfigMode list = new ConfigMode();
		list.addRequiredOption(listmode);
		list.addOption(region);
		list.addOption(key);
		list.addOption(secret);
		list.addOption(config);

		// Options for help
		ConfigMode help = new ConfigMode();
		help.addRequiredOption(helpmode);

		// Options for version
		ConfigMode version = new ConfigMode();
		version.addRequiredOption(versionmode);

		ConfigModes.addMode(ModeType.ROOT, root);
		ConfigModes.addMode(ModeType.HELP, help);
		ConfigModes.addMode(ModeType.VERSION, version);
		ConfigModes.addMode(ModeType.LIST, list);
	}

	private static void addMode(ModeType type, ConfigMode mode) {
		ConfigModes.getModes().put(type, mode);
	}

	public static ConfigMode getMode(ModeType mode) {
		return ConfigModes.modeMap.get(mode);
	}

	public static Map<ModeType, ConfigMode> getModes() {
		return ConfigModes.modeMap;
	}

	public static ConfigMode getConsolidatedModes() {
		ConfigMode all = new ConfigMode();

		for (ConfigMode mode : ConfigModes.modeMap.values()) {
			for (ConfigParameter opt : mode.getAllOptions()) {
				opt.setRequired(false);
				all.addOption(opt);
			}
		}

		return all;
	}

	public static void printAllHelp() {
		HelpFormatter format = new HelpFormatter();

		for (ModeType mode : ConfigModes.getModes().keySet()) {
			format.setSyntaxPrefix(String.format("%s mode: ", mode.toString()));
			format.printHelp(Version.PROJECT_NAME, ConfigModes.getMode(mode), true);
		}
	}

	public static void printConfigModeHelp(ModeType mode) {
		HelpFormatter format = new HelpFormatter();
		if (mode != ModeType.ROOT) {
			format.setSyntaxPrefix(String.format("Usage for %s mode: ", mode.toString()));
		}
		format.printHelp(Version.PROJECT_NAME, ConfigModes.getMode(mode), true);
	}
}
