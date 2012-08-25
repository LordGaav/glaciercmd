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
