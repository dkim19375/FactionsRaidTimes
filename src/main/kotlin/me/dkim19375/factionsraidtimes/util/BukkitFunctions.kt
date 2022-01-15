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

package me.dkim19375.factionsraidtimes.util

import me.dkim19375.dkimbukkitcore.data.*
import me.dkim19375.dkimbukkitcore.function.*
import me.dkim19375.factionsraidtimes.FactionsRaidTimes
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permissible
import org.bukkit.plugin.java.JavaPlugin

private val plugin by lazy { JavaPlugin.getPlugin(FactionsRaidTimes::class.java) }

private val commands = listOf(
    HelpMessage("help [page]", "Shows the help menu", Permissions.COMMAND.perm),
    HelpMessage("reload", "Reload the config files", Permissions.RELOAD.perm),
    HelpMessage("status", "View the status of the raid", Permissions.STATUS.perm),
    HelpMessage("reset", "Reset the saved countdown data (memory)", Permissions.RESET.perm)
)

private val format = HelpMessageFormat(
    topBar = null,
    header = "${ChatColor.GREEN}%name% v%version% Help Page: %page%/%maxpages%",
    bottomBar = null
)

fun CommandSender.sendHelpMessage(
    label: String,
    page: Int = 1,
) = showHelpMessage(
    label = label,
    error = null,
    page = page,
    commands = commands,
    plugin = plugin,
    format = format
)

fun Permissible.hasPermission(permissions: Permissions): Boolean = hasPermission(permissions.perm)

fun CommandSender.sendMessage(error: ErrorMessages) = sendMessage(error.component)

fun Permissible.getMaxHelpPages(): Int = getMaxHelpPages(commands)

/*fun String.getWorldTime(): Int {
    val split = split(':')
    if (split.size < 2) {
        throw ConfigurationException("Time $this is invalid! (Format: hour:minute, hour = 0 -> 24)")
    }
    val hour = split[0].toIntOrNull()?.takeIf { it in 0..24 } ?: run {
        throw ConfigurationException("Hour of time $this is invalid!")
    }
    val minute = split[1].toIntOrNull() ?: run {
        throw ConfigurationException("Minute of time $this is invalid!")
    }
    val time = (hour * 1000) + (minute / 0.06).roundToInt()
    return (time + 18000) % 24000
}*/

/*
fun Number.worldTimeToMinutes(): Int {
    val time = toInt()
    if (time < 0 || time > 24000) {
        throw IllegalArgumentException("Time cannot be below 0 or above 24000! (Was $time)")
    }
    return (time * 0.06).toInt()
}*/
