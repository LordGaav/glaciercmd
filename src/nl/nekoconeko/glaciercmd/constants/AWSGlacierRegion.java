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
package nl.nekoconeko.glaciercmd.constants;

public enum AWSGlacierRegion {
	VIRGINIA("glacier.us-east-1.amazonaws.com"), OREGON("glacier.us-west-2.amazonaws.com"), CALIFORNIA("glacier.us-west-1.amazonaws.com"), IRELAND("glacier.eu-west-1.amazonaws.com"), TOKYO("glacier.ap-northeast-1.amazonaws.com");

	private String endpoint;

	private AWSGlacierRegion(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getEndpoint() {
		return this.endpoint;
	}
}
