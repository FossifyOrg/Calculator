package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal

object TimeConverter : Converter {
    override val nameResId: Int = R.string.unit_time
    override val imageResId: Int = org.fossify.commons.R.drawable.ic_clock_vector
    override val key: String = "TimeConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) :
        Converter.Unit(nameResId, symbolResId, factor, key) {
        companion object {
            private val MINUTE = BigDecimal("60")
            private val HOUR = MINUTE.multiply(BigDecimal("60"))
            private val DAY = HOUR.multiply(BigDecimal("24"))
            private val GREGORIAN_YEAR = DAY.multiply(BigDecimal("365.2425"))
        }

        data object Hour : Unit(
            nameResId = R.string.unit_time_hour,
            symbolResId = R.string.unit_time_hour_symbol,
            factor = HOUR,
            key = "Hour"
        )

        data object Minute : Unit(
            nameResId = R.string.unit_time_minute,
            symbolResId = R.string.unit_time_minute_symbol,
            factor = MINUTE,
            key = "Minute"
        )

        data object Second : Unit(
            nameResId = R.string.unit_time_second,
            symbolResId = R.string.unit_time_second_symbol,
            factor = BigDecimal.ONE,
            key = "Second"
        )

        data object Millisecond : Unit(
            nameResId = R.string.unit_time_millisecond,
            symbolResId = R.string.unit_time_millisecond_symbol,
            factor = BigDecimal("0.001"),
            key = "Millisecond"
        )

        data object Day : Unit(
            nameResId = R.string.unit_time_day,
            symbolResId = R.string.unit_time_day_symbol,
            factor = DAY,
            key = "Day"
        )

        data object Week : Unit(
            nameResId = R.string.unit_time_week,
            symbolResId = R.string.unit_time_week_symbol,
            factor = BigDecimal("604800"),
            key = "Week"
        )

        data object Year : Unit(
            nameResId = R.string.unit_time_year,
            symbolResId = R.string.unit_time_year_symbol,
            factor = GREGORIAN_YEAR,
            key = "Year"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.Hour,
        Unit.Minute,
        Unit.Second,
        Unit.Millisecond,
        Unit.Day,
        Unit.Week,
        Unit.Year,
    )

    override val defaultTopUnit: Unit = Unit.Hour
    override val defaultBottomUnit: Unit = Unit.Second
}
