package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal
import java.math.MathContext

object TemperatureConverter : Converter {
    override val nameResId: Int = R.string.unit_temperature
    override val imageResId: Int = R.drawable.ic_thermostat_vector
    override val key: String = "TemperatureConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) : Converter.Unit(nameResId, symbolResId, factor, key) {

        data object Celsius : Unit(
            nameResId = R.string.unit_temperature_celsius,
            symbolResId = R.string.unit_temperature_celsius_symbol,
            factor = BigDecimal.ONE,
            key = "Celsius"
        ) {
            private val KELVIN_OFFSET = BigDecimal("273.15")

            override fun toBase(value: BigDecimal): BigDecimal = value.add(KELVIN_OFFSET, mathContext)
            override fun fromBase(value: BigDecimal): BigDecimal = value.subtract(KELVIN_OFFSET, mathContext)
        }

        data object Fahrenheit : Unit(
            nameResId = R.string.unit_temperature_fahrenheit,
            symbolResId = R.string.unit_temperature_fahrenheit_symbol,
            factor = BigDecimal("9").divide(BigDecimal("5"), MathContext.DECIMAL128),
            key = "Fahrenheit"
        ) {
            private val CELSIUS_OFFSET = BigDecimal("32")

            override fun toBase(value: BigDecimal): BigDecimal = 
                Celsius.toBase(value.subtract(CELSIUS_OFFSET, mathContext).divide(factor, mathContext))

            override fun fromBase(value: BigDecimal): BigDecimal = 
                Celsius.fromBase(value).multiply(factor, mathContext).add(CELSIUS_OFFSET, mathContext)
        }

        data object Rankine : Unit(
            nameResId = R.string.unit_temperature_rankine,
            symbolResId = R.string.unit_temperature_rankine_symbol,
            factor = BigDecimal("5").divide(BigDecimal("9"), MathContext.DECIMAL128),
            key = "Rankine"
        )

        data object Kelvin : Unit(
            nameResId = R.string.unit_temperature_kelvin,
            symbolResId = R.string.unit_temperature_kelvin_symbol,
            factor = BigDecimal.ONE,
            key = "Kelvin"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.Celsius,
        Unit.Fahrenheit,
        Unit.Rankine,
        Unit.Kelvin,
    )

    override val defaultTopUnit: Unit = Unit.Celsius
    override val defaultBottomUnit: Unit = Unit.Kelvin
}
