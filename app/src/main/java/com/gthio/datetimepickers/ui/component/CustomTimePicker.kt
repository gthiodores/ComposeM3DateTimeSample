package com.gthio.datetimepickers.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gthio.datetimepickers.ui.util.SizeCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onDismiss: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    state: TimePickerState,
    colors: TimePickerColors = TimePickerDefaults.colors(),
    sizeCategory: SizeCategory,
) {
    val isCompact by rememberUpdatedState(sizeCategory == SizeCategory.Mobile)

    val sizeModifier by rememberUpdatedState(
        when (isCompact) {
            true -> Modifier.fillMaxSize()
            false -> Modifier.padding(vertical = 32.dp)
        }
    )

    val dialogShape by rememberUpdatedState(
        when (isCompact) {
            true -> RectangleShape
            false -> AlertDialogDefaults.shape
        }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isCompact,
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            decorFitsSystemWindows = !isCompact,
        ),
    ) {
        Column(
            modifier = sizeModifier
                .background(MaterialTheme.colorScheme.background, shape = dialogShape)
                .clip(dialogShape)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TimePicker(
                state = state,
                colors = colors,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                dismissButton()
                confirmButton()
            }
        }
    }
}