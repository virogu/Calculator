package com.virogu.calculator.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by Virogu
 * Date 2022-08-05 15:24
 **/

@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return remember {
        WindowInfo(
            screenWidthInfo = when {
                configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
                configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium
                else -> WindowInfo.WindowType.Expanded
            },
            screenHeightInfo = when {
                configuration.screenHeightDp < 480 -> WindowInfo.WindowType.Compact
                configuration.screenHeightDp < 900 -> WindowInfo.WindowType.Medium
                else -> WindowInfo.WindowType.Expanded
            },
            screenWidth = configuration.screenWidthDp,
            screenHeight = configuration.screenHeightDp,
            screenWidthDp = configuration.screenWidthDp.dp,
            screenHeightDp = configuration.screenHeightDp.dp,
        )
    }
}

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Int,
    val screenHeight: Int,
    val screenWidthDp: Dp,
    val screenHeightDp: Dp,
) {
    sealed class WindowType {
        object Compact : WindowType()
        object Medium : WindowType()
        object Expanded : WindowType()
    }
}

@Composable
fun isWideScreen(): Boolean = with(rememberWindowInfo()) {
    screenWidth > screenHeight
}