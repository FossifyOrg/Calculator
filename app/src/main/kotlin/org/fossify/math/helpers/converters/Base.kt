package org.fossify.math.helpers.converters

import android.content.Context
import org.fossify.math.R
import org.fossify.math.helpers.MATH_CONTEXT
import java.math.BigDecimal

interface Converter {
    companion object {
        val ALL = listOf(
            LengthConverter,
            AreaConverter,
            VolumeConverter,
            MassConverter,
            TemperatureConverter,
            TimeConverter,
            SpeedConverter,
            PressureConverter,
            EnergyConverter
        )
    }

    val nameResId: Int
    val imageResId: Int
    val units: List<Unit>
    val defaultTopUnit: Unit
    val defaultBottomUnit: Unit

    val key: String

    fun convert(from: ValueWithUnit<Unit>, to: Unit): ValueWithUnit<Unit> {
        return ValueWithUnit(to.fromBase(from.unit.toBase(from.value)), to)
    }

    open class Unit(
        val nameResId: Int,
        val symbolResId: Int,
        val factor: BigDecimal,
        val key: String
    ) {
        open fun toBase(value: BigDecimal): BigDecimal = value.multiply(factor, MATH_CONTEXT)

        open fun fromBase(value: BigDecimal): BigDecimal = value.divide(factor, MATH_CONTEXT)

        fun withValue(value: BigDecimal) = ValueWithUnit(value, this)

        fun getNameWithSymbol(context: Context) = context.getString(
            R.string.unit_name_with_symbol_format,
            context.getString(nameResId),
            context.getString(symbolResId)
        )
    }
}

data class ValueWithUnit<T : Converter.Unit>(val value: BigDecimal, val unit: T)
