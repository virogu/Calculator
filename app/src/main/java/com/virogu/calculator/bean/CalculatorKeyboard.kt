package com.virogu.calculator.bean

/**
 * Created by Virogu
 * Date 2022-08-05 16:08
 **/

sealed class Keyboard(
    open val cells: Int = 1
)

sealed class FunctionKey(
    override val cells: Int = 1
) : Keyboard(cells)

class ClearKey(
    override val cells: Int = 1
) : FunctionKey(cells)

class ReduceKey(
    override val cells: Int = 1
) : FunctionKey(cells)

class ResultKey(
    override val cells: Int = 1
) : FunctionKey(cells)

sealed class CalculatorKey(
    open val value: CalculatorEntity,
    override val cells: Int = 1
) : Keyboard(cells)

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