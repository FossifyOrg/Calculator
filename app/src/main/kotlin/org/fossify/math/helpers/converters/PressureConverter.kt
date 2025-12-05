package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal

/**
 * Base unit: pascal.
 *
 * Main references:
 * - https://en.wikipedia.org/wiki/Pascal_(unit)
 * - https://en.wikipedia.org/wiki/Bar_(unit)
 * - https://en.wikipedia.org/wiki/Atmosphere_(unit)
 * - https://en.wikipedia.org/wiki/Pounds_per_square_inch
 * - https://en.wikipedia.org/wiki/Torr
 * - https://en.wikipedia.org/wiki/Millimetre_of_mercury
 */
object PressureConverter : Converter {
    override val nameResId: Int = R.string.unit_pressure
    override val imageResId: Int = R.drawable.ic_pressure_vector
    override val key: String = "PressureConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) :
        Converter.Unit(nameResId, symbolResId, factor, key) {
        data object Pascal : Unit(
            nameResId = R.string.unit_pressure_pascal,
            symbolResId = R.string.unit_pressure_pascal_symbol,
            factor = BigDecimal.ONE,
            key = "Pascal"
        )

        data object Kilopascal : Unit(
            nameResId = R.string.unit_pressure_kilopascal,
            symbolResId = R.string.unit_pressure_kilopascal_symbol,
            factor = BigDecimal("1000"),
            key = "Kilopascal"
        )

        data object Megapascal : Unit(
            nameResId = R.string.unit_pressure_megapascal,
            symbolResId = R.string.unit_pressure_megapascal_symbol,
            factor = BigDecimal("1000000"),
            key = "Megapascal"
        )

        data object Bar : Unit(
            nameResId = R.string.unit_pressure_bar,
            symbolResId = R.string.unit_pressure_bar_symbol,
            factor = BigDecimal("100000"),
            key = "Bar"
        )

        data object Millibar : Unit(
            nameResId = R.string.unit_pressure_millibar,
            symbolResId = R.string.unit_pressure_millibar_symbol,
            factor = BigDecimal("100"),
            key = "Millibar"
        )

        data object Atmosphere : Unit(
            nameResId = R.string.unit_pressure_atmosphere,
            symbolResId = R.string.unit_pressure_atmosphere_symbol,
            factor = BigDecimal("101325"),
            key = "Atmosphere"
        )

        data object Psi : Unit(
            nameResId = R.string.unit_pressure_psi,
            symbolResId = R.string.unit_pressure_psi_symbol,
            factor = BigDecimal("6894.757293168"),
            key = "Psi"
        )

        data object Torr : Unit(
            nameResId = R.string.unit_pressure_torr,
            symbolResId = R.string.unit_pressure_torr_symbol,
            factor = BigDecimal("133.32236842105"),
            key = "Torr"
        )

        data object MillimeterOfMercury : Unit(
            nameResId = R.string.unit_pressure_mmhg,
            symbolResId = R.string.unit_pressure_mmhg_symbol,
            factor = BigDecimal("133.322387415"),
            key = "MillimeterOfMercury"
        )

        data object InchOfMercury : Unit(
            nameResId = R.string.unit_pressure_inhg,
            symbolResId = R.string.unit_pressure_inhg_symbol,
            factor = BigDecimal("3386.389"),
            key = "InchOfMercury"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.Pascal,
        Unit.Kilopascal,
        Unit.Megapascal,
        Unit.Bar,
        Unit.Millibar,
        Unit.Atmosphere,
        Unit.Psi,
        Unit.Torr,
        Unit.MillimeterOfMercury,
        Unit.InchOfMercury
    )
    override val defaultTopUnit: Unit = Unit.Bar
    override val defaultBottomUnit: Unit = Unit.Psi
}
