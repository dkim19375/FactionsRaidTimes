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

package me.dkim19375.factionsraidtimes.data

import me.dkim19375.dkimbukkitcore.function.formatAll
import me.dkim19375.factionsraidtimes.util.toComponent
import org.bukkit.Sound
import org.bukkit.entity.Player

data class RaidWarningActions(
    var title: TitleData = TitleData(),
    var actionBar: String = "",
    @Suppress("SpellCheckingInspection")
    var chat: List<String> = listOf(
        "&cThere are &6%minutes% &cminutes remaining until raiding enables!"
    ),
    var sounds: List<SoundData> = listOf(SoundData(
        name = Sound.BLOCK_NOTE_BLOCK_PLING.name,
        volume = 0.8f,
        pitch = 7.0f
    )),
    var times: List<Int> = listOf(120, 90, 60, 30),
) {
    fun verify() {
        sounds.forEach(SoundData::verify)
    }

    fun execute(player: Player, minutesRemaining: Int) {
        title.execute(player, minutesRemaining)
        if (chat.isNotEmpty()) {
            val message = chat.joinToString("\n")
                .replace("%minutes%", minutesRemaining.toString())
                .formatAll(player)
                .toComponent()
            player.sendMessage(message)
        }
        sounds.forEach { it.playSound(player) }
    }
}