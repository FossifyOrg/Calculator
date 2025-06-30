package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal

object AreaConverter : Converter {
    override val nameResId: Int = R.string.unit_area
    override val imageResId: Int = R.drawable.ic_box_vector
    override val key: String = "AreaConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) :
        Converter.Unit(nameResId, symbolResId, factor, key) {
        data object SquareKilometer : Unit(
            nameResId = R.string.unit_area_square_kilometer,
            symbolResId = R.string.unit_area_square_kilometer_symbol,
            factor = BigDecimal("1000000"),
            key = "SquareKilometer"
        )

        data object SquareMeter : Unit(
            nameResId = R.string.unit_area_square_meter,
            symbolResId = R.string.unit_area_square_meter_symbol,
            factor = BigDecimal.ONE,
            key = "SquareMeter"
        )

        data object SquareCentimeter : Unit(
            nameResId = R.string.unit_area_square_centimeter,
            symbolResId = R.string.unit_area_square_centimeter_symbol,
            factor = BigDecimal("0.0001"),
            key = "SquareCentimeter"
        )

        data object SquareMillimeter : Unit(
            nameResId = R.string.unit_area_square_millimeter,
            symbolResId = R.string.unit_area_square_millimeter_symbol,
            factor = BigDecimal("0.000001"),
            key = "SquareMillimeter"
        )

        data object SquareMile : Unit(
            nameResId = R.string.unit_area_square_mile,
            symbolResId = R.string.unit_area_square_mile_symbol,
            factor = BigDecimal("2589988.110336"),
            key = "SquareMile"
        )

        data object SquareYard : Unit(
            nameResId = R.string.unit_area_square_yard,
            symbolResId = R.string.unit_area_square_yard_symbol,
            factor = BigDecimal("0.83612736"),
            key = "SquareYard"
        )

        data object SquareFoot : Unit(
            nameResId = R.string.unit_area_square_foot,
            symbolResId = R.string.unit_area_square_foot_symbol,
            factor = BigDecimal("0.09290304"),
            key = "SquareFoot"
        )

        data object SquareInch : Unit(
            nameResId = R.string.unit_area_square_inch,
            symbolResId = R.string.unit_area_square_inch_symbol,
            factor = BigDecimal("0.00064516"),
            key = "SquareInch"
        )

        data object Acre : Unit(
            nameResId = R.string.unit_area_acre,
            symbolResId = R.string.unit_area_acre_symbol,
            factor = BigDecimal("4046.8564224"),
            key = "Acre"
        )

        data object Hectare : Unit(
            nameResId = R.string.unit_area_hectare,
            symbolResId = R.string.unit_area_hectare_symbol,
            factor = BigDecimal("10000"),
            key = "Hectare"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.SquareKilometer,
        Unit.SquareMeter,
        Unit.SquareCentimeter,
        Unit.SquareMillimeter,
        Unit.SquareMile,
        Unit.SquareYard,
        Unit.SquareFoot,
        Unit.SquareInch,
        Unit.Acre,
        Unit.Hectare,
    )
    override val defaultTopUnit: Unit = Unit.SquareKilometer
    override val defaultBottomUnit: Unit = Unit.SquareMeter
}
