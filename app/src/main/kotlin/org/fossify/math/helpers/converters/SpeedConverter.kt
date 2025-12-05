package org.fossify.math.helpers.converters

import org.fossify.commons.helpers.HOUR_SECONDS
import org.fossify.math.R
import org.fossify.math.helpers.MATH_CONTEXT
import java.math.BigDecimal

/**
 * Base unit: meter per second.
 *
 * Main references:
 * - https://en.wikipedia.org/wiki/Metre_per_second
 * - https://en.wikipedia.org/wiki/Kilometres_per_hour
 * - https://en.wikipedia.org/wiki/Miles_per_hour
 * - https://en.wikipedia.org/wiki/Knot_(unit)
 * - https://en.wikipedia.org/wiki/Mach_number
 * - https://en.wikipedia.org/wiki/Speed_of_light
 */
object SpeedConverter : Converter {
    override val nameResId: Int = R.string.unit_speed
    override val imageResId: Int = R.drawable.ic_speed_vector
    override val key: String = "SpeedConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) :
        Converter.Unit(nameResId, symbolResId, factor, key) {
        data object MeterPerSecond : Unit(
            nameResId = R.string.unit_speed_meter_per_second,
            symbolResId = R.string.unit_speed_meter_per_second_symbol,
            factor = BigDecimal.ONE,
            key = "MeterPerSecond"
        )

        data object KilometerPerSecond : Unit(
            nameResId = R.string.unit_speed_kilometer_per_second,
            symbolResId = R.string.unit_speed_kilometer_per_second_symbol,
            factor = BigDecimal("1000"),
            key = "KilometerPerSecond"
        )

        data object KilometerPerHour : Unit(
            nameResId = R.string.unit_speed_kilometer_per_hour,
            symbolResId = R.string.unit_speed_kilometer_per_hour_symbol,
            factor = BigDecimal(1000).divide(BigDecimal(HOUR_SECONDS), MATH_CONTEXT),
            key = "KilometerPerHour"
        )

        data object MilePerHour : Unit(
            nameResId = R.string.unit_speed_mile_per_hour,
            symbolResId = R.string.unit_speed_mile_per_hour_symbol,
            factor = BigDecimal("0.44704"),
            key = "MilePerHour"
        )

        data object Knot : Unit(
            nameResId = R.string.unit_speed_knot,
            symbolResId = R.string.unit_speed_knot_symbol,
            factor = BigDecimal(1852).divide(BigDecimal(3600), MATH_CONTEXT),
            key = "Knot"
        )

        data object FootPerSecond : Unit(
            nameResId = R.string.unit_speed_foot_per_second,
            symbolResId = R.string.unit_speed_foot_per_second_symbol,
            factor = BigDecimal("0.3048"),
            key = "FootPerSecond"
        )

        data object Mach : Unit(
            nameResId = R.string.unit_speed_mach,
            symbolResId = R.string.unit_speed_mach_symbol,
            factor = BigDecimal("340.27"), // ISA: dry air, 15 °C, sea level
            key = "Mach"
        )

        data object SpeedOfLight : Unit(
            nameResId = R.string.unit_speed_speed_of_light,
            symbolResId = R.string.unit_speed_speed_of_light_symbol,
            factor = BigDecimal("299792458"),
            key = "SpeedOfLight"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.MeterPerSecond,
        Unit.KilometerPerSecond,
        Unit.KilometerPerHour,
        Unit.MilePerHour,
        Unit.Knot,
        Unit.FootPerSecond,
        Unit.Mach,
        Unit.SpeedOfLight
    )
    override val defaultTopUnit: Unit = Unit.KilometerPerHour
    override val defaultBottomUnit: Unit = Unit.MilePerHour
}
