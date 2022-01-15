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

package me.dkim19375.factionsraidtimes.manager

import me.dkim19375.dkimcore.file.YamlFile
import me.dkim19375.factionsraidtimes.FactionsRaidTimes
import me.dkim19375.factionsraidtimes.data.*
import org.bukkit.Bukkit

class RaidTimesManager(private val plugin: FactionsRaidTimes) {
    private val config: YamlFile
        get() = plugin.mainConfig
    var lastMinute: Pair<String, Int>? = null
    var raid: String? = null
    var firstRaid = false

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            val raidTimes = config.get(MainConfigData.RAID_TIMES)
            val time = DateData.getCurrentTime()
            val newRaid = raidTimes.entries.firstOrNull { it.value.contains(time) }
            if (raid != null) {
                val contains = raidTimes[raid]?.contains(time) == true
                if (!contains) {
                    raid = null
                    firstRaid = false
                    val action = config.get(MainConfigData.END_ACTION)
                    for (player in Bukkit.getOnlinePlayers()) {
                        action.execute(player)
                    }
                }
            }
            if (newRaid != null) {
                if (raid != newRaid.key) {
                    firstRaid = true
                }
                if (firstRaid) {
                    lastMinute = null
                }
                if (lastMinute?.first != newRaid.key) {
                    lastMinute = null
                }
                val firstRaid = firstRaid
                this.firstRaid = false
                raid = newRaid.key
                if (firstRaid) {
                    val action = config.get(MainConfigData.START_ACTION)
                    for (player in Bukkit.getOnlinePlayers()) {
                        action.execute(player)
                    }
                }
                val timeUntil = newRaid.value.distanceEnd(time).getTotalMinutes()
                if (timeUntil == lastMinute?.second) {
                    return@Runnable
                }
                lastMinute = newRaid.key to timeUntil
                val actions = config.get(MainConfigData.END_WARNING_ACTIONS).values
                    .filter { timeUntil in it.times }
                    .ifEmpty { return@Runnable }
                for (player in Bukkit.getOnlinePlayers()) {
                    actions.forEach { action ->
                        action.execute(player, timeUntil)
                    }
                }
                return@Runnable
            }
            val closest: Map.Entry<String, RaidTimeData>? = raidTimes.entries.reduceOrNull { acc, data ->
                if (acc.value.distance(time) < data.value.distance(time)) {
                    acc
                } else {
                    data
                }
            }
            if (closest == null) {
                lastMinute = null
                return@Runnable
            }
            val lastMinute = lastMinute
            if (lastMinute != null && lastMinute.first != closest.key) {
                this.lastMinute = null
            }
            val last = lastMinute
            val timeUntil = closest.value.distance(time).getTotalMinutes()
            if (last?.second == timeUntil) {
                return@Runnable
            }
            this.lastMinute = closest.key to timeUntil
            val actions = config.get(MainConfigData.WARNING_ACTIONS).values
                .filter { timeUntil in it.times }
                .ifEmpty { return@Runnable }
            for (player in Bukkit.getOnlinePlayers()) {
                actions.forEach { action ->
                    action.execute(player, timeUntil)
                }
            }
        }, 1L, 1L)
    }
}