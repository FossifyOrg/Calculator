package org.fossify.math.helpers

import java.math.MathContext

const val DIGIT = "digit"
const val EQUALS = "equals"
const val PLUS = "plus"
const val MINUS = "minus"
const val MULTIPLY = "multiply"
const val DIVIDE = "divide"
const val PERCENT = "percent"
const val POWER = "power"
const val ROOT = "root"
const val DECIMAL = "decimal"
const val CLEAR = "clear"
const val RESET = "reset"

const val NAN = "NaN"
const val ZERO = "zero"
const val ONE = "one"
const val TWO = "two"
const val THREE = "three"
const val FOUR = "four"
const val FIVE = "five"
const val SIX = "six"
const val SEVEN = "seven"
const val EIGHT = "eight"
const val NINE = "nine"

// shared prefs
const val CONVERTER_UNITS_PREFIX = "converter_last_units"

// calculator state
const val RES = "res"
const val PREVIOUS_CALCULATION = "previousCalculation"
const val LAST_KEY = "lastKey"
const val LAST_OPERATION = "lastOperation"
const val BASE_VALUE = "baseValue"
const val SECOND_VALUE = "secondValue"
const val INPUT_DISPLAYED_FORMULA = "inputDisplayedFormula"
const val CALCULATOR_STATE = "calculatorState"

// converter state
const val TOP_UNIT = "top_unit"
const val BOTTOM_UNIT = "bottom_unit"
const val CONVERTER_VALUE = "converter_value"
const val CONVERTER_STATE = "converter_state"

// big decimal configuration
val MATH_CONTEXT: MathContext? = MathContext.DECIMAL128
