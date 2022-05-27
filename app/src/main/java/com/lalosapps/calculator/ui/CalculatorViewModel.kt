package com.lalosapps.calculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lalosapps.calculator.core.CalculatorAction
import com.lalosapps.calculator.core.CalculatorOperation

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
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
        if (state.operation == null && !state.number1.contains(".") && state.number1.isNotBlank()) {
            state = state.copy(number1 = state.number1 + ".")
            return
        }
        if (!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(number2 = state.number2 + ".")
        }
    }

    private fun performCalculation() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number1 == null || number2 == null) return
        val result = when (state.operation) {
            is CalculatorOperation.Add -> number1 + number2
            is CalculatorOperation.Subtract -> number1 - number2
            is CalculatorOperation.Multiply -> number1 * number2
            is CalculatorOperation.Divide -> if (number2 != 0.0) number1 / number2 else return
            null -> return
        }
        state = state.copy(
            number1 = result.toString(),
            number2 = "",
            operation = null
        )
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            state = state.copy(number1 = state.number1 + number)
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