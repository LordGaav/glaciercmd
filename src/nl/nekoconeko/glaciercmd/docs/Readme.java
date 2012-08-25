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
package nl.nekoconeko.glaciercmd.docs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import nl.nekoconeko.glaciercmd.Version;
import nl.nekoconeko.glaciercmd.config.ConfigModeSorter;
import nl.nekoconeko.glaciercmd.config.ConfigModes;
import nl.nekoconeko.glaciercmd.config.ConfigParameter;
import nl.nekoconeko.glaciercmd.constants.AWSGlacierRegion;
import nl.nekoconeko.glaciercmd.types.ModeType;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang.StringUtils;

public class Readme {

	private String getNameSection() {
		StringBuilder section = new StringBuilder();
		String header = String.format("%s - a simple tool to consume Amazon's Glacier AWS service\n", Version.PROJECT_NAME);
		section.append(header);
		section.append(StringUtils.repeat("-", header.length()) + "\n\n");
		return section.toString();
	}

	private String getSynopsisSection() {
		StringBuilder section = new StringBuilder();
		section.append("SYNOPSIS\n");
		section.append("--------\n");

		HelpFormatter help = new HelpFormatter();
		help.setSyntaxPrefix("");

		Writer str = new StringWriter();
		PrintWriter pw = new PrintWriter(str);

		help.printUsage(pw, 1000, Version.PROJECT_NAME, ConfigModes.getMode(ModeType.ROOT));
		section.append("\t\n\t" + str.toString() + "\n");

		for (ModeType m : ModeType.values()) {
			if (m == ModeType.ROOT) {
				continue;
			}
			try {
				str = new StringWriter();
				pw = new PrintWriter(str);
				help.printUsage(pw, 1000, Version.PROJECT_NAME, ConfigModes.getMode(m));
			} catch (NullPointerException e) {
				continue;
			}

			section.append(String.format("**%s**\n\n", m.toString()));
			section.append("\t" + str.toString() + "\n");
		}
		return section.toString();
	}

	private String getDescriptionSection() {
		StringBuilder section = new StringBuilder();
		section.append(String.format("**%s** ", Version.PROJECT_NAME));
		section.append("is a simple tool to consume Amazon's Glacier AWS service, using the AWS REST API.\n\n");
		section.append("There are several modes of operation, all of which can be found in the synopsis below. ");
		section.append("Each mode has a different set of required and optional arguments, which can also be found in the synopsis. ");
		section.append("Help mode can be used in a context sensitive manner. For example, ");
		section.append("*-h* ");
		section.append("will show all the modes, and ");
		section.append("*-h LIST* ");
		section.append("will show help about the LIST mode.\n\n");
		section.append("All commands require proper authentication. This can be provided on the command line by using ");
		section.append("*-k -s -r* ");
		section.append("or by creating a configuration file and specifying it with ");
		section.append("*-c config-file*\n\n");
		section.append(String.format("**%s** is licensed under the GPLv3 license. For more information, see the *LICENSE* file.\n", Version.PROJECT_NAME));
		section.append(String.format("This project uses libraries and routines which may have a different license. Refer to the included licenses in the source files and/or JAR files for more information.\n\n"));
		return section.toString();
	}

	private String getOptionsSection() {
		StringBuilder section = new StringBuilder();
		section.append("OPTIONS\n");
		section.append("-------\n");

		List<ConfigParameter> options = ConfigModes.getConsolidatedModes().getAllOptions();
		Collections.sort(options, ConfigModeSorter.CONFIGPARAMETER_ALPHANUM);

		for (Object opt : options) {
			if (!opt.getClass().equals(ConfigParameter.class)) {
				continue;
			}
			ConfigParameter o = (ConfigParameter) opt;
			if (o.getOpt() != null) {
				section.append(String.format("**-%s** ", o.getOpt()));
			}
			if (o.getLongOpt() != null) {
				section.append(String.format("**--%s** ", o.getLongOpt()));
			}
			if (o.hasArg() && o.hasArgName()) {
				section.append(String.format("*%s* ", o.getArgName()));
			}
			if (o.getDescription() != null) {
				section.append("\n\n");
				section.append(o.getDescription() + "\n");
			}
			section.append("\n");
		}

		return section.toString();
	}

	private String getConfigurationSection() {
		StringBuilder section = new StringBuilder();
		section.append("CONFIGURATION\n");
		section.append("-------------\n");
		section.append("All command line parameters can optionally be provided using a configuration file. Exception on this are the mode selectors. ");
		section.append("The configuration file uses a simple format, which is:\n\n");
		section.append("\toption");
		section.append("=");
		section.append("value\n\n");
		section.append("*option* ");
		section.append("is the same as the long options which can be specified on the command line. For example, this is a valid configuration line:\n\n");
		section.append("\tregion=ireland\n\n");
		section.append("Configuration options are parsed in the following order: \n\n");
		section.append("1. The ");
		section.append("*-c* ");
		section.append("option.\n");
		section.append("2. All options provided on the command line, in the order they are specified.\n\n");
		section.append("It is possible to override already specified configuration options by specifying them again. Duplicate options will take ");
		section.append("the value of the last one specified. An example configuration file can be found in the distribution package.\n\n");
		return section.toString();
	}

	private String getBugsSection() {
		StringBuilder section = new StringBuilder();
		section.append("BUGS\n");
		section.append("----\n");
		section.append("1. The  DOWNLOAD  mode  currently only works for the VIRGINIA region, because of a bug in the AWS SDK. ");
		section.append("All other regions will cause an exception, because the SDK contains hardcoded references to the VIRGINIA region.\n\n");
		return section.toString();
	}

	private String getAuthorsSection() {
		StringBuilder section = new StringBuilder();
		section.append("AUTHOR\n");
		section.append("------\n");
		section.append("Nick Douma (n.douma@nekoconeko.nl)\n\n");
		return section.toString();
	}

	private String getBuildSection() {
		StringBuilder section = new StringBuilder();
		section.append("BUILDING\n");
		section.append("--------\n");
		section.append(String.format("Building **%s** requires the following:\n\n", Version.PROJECT_NAME));
		section.append("1. Oracle Java or OpenJDK >= 6\n");
		section.append("2. Apache Ant >= 1.8\n\n");
		section.append(String.format("Then you can simply call `ant dist` to create a *dist* folder with everything %s needs to run. ", Version.PROJECT_NAME));
		section.append("You can also use `ant package-tar` to create a tarball\n\n");

		return section.toString();
	}

	private String getRegionsSection() {
		StringBuilder section = new StringBuilder();
		section.append("REGIONS\n");
		section.append("-------\n");
		section.append(String.format("**%s** supports the following AWS Glacier regions:\n\n", Version.PROJECT_NAME));
		int i = 1;
		for (AWSGlacierRegion region : AWSGlacierRegion.values()) {
			section.append(String.format("%d. %s - %s\n", i, region.name(), region.getEndpoint()));
			i++;
		}
		section.append("\n");
		return section.toString();
	}

	public static void main(String[] args) {
		Readme r = new Readme();
		System.out.print(r.getNameSection());
		System.out.print(r.getDescriptionSection());
		System.out.print(r.getBuildSection());
		System.out.print(r.getSynopsisSection());
		System.out.print(r.getOptionsSection());
		System.out.print(r.getConfigurationSection());
		System.out.print(r.getRegionsSection());
		System.out.print(r.getBugsSection());
		System.out.print(r.getAuthorsSection());
	}
}
