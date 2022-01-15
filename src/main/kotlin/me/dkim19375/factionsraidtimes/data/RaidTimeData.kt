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

data class RaidTimeData(
    var start: String = "6:00",
    var end: String = "9:00",
) {
    fun getStartTime(): DateData = DateData.fromString(start)

    fun getEndTime(): DateData = DateData.fromString(end)

    fun contains(time: DateData): Boolean {
        val start = getStartTime()
        val end = getEndTime()
        return when {
            start == end -> time == start
            start < end -> time.getTotalMinutes() in start.getTotalMinutes()..end.getTotalMinutes()
            else -> time.getTotalMinutes() !in end.getTotalMinutes()..start.getTotalMinutes()
        }
    }

    fun contains(time: SpecificTimeData): Boolean {
        val start = getStartTime().toSpecific()
        val end = getEndTime().toSpecific()
        return when {
            start == end -> time == start
            start < end -> time.getTotalSeconds() in start.getTotalSeconds()..end.getTotalSeconds()
            else -> time.getTotalSeconds() !in end.getTotalSeconds()..start.getTotalSeconds()
        }
    }

    fun distance(time: DateData): DateData {
        if (contains(time)) {
            return DateData.fromMinutes(0)
        }
        val total = time.getTotalMinutes()
        val start = getStartTime().getTotalMinutes()
        if (total < start) {
            return DateData.fromMinutes(start - total)
        }
        return DateData.fromMinutes((1440 - total) + start)
    }

    fun distanceEnd(time: DateData): DateData {
        if (!contains(time)) {
            throw IllegalStateException("Cannot get end when not contained!")
        }
        val total = time.getTotalMinutes()
        val end = getEndTime().getTotalMinutes()
        if (total < end) {
            return DateData.fromMinutes(end - total)
        }
        return DateData.fromMinutes((1440 - total) + end)
    }

    fun distance(time: SpecificTimeData): SpecificTimeData {
        if (contains(time)) {
            return SpecificTimeData.fromSeconds(0)
        }
        val total = time.getTotalSeconds()
        val start = getStartTime().toSpecific().getTotalSeconds()
        if (total < start) {
            return SpecificTimeData.fromSeconds(start - total)
        }
        return SpecificTimeData.fromSeconds((86400 - total) + start)
    }

    fun distanceEnd(time: SpecificTimeData): SpecificTimeData {
        val end = getEndTime().toSpecific().getTotalSeconds()
        if (!contains(time)) {
            throw IllegalStateException("Cannot get end when not contained!")
        }
        val total = time.getTotalSeconds()
        if (total < end) {
            return SpecificTimeData.fromSeconds(end - total)
        }
        return SpecificTimeData.fromSeconds((86400 - total) + end)
    }
}