package com.gthio.datetimepickers.ui.util

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

sealed interface SizeCategory {
    object Mobile : SizeCategory
    object Tablet : SizeCategory

    companion object {
        fun calculate(sizeClass: WindowSizeClass): SizeCategory {
            if (sizeClass.widthSizeClass == WindowWidthSizeClass.Compact) return Mobile

            if (sizeClass.heightSizeClass == WindowHeightSizeClass.Compact) return Mobile

            return Tablet
        }
    }
}