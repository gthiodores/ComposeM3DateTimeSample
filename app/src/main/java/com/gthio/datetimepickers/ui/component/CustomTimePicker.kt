package com.gthio.datetimepickers.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import com.gthio.datetimepickers.ui.util.SizeCategory
import com.gthio.datetimepickers.ui.util.TimePickerFields

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onDismiss: () -> Unit,
    confirmButton: @Composable (TimePickerFields) -> Unit,
    dismissButton: @Composable () -> Unit,
    fields: TimePickerFields,
    colors: TimePickerColors = TimePickerDefaults.colors(),
    sizeCategory: SizeCategory,
) {
    val isCompact by rememberUpdatedState(sizeCategory == SizeCategory.Mobile)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
        ),
    ) {
        when (isCompact) {
            true -> TimePickerTextField(
                fields = fields,
                dialogShape = AlertDialogDefaults.shape,
                confirmButton = confirmButton,
                dismissButton = dismissButton,
            )
            false -> TimePickerDialog(
                dialogShape = AlertDialogDefaults.shape,
                confirmButton = confirmButton,
                dismissButton = dismissButton,
                fields = fields,
                colors = colors
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    dialogShape: Shape,
    confirmButton: @Composable (TimePickerFields) -> Unit,
    dismissButton: @Composable () -> Unit,
    fields: TimePickerFields,
    colors: TimePickerColors,
) {
    val state = rememberTimePickerState(
        is24Hour = true,
        initialHour = fields.hour,
        initialMinute = fields.minute,
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = dialogShape)
            .clip(dialogShape)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimePicker(state = state, colors = colors)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            dismissButton()
            confirmButton(TimePickerFields(hour = state.hour, minute = state.minute))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerTextField(
    confirmButton: @Composable (TimePickerFields) -> Unit,
    dismissButton: @Composable () -> Unit,
    fields: TimePickerFields,
    dialogShape: Shape,
) {
    fun generateInitialValue(): String {
        if (fields.hour == 0 && fields.minute == 0) return ""

        val formattedHour = if (fields.hour < 10) "0${fields.hour}" else fields.hour
        val formattedMinute = if (fields.minute < 10) "0${fields.minute}" else fields.minute

        return "$formattedHour$formattedMinute"
    }

    var customState by rememberSaveable { mutableStateOf(generateInitialValue()) }

    fun evaluateHourMinuteDigits(hourDigit: Int, minuteDigit: Int): Boolean {
        if (hourDigit >= 24) return false

        if (minuteDigit >= 60) return false

        return true
    }

    fun evaluateNewValue(value: String): Boolean {
        if (!value.isDigitsOnly()) return false

        if (value.length > 4) return false

        if (value.contains(" ")) return false

        return when (value.length) {
            in 0..2 -> (value.toIntOrNull() ?: 0) < 24
            else -> evaluateHourMinuteDigits(
                hourDigit = value.slice(0..1).toInt(),
                minuteDigit = value.slice(2 until value.length).toInt(),
            )
        }
    }

    fun parseState(): TimePickerFields {
        if (customState.isBlank()) return TimePickerFields(hour = 0, minute = 0)

        if (customState.length < 3) return TimePickerFields(hour = customState.toInt(), minute = 0)

        return TimePickerFields(
            hour = customState.slice(0..1).toInt(),
            minute = customState.slice(2 until customState.length).toInt(),
        )
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = dialogShape)
            .clip(dialogShape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Input Time", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = customState,
            onValueChange = { value -> if (evaluateNewValue(value)) customState = value },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            label = { Text(text = "Time") },
            placeholder = { Text(text = "HH:MM") },
            visualTransformation = TimeVisualTransformation(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            dismissButton()
            confirmButton(parseState())
        }
    }
}

class TimeOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        if (offset < 3) return offset

        return offset + 1
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset < 3) return offset

        return offset - 1
    }

}

class TimeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val updatedText = when (text.length) {
            in 0..2 -> text
            else -> "${text.slice(0..1)}:${text.slice(2 until text.length)}"
        }

        return TransformedText(
            text = AnnotatedString(updatedText.toString()),
            offsetMapping = TimeOffsetMapping(),
        )
    }
}