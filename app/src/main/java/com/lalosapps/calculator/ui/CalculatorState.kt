package com.lalosapps.calculator.ui

import com.lalosapps.calculator.core.CalculatorOperation

data class CalculatorState(
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null
)
