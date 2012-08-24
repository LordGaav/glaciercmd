package nl.nekoconeko.glaciercmd.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ConfigMode extends Options {
	private static final long serialVersionUID = 843228815544013345L;

	public ConfigMode() {
		super();
	}

	public void addRequiredOption(Option opt) {
		opt.setRequired(true);
		this.addOption(opt);
	}

	public List<ConfigParameter> getAllOptions() {
		List<ConfigParameter> all = new ArrayList<ConfigParameter>(this.getOptions().size());

		for (Object item : this.getOptions()) {
			all.add((ConfigParameter) item);
		}

		return all;
	}

	public void addOptions(List<ConfigParameter> options) {
		for (ConfigParameter opt : options) {
			this.addOption(opt);
		}
	}
}
