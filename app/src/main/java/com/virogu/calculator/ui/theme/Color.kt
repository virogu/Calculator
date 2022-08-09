package com.virogu.calculator.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun buttonBackgroundColor(isDark: Boolean): Color = if (isDark) {
    Color(0xFF151315)
} else {
    Color(0xFFfbfafb)
}

fun buttonContentColor(isDark: Boolean): Color = if (isDark) {
    Color(0xFFeae8ea)
} else {
    Color(0xFF010001)
}

val OptionKeyboardColor = Color(0xFFff7d30)