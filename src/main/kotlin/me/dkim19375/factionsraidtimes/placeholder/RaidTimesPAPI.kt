/*
 *     FactionsRaidTimes, a spigot plugin for raids
 *     Copyright (C) 2022  dkim19375
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.dkim19375.factionsraidtimes.placeholder

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.dkim19375.dkimbukkitcore.function.formatAll
import me.dkim19375.factionsraidtimes.FactionsRaidTimes
import me.dkim19375.factionsraidtimes.data.*
import org.bukkit.*

class RaidTimesPAPI(private val plugin: FactionsRaidTimes) : PlaceholderExpansion() {
    override fun persist(): Boolean = true

    override fun canRegister(): Boolean = true

    override fun getAuthor(): String = plugin.description.authors.joinToString()

    override fun getIdentifier(): String = "raid"

    override fun getVersion(): String = plugin.description.version

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        if (params.isBlank()) {
            return null
        }
        val split = params.split('_')
        val placeholderConfig = plugin.mainConfig.get(MainConfigData.PLACEHOLDER)
        val timeFormat = plugin.mainConfig.get(MainConfigData.TIME_FORMAT)
        when (split[0].lowercase()) {
            "countdown" -> {
                val raidData = plugin.manager.raid?.let { name ->
                    plugin.mainConfig.get(MainConfigData.RAID_TIMES)[name]
                }
                val time = SpecificTimeData.getCurrentTime()
                if (raidData != null) {
                    return placeholderConfig.countdown.ending
                        .replace("%time%", timeFormat.formatTime(raidData.distanceEnd(time)))
                        .formatAll(player)
                }
                val data = plugin.manager.lastMinute?.first?.let { name ->
                    plugin.mainConfig.get(MainConfigData.RAID_TIMES)[name]
                } ?: return "${ChatColor.RED}None configured!"
                return placeholderConfig.countdown.starting
                    .replace("%time%", timeFormat.formatTime(data.distance(time)))
                    .formatAll(player)
            }
            else -> return null
        }
    }
}