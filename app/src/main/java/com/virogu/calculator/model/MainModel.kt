package com.virogu.calculator.model

import androidx.lifecycle.ViewModel
import com.virogu.calculator.bean.*
import com.virogu.calculator.tool.CalculatorTool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Virogu
 * Date 2022-08-05 15:59
 **/

class MainModel : ViewModel() {

    //横屏
    val keyboardListHorizontal = listOf(
        NumberKey(Seven), NumberKey(Eight), NumberKey(Nine),
        SpecialKey(LeftBrackets), SpecialKey(RightBrackets),
        NumberKey(Four), NumberKey(Five), NumberKey(Six), OptionKey(Divide), OptionKey(Multiply),
        NumberKey(One), NumberKey(Two), NumberKey(Three), OptionKey(Plus), OptionKey(Reduce),
        NumberKey(Zero), NumberKey(DotNumber), ClearKey(), ReduceKey(), ResultKey(),
    ).let {
        Pair(5, it)
    }

    //竖屏
    val keyboardListVertical = listOf(
        ClearKey(), SpecialKey(LeftBrackets), SpecialKey(RightBrackets), OptionKey(Divide),
        NumberKey(Seven), NumberKey(Eight), NumberKey(Nine), OptionKey(Multiply),
        NumberKey(Four), NumberKey(Five), NumberKey(Six), OptionKey(Reduce),
        NumberKey(One), NumberKey(Two), NumberKey(Three), OptionKey(Plus),
        NumberKey(DotNumber), NumberKey(Zero), ReduceKey(), ResultKey(),
    ).let {
        Pair(4, it)
    }

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
        calculatorTool.calculator(false)
        val tag = mResultChanged.value
        if (tag == 0) {
            mResultChanged.tryEmit(1)
        } else {
            mResultChanged.tryEmit(-tag)
        }
    }

}