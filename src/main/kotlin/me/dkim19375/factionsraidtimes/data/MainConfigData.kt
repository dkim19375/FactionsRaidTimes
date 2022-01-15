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

import me.dkim19375.dkimbukkitcore.function.logInfo
import me.dkim19375.factionsraidtimes.FactionsRaidTimes
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.configurationdata.CommentsConfiguration
import me.mattstudios.config.properties.Property
import java.util.logging.Level

object MainConfigData : SettingsHolder {
    @Path("raid-times")
    val RAID_TIMES = Property.create(RaidTimeData::class.java, mapOf(
        "morning" to RaidTimeData(),
        "afternoon" to RaidTimeData(
            start = "18:00",
            end = "21:00"
        )
    ))

    @Suppress("SpellCheckingInspection")
    @Path("warning-actions")
    val WARNING_ACTIONS = Property.create(RaidWarningActions::class.java, mapOf(
        "early" to RaidWarningActions(),
        "close" to RaidWarningActions(
            title = TitleData(
                title = "&6%minutes% minutes remaining!",
                subTitle = "&cBe sure to prepare!"
            ),
            actionBar = "&6Raiding enables in %minutes% minutes!",
            chat = listOf(
                "&cRaiding enables in &6%minutes% &cminutes!",
                "&6Not much time left!"
            ),
            times = listOf(15, 10, 5)
        ),
        "minute" to RaidWarningActions(
            title = TitleData(
                title = "&6%minutes% minute remaining!",
                subTitle = "&cEnabling very soon!"
            ),
            actionBar = "&6Raiding enables in %minutes% minutes!",
            chat = listOf(
                "&cRaiding enables in &6%minutes% &cminutes!",
                "&6Only one minute left!"
            ),
            times = listOf(1)
        ),
    ))

    @Suppress("SpellCheckingInspection")
    @Path("end-warning-actions")
    val END_WARNING_ACTIONS = Property.create(RaidWarningActions::class.java, mapOf(
        "close" to RaidWarningActions(
            title = TitleData(
                title = "&6%minutes% minutes remaining until the raid ends!",
                subTitle = ""
            ),
            actionBar = "&6Raiding disables in %minutes% minutes!",
            chat = listOf(
                "&aRaiding disables in &6%minutes% &cminutes!",
                "&6Not much time left!"
            ),
            times = listOf(15, 10, 5)
        ),
        "minute" to RaidWarningActions(
            title = TitleData(
                title = "&6%minutes% minute remaining!",
                subTitle = "&aDisabling very soon!"
            ),
            actionBar = "&6Raiding disables in %minutes% minute!",
            chat = listOf(
                "&aRaiding disables in &6%minutes% &cminute!",
                "&6Only one minute left!"
            ),
            times = listOf(1)
        ),
    ))

    @Path("start-action")
    val START_ACTION = Property.create(RaidStartEndAction())

    @Path("end-action")
    val END_ACTION = Property.create(RaidStartEndAction(
        chat = listOf(
            "&aRaiding is now disabled!",
            "&6This means that explosions are now disabled."
        )
    ))

    @Path("time-format")
    val TIME_FORMAT = Property.create(TimeFormatData())

    @Path("placeholder")
    val PLACEHOLDER = Property.create(PlaceholderData())

    override fun registerComments(conf: CommentsConfiguration) {
        conf.setComment("warning-actions.early.title.title",
            "Leave empty for no title (same applies to the subtitle, actionbar, ",
            "and for chat you can use YAML's empty list:",
            "chat: []"
        )
        conf.setComment("warning-actions.early.title.fade-in", "Set to -1 to use default times")
        conf.setComment("warning-actions.early.times", "The amount of MINUTES until the raid starts")
    }

    fun verify(plugin: FactionsRaidTimes) {
        var errors = 0
        val config = plugin.mainConfig
        fun tryRun(path: String, text: () -> Unit) {
            try {
                text()
            } catch (ex: ConfigurationException) {
                logInfo("Encountered an error while reading config value $path.start: ${ex.message}", Level.SEVERE)
                errors++
            }
        }
        config.get(RAID_TIMES).forEach { (key, data) ->
            val path = "raid-times.$key"
            tryRun("$path.start") {
                data.getStartTime()
            }
            tryRun("$path.end") {
                data.getEndTime()
            }
        }
        config.get(WARNING_ACTIONS).forEach { (key, data) ->
            val path = "warning-actions.$key"
            tryRun(path, data::verify)
        }
        config.get(END_WARNING_ACTIONS).forEach { (key, data) ->
            val path = "end-warning-actions.$key"
            tryRun(path, data::verify)
        }
        tryRun("start-action") {
            config.get(START_ACTION).verify()
        }
        tryRun("end-action") {
            config.get(END_ACTION).verify()
        }
        if (errors > 0) {
            throw ConfigurationException("$errors errors found while reading the config! (More details above)")
        }
    }
}