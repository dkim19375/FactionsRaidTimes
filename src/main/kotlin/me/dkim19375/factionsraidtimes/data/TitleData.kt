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
import me.mattstudios.config.annotations.Name
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import java.time.Duration

data class TitleData(
    var title: String = "Sample Title",
    @Name("sub-title")
    var subTitle: String = "Sample Subtitle",
    @Name("fade-in")
    var fadeIn: Int = 10,
    var stay: Int = 50,
    @Name("fade-out")
    var fadeOut: Int = 10
) {
    fun execute(player: Player, minutesRemaining: Int = 0) {
        if (title.isBlank()) {
            return
        }
        val title = title.replace("%minutes%", minutesRemaining.toString()).formatAll(player).toComponent()
        val subTitle = subTitle.replace("%minutes%", minutesRemaining.toString()).formatAll(player).toComponent()
        player.showTitle(Title.title(title, subTitle, fadeIn.takeIf { it >= 0 }?.let {
            Title.Times.of(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
            )
        }))
    }
}