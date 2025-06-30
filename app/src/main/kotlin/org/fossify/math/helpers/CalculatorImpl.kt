package org.fossify.math.helpers

import android.content.Context
import com.ezylang.evalex.Expression
import org.fossify.commons.extensions.showErrorToast
import org.fossify.commons.extensions.toast
import org.fossify.math.R
import org.fossify.math.models.History
import org.json.JSONObject
import org.json.JSONTokener
import java.math.BigDecimal

class CalculatorImpl(
    calculator: Calculator,
    private val context: Context,
    private var decimalSeparator: String = DOT,
    private var groupingSeparator: String = COMMA,
    calculatorState: String = ""
) {
    private var callback: Calculator? = calculator
    private var stateInstance = calculatorState
    private var currentResult = "0"
    private var previousCalculation = ""
    private var baseValue = BigDecimal.ZERO
    private var secondValue = BigDecimal.ZERO
    private var inputDisplayedFormula = "0"
    private var lastKey = ""
    private var lastOperation = ""
    private val operations = listOf("+", "-", "×", "÷", "^", "%", "√")
    private val operationsRegex = "[-+×÷^%√]".toPattern()
    private val numbersRegex = "[^0-9,.]".toRegex()
    private val formatter = NumberFormatHelper(
        decimalSeparator = decimalSeparator, groupingSeparator = groupingSeparator
    )

    init {
        if (stateInstance != "") {
            setFromSaveInstanceState(stateInstance)
        }
        showNewResult(currentResult)
        showNewFormula(previousCalculation)
    }

    private fun addDigit(number: Int) {
        if (inputDisplayedFormula == "0") {
            inputDisplayedFormula = ""
        }

        inputDisplayedFormula += number
        addThousandsDelimiter()
        showNewResult(inputDisplayedFormula)
    }

    private fun zeroClicked() {
        val valueToCheck = inputDisplayedFormula.trimStart('-').removeGroupSeparator()
        val value = valueToCheck.substring(valueToCheck.indexOfAny(operations) + 1)
        if (value != "0" || value.contains(decimalSeparator)) {
            addDigit(0)
        }
    }

    private fun decimalClicked() {
        val valueToCheck = inputDisplayedFormula.trimStart('-').replace(groupingSeparator, "")
        val value = valueToCheck.substring(valueToCheck.indexOfAny(operations) + 1)
        if (!value.contains(decimalSeparator)) {
            when {
                value == "0" && !valueToCheck.contains(operationsRegex.toRegex()) -> {
                    inputDisplayedFormula = "0$decimalSeparator"
                }

                value == "" -> inputDisplayedFormula += "0$decimalSeparator"
                else -> inputDisplayedFormula += decimalSeparator
            }
        }

        lastKey = DECIMAL
        showNewResult(inputDisplayedFormula)
    }

    private fun addThousandsDelimiter() {
        val valuesToCheck = numbersRegex.split(inputDisplayedFormula)
            .filter { it.trim().isNotEmpty() }
        valuesToCheck.forEach {
            var newString = formatter.addGroupingSeparators(it)

            // allow writing numbers like 0.003
            if (it.contains(decimalSeparator)) {
                val firstPart = newString.substringBefore(decimalSeparator)
                val lastPart = it.substringAfter(decimalSeparator)
                newString = "$firstPart$decimalSeparator$lastPart"
            }

            inputDisplayedFormula = inputDisplayedFormula.replace(it, newString)
        }
    }

    fun handleOperation(operation: String) {
        if (inputDisplayedFormula == "NaN") {
            inputDisplayedFormula = "0"
        }

        if (inputDisplayedFormula == "") {
            inputDisplayedFormula = "0"
        }

        if (operation == ROOT && inputDisplayedFormula == "0") {
            if (lastKey != DIGIT) {
                inputDisplayedFormula = "1√"
            }
        }

        val lastChar = inputDisplayedFormula.last().toString()
        if (lastChar == decimalSeparator) {
            inputDisplayedFormula = inputDisplayedFormula.dropLast(1)
        } else if (operations.contains(lastChar)) {
            inputDisplayedFormula = inputDisplayedFormula.dropLast(1)
            inputDisplayedFormula += getSign(operation)
        } else if (!inputDisplayedFormula.trimStart('-').contains(operationsRegex.toRegex())) {
            inputDisplayedFormula += getSign(operation)
        }

        if (lastKey == DIGIT || lastKey == DECIMAL) {
            if (lastOperation != "" && operation == PERCENT) {
                handlePercent()
            } else {
                // split to multiple lines just to see when does the crash happen
                secondValue = when (operation) {
                    PLUS -> getSecondValue()
                    MINUS -> getSecondValue()
                    MULTIPLY -> getSecondValue()
                    DIVIDE -> getSecondValue()
                    ROOT -> getSecondValue()
                    POWER -> getSecondValue()
                    PERCENT -> getSecondValue()
                    else -> getSecondValue()
                }

                calculateResult()

                if (!operations.contains(inputDisplayedFormula.last().toString())) {
                    if (!inputDisplayedFormula.contains("÷")) {
                        inputDisplayedFormula += getSign(operation)
                    }
                }
            }
        }

        if (getSecondValue() == BigDecimal.ZERO && inputDisplayedFormula.contains("÷")) {
            lastKey = DIVIDE
            lastOperation = DIVIDE
        } else {
            lastKey = operation
            lastOperation = operation
        }

        showNewResult(inputDisplayedFormula)
    }

    fun turnToNegative(): Boolean {
        if (inputDisplayedFormula.isEmpty()) {
            return false
        }

        if (!inputDisplayedFormula.trimStart('-').any { it.toString() in operations } &&
            try {
                inputDisplayedFormula.removeGroupSeparator().toBigDecimal() != BigDecimal.ZERO
            } catch (_: Exception) {
                false
            }) {
            inputDisplayedFormula = if (inputDisplayedFormula.first() == '-') {
                inputDisplayedFormula.substring(1)
            } else {
                "-$inputDisplayedFormula"
            }

            showNewResult(inputDisplayedFormula)
            return true
        }

        return false
    }

    // handle percents manually, it doesn't seem to be possible via EvalEx. "%" is used only for modulo there
    // handle cases like 10+200% here
    @Suppress("SwallowedException")
    private fun handlePercent() {
        val result = try {
            calculatePercentage(baseValue, getSecondValue(), lastOperation)
        } catch (_: ArithmeticException) {
            // Return zero if percentage calculation fails (e.g., division by zero)
            BigDecimal.ZERO
        }

        showNewFormula("${baseValue.format()}${getSign(lastOperation)}${getSecondValue().format()}%")
        inputDisplayedFormula = result.format()
        showNewResult(result.format())
        baseValue = result
    }

    fun handleEquals() {
        if (lastKey == EQUALS) {
            calculateResult()
        }

        if (lastKey != DIGIT && lastKey != DECIMAL) {
            return
        }

        secondValue = getSecondValue()
        calculateResult()
        if ((lastOperation == DIVIDE || lastOperation == PERCENT) && secondValue == BigDecimal.ZERO) {
            lastKey = DIGIT
            return
        }

        lastKey = EQUALS
    }

    private fun getSecondValue(): BigDecimal {
        val valueToCheck = inputDisplayedFormula.trimStart('-').removeGroupSeparator()

        var value = valueToCheck.substring(valueToCheck.indexOfAny(operations) + 1)
        if (value == "") {
            value = "0"
        }

        return try {
            value.toBigDecimal()
        } catch (e: NumberFormatException) {
            context.showErrorToast(e)
            BigDecimal.ZERO
        }
    }

    private fun calculateResult() {
        if (lastOperation == ROOT && inputDisplayedFormula.startsWith("√")) {
            baseValue = BigDecimal.ONE
        }

        if (lastKey != EQUALS) {
            val valueToCheck = inputDisplayedFormula.trimStart('-').removeGroupSeparator()

            if (inputDisplayedFormula.startsWith("√")) {
                val numberAfterRoot = valueToCheck.substring(1)
                try {
                    secondValue = numberAfterRoot.toBigDecimal()
                } catch (e: NumberFormatException) {
                    context.showErrorToast(e)
                    secondValue = BigDecimal.ZERO
                }
            } else {
                val parts = valueToCheck.split(operationsRegex).filter { it != "" }
                if (parts.isEmpty()) {
                    return
                }

                try {
                    baseValue = parts.first().toBigDecimal()
                } catch (e: NumberFormatException) {
                    context.showErrorToast(e)
                }

                if (inputDisplayedFormula.startsWith("-")) {
                    baseValue = baseValue.negate()
                }

                secondValue = parts.getOrNull(1)?.toBigDecimal() ?: secondValue
            }
        }

        if (lastOperation != "") {
            val sign = getSign(lastOperation)
            val formattedBaseValue = baseValue.format().removeGroupSeparator()
            val formatterSecondValue = secondValue.format().removeGroupSeparator()

            val expression = if (sign == "√") {
                "$formattedBaseValue*SQRT($formatterSecondValue)"
            } else {
                "$formattedBaseValue$sign$formatterSecondValue"
                    .replace("×", "*")
                    .replace("÷", "/")
            }

            try {
                if (sign == "÷" && secondValue == BigDecimal.ZERO) {
                    context.toast(R.string.formula_divide_by_zero_error)
                    return
                }

                // handle percents manually, it doesn't seem to be possible via EvalEx. 
                // "%" is used only for modulo there
                // handle cases like 10%200 here
                val result = if (sign == "%") {
                    val secondPercentValue = secondValue.divide(BigDecimal("100"), MATH_CONTEXT)
                    val second = secondPercentValue.format().removeGroupSeparator()
                    val percentExpression = "$formattedBaseValue*$second"
                    val expr = Expression(percentExpression)
                    expr.evaluate().numberValue
                } else {
                    val expr = Expression(expression)
                    val evaluationResult = expr.evaluate()
                    evaluationResult.numberValue
                }

                showNewResult(result.format())
                val newFormula = "${baseValue.format()}$sign${secondValue.format()}"
                HistoryHelper(context).insertOrUpdateHistoryEntry(
                    History(
                        id = null,
                        formula = newFormula,
                        result = result.format(),
                        timestamp = System.currentTimeMillis()
                    )
                )
                showNewFormula(newFormula)
                inputDisplayedFormula = result.format()
                baseValue = result
            } catch (_: Exception) {
                context.toast(org.fossify.commons.R.string.unknown_error_occurred)
            }
        }
    }

    private fun calculatePercentage(
        baseValue: BigDecimal,
        secondValue: BigDecimal,
        sign: String
    ): BigDecimal {
        if (secondValue == BigDecimal.ZERO) {
            throw ArithmeticException("Division by zero in percentage calculation")
        }

        return when (sign) {
            MULTIPLY -> {
                val partial = BigDecimal("100").divide(secondValue, MATH_CONTEXT)
                baseValue.divide(partial, MATH_CONTEXT)
            }

            DIVIDE -> {
                val partial = BigDecimal("100").divide(secondValue, MATH_CONTEXT)
                baseValue.multiply(partial, MATH_CONTEXT)
            }

            PLUS -> {
                val partial = baseValue.divide(
                    BigDecimal("100").divide(secondValue, MATH_CONTEXT), MATH_CONTEXT
                )
                baseValue.add(partial, MATH_CONTEXT)
            }

            MINUS -> {
                val partial = baseValue.divide(
                    BigDecimal("100").divide(secondValue, MATH_CONTEXT), MATH_CONTEXT
                )
                baseValue.subtract(partial, MATH_CONTEXT)
            }

            PERCENT -> {
                val partial = baseValue.remainder(secondValue, MATH_CONTEXT)
                    .divide(BigDecimal("100"), MATH_CONTEXT)
                partial
            }

            else -> baseValue.divide(
                BigDecimal("100").multiply(secondValue, MATH_CONTEXT), MATH_CONTEXT
            )
        }
    }

    private fun showNewResult(value: String) {
        currentResult = value
        callback!!.showNewResult(value, context)
    }

    private fun showNewFormula(value: String) {
        previousCalculation = value
        callback!!.showNewFormula(value, context)
    }

    fun handleClear() {
        val lastDeletedValue = inputDisplayedFormula.lastOrNull().toString()

        var newValue = inputDisplayedFormula.dropLast(1)
        if (newValue == "" || newValue == "0") {
            newValue = "0"
            lastKey = CLEAR
        } else {
            if (operations.contains(lastDeletedValue) || lastKey == EQUALS) {
                lastOperation = ""
            }
            val lastValue = newValue.last().toString()
            lastKey = when {
                operations.contains(lastValue) -> CLEAR
                lastValue == decimalSeparator -> DECIMAL
                else -> DIGIT
            }
        }

        newValue = newValue.trimEnd(groupingSeparator.single())
        inputDisplayedFormula = newValue
        addThousandsDelimiter()
        showNewResult(inputDisplayedFormula)
    }

    fun handleReset() {
        resetValues()
        showNewResult("0")
        showNewFormula("")
        inputDisplayedFormula = ""
    }

    private fun resetValues() {
        baseValue = BigDecimal.ZERO
        secondValue = BigDecimal.ZERO
        lastKey = ""
        lastOperation = ""
    }

    private fun getSign(lastOperation: String) = when (lastOperation) {
        MINUS -> "-"
        MULTIPLY -> "×"
        DIVIDE -> "÷"
        PERCENT -> "%"
        POWER -> "^"
        ROOT -> "√"
        else -> "+"
    }

    fun numpadClicked(id: Int) {
        if (inputDisplayedFormula == "NaN") {
            inputDisplayedFormula = ""
        }

        if (lastKey == EQUALS) {
            lastOperation = EQUALS
        }

        lastKey = DIGIT

        when (id) {
            R.id.btn_decimal -> decimalClicked()
            R.id.btn_0 -> zeroClicked()
            R.id.btn_1 -> addDigit(1)
            R.id.btn_2 -> addDigit(2)
            R.id.btn_3 -> addDigit(3)
            R.id.btn_4 -> addDigit(4)
            R.id.btn_5 -> addDigit(5)
            R.id.btn_6 -> addDigit(6)
            R.id.btn_7 -> addDigit(7)
            R.id.btn_8 -> addDigit(8)
            R.id.btn_9 -> addDigit(9)
        }
    }

    fun addNumberToFormula(number: String) {
        handleReset()
        inputDisplayedFormula = number
        addThousandsDelimiter()
        showNewResult(inputDisplayedFormula)
    }

    fun updateSeparators(decimalSeparator: String, groupingSeparator: String) {
        if (this.decimalSeparator != decimalSeparator || this.groupingSeparator != groupingSeparator) {
            this.decimalSeparator = decimalSeparator
            this.groupingSeparator = groupingSeparator
            formatter.decimalSeparator = decimalSeparator
            formatter.groupingSeparator = groupingSeparator
            // future: maybe update the formulas with new separators instead of resetting the whole thing
            handleReset()
        }
    }

    private fun BigDecimal.format() = formatter.bigDecimalToString(this)

    private fun String.removeGroupSeparator() = formatter.removeGroupingSeparator(this)

    fun getCalculatorStateJson(): JSONObject {
        val jsonObj = JSONObject()
        jsonObj.put(RES, currentResult)
        jsonObj.put(PREVIOUS_CALCULATION, previousCalculation)
        jsonObj.put(LAST_KEY, lastKey)
        jsonObj.put(LAST_OPERATION, lastOperation)
        jsonObj.put(BASE_VALUE, baseValue.toString())
        jsonObj.put(SECOND_VALUE, secondValue.toString())
        jsonObj.put(INPUT_DISPLAYED_FORMULA, inputDisplayedFormula)
        return jsonObj
    }

    private fun setFromSaveInstanceState(json: String) {
        val jsonObject = JSONTokener(json).nextValue() as JSONObject
        currentResult = jsonObject.getString(RES)
        previousCalculation = jsonObject.getString(PREVIOUS_CALCULATION)
        lastKey = jsonObject.getString(LAST_KEY)
        lastOperation = jsonObject.getString(LAST_OPERATION)
        baseValue = try {
            BigDecimal(jsonObject.getString(BASE_VALUE))
        } catch (_: Exception) {
            BigDecimal.ZERO
        }
        secondValue = try {
            BigDecimal(jsonObject.getString(SECOND_VALUE))
        } catch (_: Exception) {
            BigDecimal.ZERO
        }
        inputDisplayedFormula = jsonObject.getString(INPUT_DISPLAYED_FORMULA)
    }
}
