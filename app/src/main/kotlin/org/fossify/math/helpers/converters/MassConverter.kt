package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal

object MassConverter : Converter {
    override val nameResId: Int = R.string.unit_mass
    override val imageResId: Int = R.drawable.ic_scale_vector
    override val key: String = "MassConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) : Converter.Unit(nameResId, symbolResId, factor, key) {
        data object Gram : Unit(
            nameResId = R.string.unit_mass_gram,
            symbolResId = R.string.unit_mass_gram_symbol,
            factor = BigDecimal("0.001"),
            key = "Gram"
        )

        data object Kilogram : Unit(
            nameResId = R.string.unit_mass_kilogram,
            symbolResId = R.string.unit_mass_kilogram_symbol,
            factor = BigDecimal.ONE,
            key = "Kilogram"
        )

        data object Milligram : Unit(
            nameResId = R.string.unit_mass_milligram,
            symbolResId = R.string.unit_mass_milligram_symbol,
            factor = BigDecimal("0.000001"),
            key = "Milligram"
        )

        data object Microgram : Unit(
            nameResId = R.string.unit_mass_microgram,
            symbolResId = R.string.unit_mass_microgram_symbol,
            factor = BigDecimal("0.000000001"),
            key = "Microgram"
        )

        data object Tonne : Unit(
            nameResId = R.string.unit_mass_tonne,
            symbolResId = R.string.unit_mass_tonne_symbol,
            factor = BigDecimal("1000"),
            key = "Tonne"
        )

        data object Pound : Unit(
            nameResId = R.string.unit_mass_pound,
            symbolResId = R.string.unit_mass_pound_symbol,
            factor = BigDecimal("0.45359237"),
            key = "Pound"
        )

        data object Ounce : Unit(
            nameResId = R.string.unit_mass_ounce,
            symbolResId = R.string.unit_mass_ounce_symbol,
            factor = BigDecimal("0.028349523125"),
            key = "Ounce"
        )

        data object Grain : Unit(
            nameResId = R.string.unit_mass_grain,
            symbolResId = R.string.unit_mass_grain_symbol,
            factor = BigDecimal("0.00006479891"),
            key = "Grain"
        )

        data object Dram : Unit(
            nameResId = R.string.unit_mass_dram,
            symbolResId = R.string.unit_mass_dram_symbol,
            factor = BigDecimal("0.0017718451953125"),
            key = "Dram"
        )

        data object Stone : Unit(
            nameResId = R.string.unit_mass_stone,
            symbolResId = R.string.unit_mass_stone_symbol,
            factor = BigDecimal("6.35029318"),
            key = "Stone"
        )

        data object LongTon : Unit(
            nameResId = R.string.unit_mass_long_ton,
            symbolResId = R.string.unit_mass_long_ton_symbol,
            factor = BigDecimal("1016.0469088"),
            key = "LongTon"
        )

        data object ShortTon : Unit(
            nameResId = R.string.unit_mass_short_ton,
            symbolResId = R.string.unit_mass_short_ton_symbol,
            factor = BigDecimal("907.18474"),
            key = "ShortTon"
        )

        data object Carat : Unit(
            nameResId = R.string.unit_mass_carat,
            symbolResId = R.string.unit_mass_carat_symbol,
            factor = BigDecimal("0.0002051965483"),
            key = "Carat"
        )

        data object CaratMetric : Unit(
            nameResId = R.string.unit_mass_carat_metric,
            symbolResId = R.string.unit_mass_carat_metric_symbol,
            factor = BigDecimal("0.0002"),
            key = "CaratMetric"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.Gram,
        Unit.Kilogram,
        Unit.Milligram,
        Unit.Microgram,
        Unit.Tonne,
        Unit.Pound,
        Unit.Ounce,
        Unit.Grain,
        Unit.Dram,
        Unit.Stone,
        Unit.LongTon,
        Unit.ShortTon,
        Unit.Carat,
        Unit.CaratMetric,
    )

    override val defaultTopUnit: Unit = Unit.Pound
    override val defaultBottomUnit: Unit = Unit.Kilogram
}
