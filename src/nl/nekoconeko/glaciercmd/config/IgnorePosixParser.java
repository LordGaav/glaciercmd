package nl.nekoconeko.glaciercmd.config;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class IgnorePosixParser extends PosixParser {
	private boolean ignoreUnknown = false;

	public IgnorePosixParser() {
		super();
	}

	public IgnorePosixParser(boolean ignore) {
		super();
		this.setIgnoreUnknown(ignore);
	}

	protected void setIgnoreUnknown(boolean ignore) {
		this.ignoreUnknown = ignore;
	}

	@SuppressWarnings("rawtypes")
	protected void processOption(final String arg, final ListIterator iter) throws ParseException {
		boolean hasOption = this.getOptions().hasOption(arg);

		if (hasOption || !this.ignoreUnknown) {
			super.processOption(arg, iter);
		}
	}

	@SuppressWarnings("rawtypes")
	protected void checkRequiredOptions() throws MissingOptionException {
		List requiredOptions = this.getRequiredOptions();
		if (!requiredOptions.isEmpty()) {
			Iterator it = requiredOptions.iterator();
			while (it.hasNext()) {
				Object opt = it.next();
				if (opt.getClass().equals(String.class)) {
					if (Configuration.has(((String) opt))) {
						it.remove();
					}
				} else if (opt.getClass().equals(Option.class)) {
					if (Configuration.has(((Option) opt).getLongOpt())) {
						it.remove();
					}
				}
			}
		}
		if (!requiredOptions.isEmpty()) {
			throw new MissingOptionException(requiredOptions);
		}
	}
}
