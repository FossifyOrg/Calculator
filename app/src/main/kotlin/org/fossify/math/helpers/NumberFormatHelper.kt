package org.fossify.math.helpers

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class NumberFormatHelper {

    companion object {
        private const val MAX_FRACTION_DIGITS = 15
    }

    val decimalSeparator: String = getDecimalSeparator()
    val groupingSeparator: String = getGroupingSeparator()

    fun bigDecimalToString(bd: BigDecimal): String {
        val symbols = DecimalFormatSymbols.getInstance()

        val formatter = DecimalFormat()
        formatter.maximumFractionDigits = MAX_FRACTION_DIGITS
        formatter.decimalFormatSymbols = symbols
        formatter.isGroupingUsed = true

        val result = formatter.format(bd)
        return if (result.contains(decimalSeparator)) {
            result.trimEnd('0').trimEnd(decimalSeparator.single())
        } else {
            result
        }
    }

    @Suppress("SwallowedException")
    fun addGroupingSeparators(str: String): String {
        return try {
            bigDecimalToString(removeGroupingSeparator(str).toBigDecimal())
        } catch (e: NumberFormatException) {
            // Return original string if it cannot be parsed as a valid number
            str
        }
    }

    fun removeGroupingSeparator(str: String): String {
        return str.replace(groupingSeparator, "").replace(decimalSeparator, ".")
    }
}

fun getDecimalSeparator(): String {
    return DecimalFormatSymbols.getInstance().decimalSeparator.toString()
}

fun getGroupingSeparator(): String {
    return DecimalFormatSymbols.getInstance().groupingSeparator.toString()
}
