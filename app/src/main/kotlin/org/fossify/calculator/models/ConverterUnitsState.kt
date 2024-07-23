package org.fossify.calculator.models

import org.fossify.calculator.helpers.converters.Converter

data class ConverterUnitsState(
    val topUnit: Converter.Unit,
    val bottomUnit: Converter.Unit,
)
