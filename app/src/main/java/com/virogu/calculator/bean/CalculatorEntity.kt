package com.virogu.calculator.bean

import androidx.annotation.IntRange

sealed class CalculatorEntity(
    open val value: String
)

sealed class CalculatorNumberEntity(
    override val value: String
) : CalculatorEntity(value)

data class UnionNumber(override val value: String) : CalculatorNumberEntity(value)

sealed class SingleNumber(override val value: String) : CalculatorNumberEntity(value)

object DotNumber : SingleNumber(".")

data class NormalNumber(
    @IntRange(from = 0, to = 9)
    val number: Int
) : SingleNumber(number.toString())

val Zero = NormalNumber(0)
val One = NormalNumber(1)
val Two = NormalNumber(2)
val Three = NormalNumber(3)
val Four = NormalNumber(4)
val Five = NormalNumber(5)
val Six = NormalNumber(6)
val Seven = NormalNumber(7)
val Eight = NormalNumber(8)

val Nine = NormalNumber(9)
//object Negative : Number("-")

sealed class CalculatorOptionEntity(
    override val value: String
) : CalculatorEntity(value)

sealed class CalculatorSpecialEntity(
    override val value: String
) : CalculatorEntity(value)

object LeftBrackets : CalculatorSpecialEntity("(")
object RightBrackets : CalculatorSpecialEntity(")")
object Plus : CalculatorOptionEntity("+")
object Reduce : CalculatorOptionEntity("-")
object Multiply : CalculatorOptionEntity("×")
object Divide : CalculatorOptionEntity("÷")