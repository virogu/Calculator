package com.virogu.calculator.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.virogu.calculator.bean.*
import com.virogu.calculator.tool.CalculatorTool
import com.virogu.calculator.view.WindowInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Virogu
 * Date 2022-08-05 15:59
 **/

class MainModel : ViewModel() {

    private val calculatorTool = CalculatorTool()

    val calculatorStringFlow = calculatorTool.calculatorStringFlow

    val resultStringFlow = calculatorTool.resultStringFlow

    private val mResultChanged: MutableStateFlow<Int> = MutableStateFlow(0)
    val resultChangedTag: StateFlow<Int> = mResultChanged

    fun clear() {
        calculatorTool.clear()
    }

    fun del() {
        calculatorTool.del()
    }

    fun plus(new: CalculatorEntity) {
        calculatorTool.add(new)
    }

    fun calculate() {
        calculatorTool.calculate(false).getOrNull()?.also {
            mResultChanged.tryEmit(1 - mResultChanged.value)
        }
    }

    fun getKeyBoardList(windowInfo: WindowInfo): Pair<Int, List<Keyboard>> {
        val scale = windowInfo.screenWidth.toFloat() / windowInfo.screenHeight.toFloat()
        Log.d(
            "WindowInfo",
            "scale: $scale, screenWidth: ${windowInfo.screenWidth}, screenHeight: ${windowInfo.screenHeight} "
        )
        return when {
            scale < 0.66f -> {
                keyboardListNormal
            }
            scale < 0.8f -> {
                keyboardListMedium
            }
            scale < 1.2f -> {
                keyboardListLarge
            }
            else -> {
                //keyboardListMax
                keyboardListLarge
            }
        }
    }

    private val keyboardListNormal = listOf(
        //row1
        ClearKey(), SpecialKey(LeftBrackets), SpecialKey(RightBrackets), OptionKey(Divide),
        //row2
        NumberKey(Seven), NumberKey(Eight), NumberKey(Nine), OptionKey(Multiply),
        //row3
        NumberKey(Four), NumberKey(Five), NumberKey(Six), OptionKey(Reduce),
        //row4
        NumberKey(One), NumberKey(Two), NumberKey(Three), OptionKey(Plus),
        //row5
        NumberKey(DotNumber), NumberKey(Zero), ReduceKey(), ResultKey(),
    ).let {
        Pair(4, it)
    }

    private val keyboardListMedium = listOf(
        //row1
        NumberKey(Seven), NumberKey(Eight), NumberKey(Nine), ClearKey(), ReduceKey(),
        //row2
        NumberKey(Four), NumberKey(Five), NumberKey(Six), OptionKey(Divide), OptionKey(Multiply),
        //row3
        NumberKey(One), NumberKey(Two), NumberKey(Three), OptionKey(Plus), OptionKey(Reduce),
        //row4
        NumberKey(Zero), NumberKey(DotNumber),
        SpecialKey(LeftBrackets), SpecialKey(RightBrackets), ResultKey(),
    ).let {
        Pair(5, it)
    }

    private val keyboardListLarge = listOf(
        //row1
        NumberKey(Seven), NumberKey(Eight), NumberKey(Nine),
        SpecialKey(LeftBrackets), SpecialKey(RightBrackets), ClearKey(), ReduceKey(),
        //row2
        NumberKey(Four), NumberKey(Five), NumberKey(Six),
        OptionKey(Divide), OptionKey(Multiply), OptionKey(Plus), OptionKey(Reduce),
        //row3
        NumberKey(One), NumberKey(Two), NumberKey(Three),
        NumberKey(Zero), NumberKey(DotNumber), ResultKey(cells = 2),
    ).let {
        Pair(7, it)
    }

    private val keyboardListMax = listOf(
        //row1
        NumberKey(Six), NumberKey(Seven), NumberKey(Eight), NumberKey(Nine), NumberKey(Zero),
        NumberKey(DotNumber), SpecialKey(LeftBrackets), SpecialKey(RightBrackets),
        ClearKey(), ReduceKey(),
        //row2
        NumberKey(One), NumberKey(Two), NumberKey(Three), NumberKey(Four), NumberKey(Five),
        OptionKey(Divide), OptionKey(Multiply), OptionKey(Plus), OptionKey(Reduce), ResultKey(),
    ).let {
        Pair(10, it)
    }

}