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

import me.dkim19375.dkimbukkitcore.function.*
import org.bukkit.Sound
import org.bukkit.entity.Player

data class SoundData(var name: String = "", var volume: Float = 1f, var pitch: Float = 1f) {
    fun verify() {
        try {
            Sound.valueOf(name.uppercase())
        } catch (_: IllegalArgumentException) {
            throw ConfigurationException("${name.uppercase()} is not a valid Sound!")
        }
        if (volume < 0f) {
            throw ConfigurationException("The volume $volume is below 0!")
        }
        if (pitch < 0f) {
            throw ConfigurationException("The pitch $pitch is below 0!")
        }
    }

    fun playSound(player: Player) = enumValueOf<Sound>(name.formatAll(player)).let { sound ->
        player.playSound(sound, volume, pitch)
    }
}