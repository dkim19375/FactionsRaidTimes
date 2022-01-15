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

import java.util.*

data class DateData(
    val hour: Int,
    val minute: Int,
) {
    fun getTotalMinutes(): Int = (hour * 60) + minute

    companion object {
        fun fromString(str: String): DateData {
            val split = str.split(':')
            if (split.size < 2) {
                throw ConfigurationException("Time $this is invalid! (Format: hour:minute, hour = 0 -> 24)")
            }
            val hour = split[0].toIntOrNull()?.takeIf { it in 0..24 } ?: run {
                throw ConfigurationException("Hour of time $this is invalid!")
            }
            val minute = split[1].toIntOrNull() ?: run {
                throw ConfigurationException("Minute of time $this is invalid!")
            }
            return DateData(hour, minute)
        }

        fun fromMinutes(minutes: Int): DateData {
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            return DateData(hours, remainingMinutes)
        }

        fun getCurrentTime(): DateData = Calendar.getInstance().let { calendar ->
            DateData(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        }
    }

    operator fun compareTo(other: DateData): Int = getTotalMinutes().compareTo(other.getTotalMinutes())

    fun toSpecific(): SpecificTimeData = SpecificTimeData.fromSeconds(getTotalMinutes() * 60)
}