package com.gthio.datetimepickers.ui.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gthio.datetimepickers.ui.component.CustomDatePicker
import com.gthio.datetimepickers.ui.component.CustomTimePicker
import com.gthio.datetimepickers.ui.util.SizeCategory
import com.gthio.datetimepickers.ui.util.TimePickerFields

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRoute(
    viewModel: MainViewModel = viewModel(),
    sizeCategory: SizeCategory
) {
    val selectedTime by viewModel.selectedTime.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = when (sizeCategory == SizeCategory.Mobile) {
            true -> DisplayMode.Input
            false -> DisplayMode.Picker
        }
    )

    if (showDatePicker) {
        CustomDatePicker(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDatePicker = false
                        datePickerState
                            .selectedDateMillis
                            ?.let { date -> viewModel.dateSelected(date) }
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text(text = "Cancel")
                }
            },
            sizeCategory = sizeCategory,
            state = datePickerState,
        )
    }

    if (showTimePicker) {
        CustomTimePicker(
            onDismiss = { showTimePicker = false },
            confirmButton = { fields ->
                Button(
                    onClick = {
                        showTimePicker = false
                        viewModel.timeSelected(fields)
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showTimePicker = false }) {
                    Text(text = "Cancel")
                }
            },
            fields = selectedTime ?: TimePickerFields(0, 0),
            sizeCategory = sizeCategory,
            title = { Text(text = "Input Time") }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
    ) {
        Button(onClick = { showDatePicker = true }) {
            Text(text = "Show Date Picker")
        }
        Button(onClick = { showTimePicker = true }) {
            Text(text = "Show Time Picker")
        }
        Text(text = "Date: $selectedDate")
        Text(text = "Time: $selectedTime")
    }
}