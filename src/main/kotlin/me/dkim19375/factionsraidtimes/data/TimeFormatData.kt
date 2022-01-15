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

data class TimeFormatData(
    var hour: String = "h",
    var minute: String = "m",
    var second: String = "s",
    var separator: String = " "
) {
    fun formatTime(time: SpecificTimeData): String {
        val list = mutableListOf<String>()
        if (time.hours > 0) {
            list.add("${time.hours}$hour")
        }
        if (time.minutes > 0) {
            list.add("${time.minutes}$minute")
        }
        if (time.seconds > 0) {
            list.add("${time.seconds}$second")
        }
        return list.joinToString(separator)
    }
}