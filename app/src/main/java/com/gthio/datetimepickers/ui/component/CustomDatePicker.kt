package com.gthio.datetimepickers.ui.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.gthio.datetimepickers.ui.util.SizeCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    onDismissRequest: () -> Unit,
    title: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    state: DatePickerState,
    shape: Shape = DatePickerDefaults.shape,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    tonalElevation: Dp = DatePickerDefaults.TonalElevation,
    sizeCategory: SizeCategory,
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        colors = colors,
        shape = shape,
        tonalElevation = tonalElevation,
        properties = DialogProperties(
            usePlatformDefaultWidth = sizeCategory != SizeCategory.Mobile,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            decorFitsSystemWindows = sizeCategory != SizeCategory.Mobile,
        ),
    ) {
        DatePicker(
            state = state,
            dateFormatter = DatePickerFormatter(),
            title = title,
            colors = colors,
            showModeToggle = sizeCategory != SizeCategory.Mobile,
        )
    }
}