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

data class SpecificTimeData(
    var hours: Int,
    var minutes: Int,
    var seconds: Int
) {
    fun getTotalSeconds(): Int = (hours * 3600) + (minutes * 60) + seconds

    companion object {
        fun fromSeconds(seconds: Int): SpecificTimeData {
            val hours = seconds / 3600
            var remainingSeconds = seconds % 3600
            val minutes = remainingSeconds / 60
            remainingSeconds %= 60
            return SpecificTimeData(hours, minutes, remainingSeconds)
        }

        fun getCurrentTime(): SpecificTimeData = Calendar.getInstance().let { calendar ->
            SpecificTimeData(
                hours = calendar.get(Calendar.HOUR_OF_DAY),
                minutes = calendar.get(Calendar.MINUTE),
                seconds = calendar.get(Calendar.SECOND)
            )
        }
    }

    operator fun compareTo(other: SpecificTimeData): Int = getTotalSeconds().compareTo(other.getTotalSeconds())
}
