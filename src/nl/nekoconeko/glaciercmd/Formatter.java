package nl.nekoconeko.glaciercmd;

import nl.nekoconeko.glaciercmd.config.ConfigModes;
import nl.nekoconeko.glaciercmd.types.ModeType;

import org.apache.commons.lang.StringUtils;

public class Formatter {
	public static void usageError(String error, ModeType mode) {
		Formatter.printErrorLine(error + "\n");
		ConfigModes.printConfigModeHelp(mode);
		System.exit(-1);
	}

	public static String getHeader() {
		StringBuilder header = new StringBuilder();

		header.append(String.format("%s version %s\n", Version.PROJECT_NAME, Version.RELEASE_VERSION));
		header.append(String.format("BUILD_VERSION: %s\n", Version.BUILD_VERSION));

		return header.toString();
	}

	public static void printHeader() {
		Formatter.printBorderedInfo(Formatter.getHeader());
	}

	public static void printError(Object error) {
		System.err.print(error);
	}

	public static void printInfo(Object info) {
		System.out.print(info);
	}

	public static void printErrorLine(Object error) {
		System.err.println(error);
	}

	public static void printInfoLine(Object info) {
		System.out.println(info);
	}

	public static void printBorderedInfo(Object info) {
		Formatter.printBordered(info, false);
	}

	public static void printBorderedError(Object error) {
		Formatter.printBordered(error, true);
	}

	private static void printBordered(Object msg, boolean error) {
		String[] lines = msg.toString().split("\n");
		int longest = 0;
		for (String line : lines) {
			if (line.length() > longest) {
				longest = line.length();
			}
		}
		if (error) {
			Formatter.printErrorLine(StringUtils.repeat("-", longest));
			Formatter.printError(msg);
			Formatter.printErrorLine(StringUtils.repeat("-", longest));
		} else {
			Formatter.printInfoLine(StringUtils.repeat("-", longest));
			Formatter.printInfo(msg);
			Formatter.printInfoLine(StringUtils.repeat("-", longest));
		}
	}

	public static void printStackTrace(Throwable e) {
		e.printStackTrace(System.err);
	}
}
