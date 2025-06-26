package org.fossify.math.helpers

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class NumberFormatHelper(
    var decimalSeparator: String = DOT,
    var groupingSeparator: String = COMMA
) {

    fun bigDecimalToString(bd: BigDecimal): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.decimalSeparator = decimalSeparator.single()
        symbols.groupingSeparator = groupingSeparator.single()

        val formatter = DecimalFormat()
        formatter.maximumFractionDigits = 15
        formatter.decimalFormatSymbols = symbols
        formatter.isGroupingUsed = true
        
        val result = formatter.format(bd)
        return if (result.contains(decimalSeparator)) {
            result.trimEnd('0').trimEnd(decimalSeparator.single())
        } else {
            result
        }
    }

    fun addGroupingSeparators(str: String): String {
        return try {
            bigDecimalToString(removeGroupingSeparator(str).toBigDecimal())
        } catch (e: Exception) {
            str
        }
    }

    fun removeGroupingSeparator(str: String): String {
        return str.replace(groupingSeparator, "").replace(decimalSeparator, DOT)
    }
}

fun getDecimalSeparator(): String {
    return DecimalFormatSymbols.getInstance().decimalSeparator.toString()
}
