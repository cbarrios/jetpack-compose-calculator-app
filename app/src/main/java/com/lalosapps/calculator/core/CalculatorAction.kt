package com.lalosapps.calculator.core

sealed class CalculatorAction {
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Decimal : CalculatorAction()
    object Calculate : CalculatorAction()
    data class Number(val number: Int) : CalculatorAction()
    data class Operation(val operation: CalculatorOperation) : CalculatorAction()
}
