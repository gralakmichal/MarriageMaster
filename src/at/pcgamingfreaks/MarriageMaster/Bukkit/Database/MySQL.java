/*
 *   Copyright (C) 2016, 2018 GeorgH93
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.pcgamingfreaks.MarriageMaster.Bukkit.Database;

import at.pcgamingfreaks.MarriageMaster.Bukkit.MarriageMaster;

import com.zaxxer.hikari.HikariConfig;

public class MySQL extends SQL
{
	protected MySQL(MarriageMaster marriageMaster)
	{
		super(marriageMaster);
	}

	@Override
	protected HikariConfig getPoolConfig()
	{
		HikariConfig poolConfig = new HikariConfig();
		poolConfig.setJdbcUrl("jdbc:mysql://" + plugin.getConfiguration().getSQLHost() + "/" + plugin.getConfiguration().getSQLDatabase() + "?allowMultiQueries=true" + plugin.getConfiguration().getSQLConnectionProperties());
		poolConfig.setUsername(plugin.getConfiguration().getSQLUser());
		poolConfig.setPassword(plugin.getConfiguration().getSQLPassword());
		poolConfig.setMinimumIdle(1);
		poolConfig.setMaximumPoolSize(plugin.getConfiguration().getSQLMaxConnections());
		return poolConfig;
	}

	@Override
	protected boolean supportBungee()
	{
		return true;
	}

	@Override
	public String getDatabaseTypeName()
	{
		return "MySQL";
	}

	@Override
	protected String getEngine()
	{
		return " ENGINE=InnoDB";
	}

	@Override
	protected void buildQuerys()
	{
		if(useUUIDs)
		{
			queryAddPlayer = "INSERT IGNORE INTO {TPlayers} ({FName},{FUUID},{FShareBackpack}) SELECT ?,?,? FROM (SELECT 1) AS `tmp` WHERE NOT EXISTS (SELECT * FROM {TPlayers} WHERE {FUUID}=?);";
			if(plugin.getBackpacksIntegration() == null)
			{
				queryAddPlayer = queryAddPlayer.replaceAll(",\\{FShareBackpack}\\) SELECT \\?,", ") SELECT ");
			}
		}
		super.buildQuerys();
	}
}