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
