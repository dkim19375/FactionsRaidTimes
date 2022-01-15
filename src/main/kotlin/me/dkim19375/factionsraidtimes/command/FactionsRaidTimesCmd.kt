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

package me.dkim19375.factionsraidtimes.command

import me.dkim19375.factionsraidtimes.FactionsRaidTimes
import me.dkim19375.factionsraidtimes.util.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.command.*

class FactionsRaidTimesCmd(private val plugin: FactionsRaidTimes) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission(Permissions.COMMAND)) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION)
            return true
        }
        if (args.isEmpty()) {
            sender.sendHelpMessage(label)
            return true
        }
        when (args[0].lowercase()) {
            "help" -> {
                sender.sendHelpMessage(label, args.getOrNull(1)?.toIntOrNull()?.coerceAtLeast(1) ?: 1)
                return true
            }
            "reload" -> {
                if (!sender.hasPermission(Permissions.RELOAD)) {
                    sender.sendMessage(ErrorMessages.NO_PERMISSION)
                    return true
                }
                plugin.reloadConfig()
                sender.sendMessage(
                    Component.text("Successfully reloaded the config files!").color(NamedTextColor.GREEN)
                )
                return true
            }
            "status" -> {
                if (!sender.hasPermission(Permissions.STATUS)) {
                    sender.sendMessage(ErrorMessages.NO_PERMISSION)
                    return true
                }
                val manager = plugin.manager
                val raid = manager.raid
                val lastMinute = manager.lastMinute
                sender.sendMessage("""
                    ${ChatColor.GOLD}Current Raid Name: ${if (raid != null) {
                        "${ChatColor.GREEN}$raid"
                    } else {
                        "${ChatColor.RED}None"
                    }}
                    ${ChatColor.AQUA}(DEBUG)
                    ${ChatColor.GOLD}Last minute: ${ChatColor.GREEN}${lastMinute?.first}:${lastMinute?.second}
                """.trimIndent().toComponent())
                return true
            }
            "reset" -> {
                if (!sender.hasPermission(Permissions.RESET)) {
                    sender.sendMessage(ErrorMessages.NO_PERMISSION)
                    return true
                }
                plugin.manager.lastMinute = null
                plugin.manager.raid = null
                sender.sendMessage(Component.text("Successfully reset!").color(NamedTextColor.GREEN))
                return true
            }
            else -> {
                sender.sendMessage(ErrorMessages.INVALID_ARG)
                return true
            }
        }
    }
}