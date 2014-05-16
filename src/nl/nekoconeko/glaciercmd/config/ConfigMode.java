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
        
        public void addOptionalOption(Option opt) {
                opt.setRequired(false);
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
