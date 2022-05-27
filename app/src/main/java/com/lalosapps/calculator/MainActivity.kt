package com.lalosapps.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lalosapps.calculator.ui.Calculator
import com.lalosapps.calculator.ui.CalculatorViewModel
import com.lalosapps.calculator.ui.theme.CalculatorTheme
import com.lalosapps.calculator.ui.theme.MediumGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val viewModel = viewModel<CalculatorViewModel>()
                val state = viewModel.state
                val scrollState = rememberScrollState()
                LaunchedEffect(state) {
                    if (viewModel.isCalculation) {
                        viewModel.onCalculationDone()
                        scrollState.animateScrollTo(0)
                    } else {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
                Calculator(
                    state = state,
                    scrollState = scrollState,
                    onAction = viewModel::onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MediumGray)
                        .padding(16.dp)
                )
            }
        }
    }
}