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

package me.dkim19375.factionsraidtimes.listener

import me.dkim19375.factionsraidtimes.FactionsRaidTimes
import org.bukkit.entity.EntityType
import org.bukkit.event.*
import org.bukkit.event.entity.EntityExplodeEvent

class ExplosionListener(private val plugin: FactionsRaidTimes) : Listener {
    private val entityTypes = setOf(
        EntityType.PRIMED_TNT, EntityType.MINECART_TNT, EntityType.ENDER_CRYSTAL, EntityType.CREEPER
    )

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun EntityExplodeEvent.onEntityExplode() {
        if (entityType !in entityTypes) {
            return
        }
        plugin.manager.raid?.let {
            return@onEntityExplode
        }
        `yield` = 0f
        blockList().clear()
    }
}