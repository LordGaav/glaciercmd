package nl.nekoconeko.glaciercmd.config;

import java.util.Comparator;

public class ConfigModeSorter {
	public static final Comparator<ConfigParameter> CONFIGPARAMETER_ALPHANUM = new Comparator<ConfigParameter>() {
		@Override
		public int compare(ConfigParameter o1, ConfigParameter o2) {
			String o1s = String.format("%s%s", (o1.getOpt() == null ? "" : o1.getOpt()), o1.getLongOpt());
			String o2s = String.format("%s%s", (o2.getOpt() == null ? "" : o2.getOpt()), o2.getLongOpt());
			return o1s.compareTo(o2s);
		}
	};
}
