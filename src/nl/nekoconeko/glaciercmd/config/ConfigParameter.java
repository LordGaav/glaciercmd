package nl.nekoconeko.glaciercmd.config;

import org.apache.commons.cli.Option;

public class ConfigParameter extends Option {
	private static final long serialVersionUID = 7833490230496409205L;

	public ConfigParameter(String opt, String description) throws IllegalArgumentException {
		super(opt, description);
	}

	public ConfigParameter(String opt, boolean hasArg, String description) throws IllegalArgumentException {
		super(opt, hasArg, description);
	}

	public ConfigParameter(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
		super(opt, longOpt, hasArg, description);
	}

	public ConfigParameter(String opt, String longOpt, boolean hasArg, String argName, String description) {
		super(opt, longOpt, hasArg, description);
		this.setArgName(argName);
	}

	public ConfigParameter(String longOpt, boolean hasArg, String argName, String description) {
		super(null, longOpt, hasArg, description);
		this.setArgName(argName);
	}
}
