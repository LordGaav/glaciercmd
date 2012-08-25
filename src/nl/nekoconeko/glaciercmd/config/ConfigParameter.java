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
