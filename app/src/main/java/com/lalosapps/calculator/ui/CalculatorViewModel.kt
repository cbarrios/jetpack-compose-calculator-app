package com.lalosapps.calculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lalosapps.calculator.core.CalculatorAction
import com.lalosapps.calculator.core.CalculatorOperation
import java.math.BigDecimal
import java.math.MathContext

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    var isCalculation by mutableStateOf(false)
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Clear -> clearState()
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation)
        }
    }

    fun onCalculationDone() {
        isCalculation = false
    }

    private fun clearState() {
        state = CalculatorState()
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }

    private fun enterDecimal() {
        if (state.operation == null && !state.number1.contains(".")) {
            state = if (state.number1.isBlank()) {
                state.copy(number1 = "0.")
            } else {
                state.copy(number1 = state.number1 + ".")
            }
        }
        if (state.operation != null && !state.number2.contains(".")) {
            state = if (state.number2.isBlank()) {
                state.copy(number2 = "0.")
            } else {
                state.copy(number2 = state.number2 + ".")
            }
        }
    }

    private fun performCalculation() {
        val number1 = state.number1.toBigDecimalOrNull()
        val number2 = state.number2.toBigDecimalOrNull()
        if (number1 == null || number2 == null) return
        val result = when (state.operation) {
            is CalculatorOperation.Add -> number1.add(number2)
            is CalculatorOperation.Subtract -> number1.subtract(number2)
            is CalculatorOperation.Multiply -> number1.multiply(number2)
            is CalculatorOperation.Divide -> if (number2.compareTo(BigDecimal.ZERO) != 0) number1.divide(
                number2,
                MathContext.DECIMAL64
            ) else return
            null -> return
        }
        isCalculation = true
        val stringResult = if (result.compareTo(BigDecimal.ZERO) == 0) "0" else result.toString()
        state = state.copy(
            number1 = stringResult,
            number2 = "",
            operation = null
        )
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length == 1 && state.number1[0] == '0') {
                state = state.copy(number1 = state.number1.dropLast(1) + number)
                return
            }
            state = state.copy(number1 = state.number1 + number)
            return
        }
        if (state.number2.length == 1 && state.number2[0] == '0') {
            state = state.copy(number2 = state.number2.dropLast(1) + number)
            return
        }
        state = state.copy(number2 = state.number2 + number)
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }
}