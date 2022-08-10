package com.virogu.calculator.bean

/**
 * Created by Virogu
 * Date 2022-08-05 16:08
 **/

sealed class Keyboard(
    open val desc: String,
    open val cells: Int = 1,
)

sealed class FunctionKey(
    override val desc: String,
    override val cells: Int = 1
) : Keyboard(desc, cells)

class ClearKey(
    override val desc: String = "AC",
    override val cells: Int = 1
) : FunctionKey(desc, cells)

class ReduceKey(
    override val desc: String = "←",
    override val cells: Int = 1
) : FunctionKey(desc, cells)

class ResultKey(
    override val desc: String = "=",
    override val cells: Int = 1
) : FunctionKey(desc, cells)

sealed class CalculatorKey(
    open val value: CalculatorEntity,
    override val cells: Int = 1
) : Keyboard(value.value, cells)

data class OptionKey(
    override val value: CalculatorOptionEntity,
    override val cells: Int = 1
) : CalculatorKey(value, cells)

data class SpecialKey(
    override val value: CalculatorEntity,
    override val cells: Int = 1
) : CalculatorKey(value, cells)

data class NumberKey(
    override val value: SingleNumber,
    override val cells: Int = 1
) : CalculatorKey(value, cells)