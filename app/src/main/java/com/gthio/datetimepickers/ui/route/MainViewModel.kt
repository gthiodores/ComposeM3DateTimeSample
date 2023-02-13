package com.gthio.datetimepickers.ui.route

import androidx.lifecycle.ViewModel
import com.gthio.datetimepickers.ui.util.TimePickerFields
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    private val _selectedTime = MutableStateFlow<TimePickerFields?>(null)
    val selectedTime = _selectedTime.asStateFlow()

    fun dateSelected(date: Long) {
        _selectedDate.update { date }
    }

    fun timeSelected(fields: TimePickerFields) {
        _selectedTime.update { fields }
    }
}