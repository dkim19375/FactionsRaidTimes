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

package me.dkim19375.factionsraidtimes

import me.dkim19375.dkimbukkitcore.function.logInfo
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.file.YamlFile
import me.dkim19375.factionsraidtimes.command.*
import me.dkim19375.factionsraidtimes.data.MainConfigData
import me.dkim19375.factionsraidtimes.listener.ExplosionListener
import me.dkim19375.factionsraidtimes.manager.RaidTimesManager
import me.dkim19375.factionsraidtimes.placeholder.RaidTimesPAPI
import java.io.File
import kotlin.system.measureTimeMillis

class FactionsRaidTimes : CoreJavaPlugin() {
    override val defaultConfig = false
    val mainConfig by lazy { YamlFile(MainConfigData, File(dataFolder, "config.yml")) }
    val manager by lazy { RaidTimesManager(this) }

    override fun onEnable() {
        logInfo("Successfully enabled ${description.name} in ${
            measureTimeMillis {
                registerConfig(mainConfig)
                manager
                registerCommand("factionsraidtimes", FactionsRaidTimesCmd(this), FactionsRaidTimesTab(this))
                registerListener(ExplosionListener(this))
                RaidTimesPAPI(this).register()
            }
        }ms!")
    }

    override fun reloadConfig() {
        super.reloadConfig()
        MainConfigData.verify(this)
    }
}