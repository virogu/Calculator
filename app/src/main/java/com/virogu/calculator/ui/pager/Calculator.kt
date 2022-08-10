package com.virogu.calculator.ui.pager

import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virogu.calculator.bean.*
import com.virogu.calculator.model.MainModel
import com.virogu.calculator.ui.theme.OptionKeyboardColor
import com.virogu.calculator.ui.theme.buttonBackgroundColor
import com.virogu.calculator.ui.theme.buttonContentColor
import com.virogu.calculator.view.rememberWindowInfo

/**
 * Created by Virogu
 * Date 2022-08-05 15:58
 **/

@Composable
fun Calculator(model: MainModel) {
    val windowInfo = rememberWindowInfo()
    //val isWidthScreen = isWideScreen()
    //if (false) {
    //    KeyboardViewHorizontal(model, model.keyboardListHorizontal)
    //} else {
    //    KeyboardViewVertical(model, model.keyboardListVertical)
    //}
    KeyboardViewVertical(model, model.getKeyBoardList(windowInfo))
}

@Composable
fun KeyboardViewHorizontal(model: MainModel, keyboardList: Pair<Int, List<Keyboard>>) {
    val state = rememberLazyGridState()
    val cells = keyboardList.first
    val list = keyboardList.second
    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    OutputView(model = model, modifier = Modifier.align(Alignment.End))
                }
            }
        }
        Box(
            Modifier
                .weight(1.4f)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                columns = GridCells.Fixed(cells),
                state = state,
                contentPadding = PaddingValues(16.dp, 16.dp)
            ) {
                KeyboardView(list = list, model = model)
            }
        }
    }
}

@Composable
fun KeyboardViewVertical(model: MainModel, keyboardList: Pair<Int, List<Keyboard>>) {
    val state = rememberLazyGridState()
    val cells = keyboardList.first
    val list = keyboardList.second
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = false,
            verticalArrangement = Arrangement.Bottom,
            horizontalArrangement = Arrangement.Start,
            columns = GridCells.Fixed(cells),
            state = state,
            contentPadding = PaddingValues(16.dp)
        ) {
            item(span = { GridItemSpan(cells) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                ) {
                    OutputView(model = model, modifier = Modifier.align(Alignment.End))
                }
            }
            KeyboardView(list = list, model = model)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OutputView(
    modifier: Modifier,
    model: MainModel
) {
    val isDark = isSystemInDarkTheme()
    val calculatorString = model.calculatorStringFlow.collectAsState()
    val resultString = model.resultStringFlow.collectAsState()
    val tag = model.resultChangedTag.collectAsState()
    val font1 = remember {
        mutableStateOf(Pair(32.sp, buttonContentColor(isDark)))
    }
    val font2 = remember {
        mutableStateOf(Pair(26.sp, buttonContentColor(isDark).copy(alpha = 0.8f)))
    }
    LaunchedEffect(tag.value) {
        if (font1.value.first > font2.value.first) {
            font1.value = Pair(24.sp, buttonContentColor(isDark).copy(alpha = 0.8f))
            font2.value = Pair(40.sp, buttonContentColor(isDark))
        }
    }
    LaunchedEffect(calculatorString.value) {
        if (font1.value.first < font2.value.first) {
            font1.value = Pair(32.sp, buttonContentColor(isDark))
            font2.value = Pair(26.sp, buttonContentColor(isDark).copy(alpha = 0.8f))
        }
    }
    AnimatedContent(
        targetState = font1.value.first,
        modifier = modifier,
        transitionSpec = {
            scaleIn(initialScale = 0.5f) with scaleOut(targetScale = 1.0f)
        }
    ) {
        Text(
            text = calculatorString.value,
            color = font1.value.second,
            modifier = modifier,
            fontSize = font1.value.first,
            lineHeight = font1.value.first,
        )
    }
    if (resultString.value.isNotEmpty()) {
        AnimatedContent(
            targetState = font2.value.first,
            modifier = modifier,
            transitionSpec = {
                scaleIn(initialScale = 0.5f) with scaleOut(targetScale = 1.0f)
            }
        ) {
            Text(
                text = "= ${resultString.value}",
                color = font2.value.second,
                modifier = modifier,
                fontSize = font2.value.first,
                lineHeight = font2.value.first,
            )
        }
    }

}

@Suppress("FunctionName")
private fun LazyGridScope.KeyboardView(list: List<Keyboard>, model: MainModel) {
    val modifier = Modifier
        .padding(8.dp)
        .height(60.dp)
    list.forEach { keyboard ->
        item(span = { GridItemSpan(keyboard.cells) }) {
            KeyboardButton(modifier, keyboard, model)
        }
    }
}

@Composable
fun KeyboardButton(modifier: Modifier, keyboard: Keyboard, model: MainModel) {
    when (keyboard) {
        is ClearKey -> {
            KeyboardButton(
                modifier = modifier,
                text = keyboard.desc,
                contentColor = OptionKeyboardColor,
            ) {
                model.clear()
            }
        }
        is ReduceKey -> {
            KeyboardButton(
                modifier = modifier,
                text = keyboard.desc,
                contentColor = OptionKeyboardColor
            ) {
                model.del()
            }
        }
        is ResultKey -> {
            KeyboardButton(
                modifier = modifier,
                text = keyboard.desc,
                fontSize = 30.sp,
                backgroundColor = OptionKeyboardColor,
                contentColor = Color.White
            ) {
                model.calculate()
            }
        }
        is SpecialKey -> {
            KeyboardButton(
                modifier = modifier,
                text = keyboard.desc,
                fontSize = 26.sp,
                contentColor = OptionKeyboardColor
            ) {
                model.plus(keyboard.value)
            }
        }
        is OptionKey -> {
            KeyboardButton(
                modifier = modifier,
                text = keyboard.desc,
                fontSize = 30.sp,
                contentColor = OptionKeyboardColor
            ) {
                model.plus(keyboard.value)
            }
        }
        is NumberKey -> {
            KeyboardButton(modifier = modifier, text = keyboard.desc) {
                model.plus(keyboard.value)
            }
        }
    }
}

@Composable
private fun KeyboardButton(
    modifier: Modifier,
    text: String,
    fontSize: TextUnit = 24.sp,
    backgroundColor: Color = buttonBackgroundColor(isSystemInDarkTheme()),
    contentColor: Color = buttonContentColor(isSystemInDarkTheme()),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        elevation = ButtonDefaults.elevation(0.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Text(text = text, fontSize = fontSize, lineHeight = fontSize)
    }
}