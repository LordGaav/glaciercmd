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
package nl.nekoconeko.glaciercmd.config;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.nekoconeko.glaciercmd.Formatter;
import nl.nekoconeko.glaciercmd.constants.AWSGlacierRegion;
import nl.nekoconeko.glaciercmd.types.ListType;
import nl.nekoconeko.glaciercmd.types.ModeType;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Configuration {

	private static final String[] connection = new String[] { "key", "secret", "region" };
	private static final String[] input = new String[] {};

	private static Map<String, Object> configuration = new HashMap<String, Object>();

	protected static boolean has(String key) {
		return Configuration.configuration.containsKey(key);
	}

	protected static void set(String key, Object value) {
		Configuration.configuration.put(key, value);
	}

	protected static Object valueOrNull(String key) {
		if (Configuration.configuration.containsKey(key)) {
			return Configuration.configuration.get(key);
		} else {
			return null;
		}
	}

	public static boolean hasMode() {
		return Configuration.has("mode");
	}

	public static ModeType getMode() {
		return (ModeType) Configuration.valueOrNull("mode");
	}

	public static void setMode(ModeType mode) {
		Configuration.set("mode", mode);
	}

	public static boolean hasListType() {
		return Configuration.has("list-type");
	}

	public static ListType getListType() {
		return (ListType) Configuration.valueOrNull("list-type");
	}

	public static void setListType(ListType listType) {
		Configuration.set("list-type", listType);
	}

	public static boolean hasHelpType() {
		return Configuration.has("help-type");
	}

	public static ModeType getHelpType() {
		return (ModeType) Configuration.valueOrNull("help-type");
	}

	public static void setHelpType(ModeType mode) {
		Configuration.set("help-type", mode);
	}

	public static boolean hasKey() {
		return Configuration.has("key");
	}

	public static String getKey() {
		return (String) Configuration.valueOrNull("key");
	}

	public static void setKey(String key) {
		Configuration.set("key", key);
	}

	public static boolean hasSecret() {
		return Configuration.has("secret");
	}

	public static String getSecret() {
		return (String) Configuration.valueOrNull("secret");
	}

	public static void setSecret(String secret) {
		Configuration.set("secret", secret);
	}

	public static boolean hasRegion() {
		return Configuration.has("region");
	}

	public static AWSGlacierRegion getRegion() {
		return (AWSGlacierRegion) Configuration.valueOrNull("region");
	}

	public static void setRegion(AWSGlacierRegion region) {
		Configuration.set("region", region);
	}

	public static boolean hasVault() {
		return Configuration.has("vault");
	}

	public static String getVault() {
		return (String) Configuration.valueOrNull("vault");
	}

	public static void setVault(String vault) {
		Configuration.set("vault", vault);
	}

	public static boolean hasJobId() {
		return Configuration.has("job-id");
	}

	public static String getJobId() {
		return (String) Configuration.valueOrNull("job-id");
	}

	public static void setJobId(String jobid) {
		Configuration.set("job-id", jobid);
	}

	public static boolean hasInputFile() {
		return Configuration.has("input");
	}

	public static String getInputFile() {
		return (String) Configuration.valueOrNull("input");
	}

	public static void setInputFile(String input) {
		Configuration.set("input", input);
	}

	public static boolean hasOutputFile() {
		return Configuration.has("output");
	}

	public static String getOutputFile() {
		return (String) Configuration.valueOrNull("output");
	}

	public static void setOutputFile(String output) {
		Configuration.set("output", output);
	}

	public static boolean hasDescription() {
		return Configuration.has("description");
	}

	public static String getDescription() {
		return (String) Configuration.valueOrNull("description");
	}

	public static void setDescription(String description) {
		Configuration.set("description", description);
	}

	public static boolean hasArchive() {
		return Configuration.has("archive");
	}

	public static String getArchive() {
		return (String) Configuration.valueOrNull("archive");
	}

	public static void setArchive(String archive) {
		Configuration.set("archive", archive);
	}

	public static CommandLine parseCli(ModeType mode, String[] args) {
		CommandLine cli = null;
		Options opt = ConfigModes.getMode(mode);
		try {
			cli = new IgnorePosixParser(true).parse(opt, args);
		} catch (MissingArgumentException me) {
			Formatter.usageError(me.getLocalizedMessage(), mode);
			System.exit(-1);
		} catch (MissingOptionException mo) {
			Formatter.usageError(mo.getLocalizedMessage(), mode);
			System.exit(-1);
		} catch (AlreadySelectedException ase) {
			Formatter.usageError(ase.getLocalizedMessage(), mode);
		} catch (UnrecognizedOptionException uoe) {
			Formatter.usageError(uoe.getLocalizedMessage(), mode);
		} catch (ParseException e) {
			Formatter.printStackTrace(e);
			System.exit(-1);
		}

		return cli;
	}

	public static void load(ModeType mode, String[] args) {
		CommandLine cli = Configuration.parseCli(mode, args);
		Configuration.load(cli);
	}

	public static void load(CommandLine cli) {
		for (Option opt : cli.getOptions()) {
			if (cli.hasOption(opt.getLongOpt())) {
				if (opt.getLongOpt().equals("help")) {
					Configuration.setMode(ModeType.HELP);
					if (cli.getOptionValue(opt.getLongOpt()) != null) {
						Configuration.setHelpType(ModeType.valueOf(cli.getOptionValue(opt.getLongOpt()).toUpperCase()));
					}
				} else if (opt.getLongOpt().equals("version")) {
					Configuration.setMode(ModeType.VERSION);
				} else if (opt.getLongOpt().equals("list")) {
					Configuration.setMode(ModeType.LIST);
					Configuration.setListType(ListType.valueOf(cli.getOptionValue(opt.getLongOpt()).toUpperCase()));
				} else if (opt.getLongOpt().equals("init-inventory")) {
					Configuration.setMode(ModeType.INITIATEINVENTORY);
				} else if (opt.getLongOpt().equals("get-inventory")) {
					Configuration.setMode(ModeType.GETINVENTORY);
				} else if (opt.getLongOpt().equals("upload")) {
					Configuration.setMode(ModeType.UPLOAD);
				} else if (opt.getLongOpt().equals("download")) {
					Configuration.setMode(ModeType.DOWNLOAD);
				} else if (opt.getLongOpt().equals("init-download")) {
					Configuration.setMode(ModeType.INITIATEDOWNLOAD);
				} else if (opt.getLongOpt().equals("get-download")) {
					Configuration.setMode(ModeType.GETDOWNLOAD);
				} else if (opt.getLongOpt().equals("region")) {
					Configuration.setRegion(AWSGlacierRegion.valueOf(cli.getOptionValue(opt.getLongOpt()).toUpperCase()));
				} else {
					Configuration.set(opt.getLongOpt(), cli.getOptionValue(opt.getLongOpt()));
				}
			}
		}
	}

	public static void loadFile(String filename) {
		org.apache.commons.configuration.Configuration conf = null;
		try {
			conf = new PropertiesConfiguration(filename);
		} catch (ConfigurationException e) {
			Formatter.printErrorLine("Failed to load configuration file");
			Formatter.printErrorLine(e.getLocalizedMessage());
			return;
		}

		Iterator<String> i = conf.getKeys();
		while (i.hasNext()) {
			String key = i.next();

			if (key.equals("help")) {
				Configuration.setMode(ModeType.HELP);
			} else if (key.equals("version")) {
				Configuration.setMode(ModeType.VERSION);
			} else if (key.equals("list")) {
				Configuration.setMode(ModeType.LIST);
				Configuration.setListType(ListType.valueOf(conf.getString(key).toUpperCase()));
			} else if (key.equals("region")) {
				Configuration.setRegion(AWSGlacierRegion.valueOf(conf.getString(key).toUpperCase()));
			} else {
				Configuration.set(key, conf.getString(key));
			}
		}
	}

	public String toString() {
		return Configuration.dumpToString();
	}

	public static String dumpToString() {
		return Configuration.dumpToString(ConfigModes.getConsolidatedModes());
	}

	public static String dumpToString(ModeType mode) {
		return Configuration.dumpToString(ConfigModes.getMode(mode));
	}

	public static String dumpToString(ConfigMode mode) {
		StringBuilder dump = new StringBuilder();

		dump.append(String.format("Selected operation: %s\n", Configuration.getMode().toString()));
		if (Configuration.getMode() == ModeType.LIST) {
			dump.append(String.format("Selected mode: %s\n", Configuration.getListType().toString()));
		}

		dump.append("Connection configuration: \n");
		for (String in : Configuration.connection) {
			if (Configuration.has(in) && mode.hasOption(in)) {
				if (in.equals("password")) {
					continue;
				}
				dump.append(String.format("\t %s: %s\n", in, Configuration.valueOrNull(in)));
			}
		}

		dump.append("User input: \n");
		for (String in : Configuration.input) {
			if (Configuration.has(in) && mode.hasOption(in)) {
				if (in.equals("ip")) {
					dump.append(String.format("\t %s: %s\n", in, ((InetAddress) Configuration.valueOrNull(in)).getHostAddress()));
				} else {
					dump.append(String.format("\t %s: %s\n", in, Configuration.valueOrNull(in)));
				}
			}
		}

		return dump.toString();
	}
}
