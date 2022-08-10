package com.virogu.calculator.tool

import android.util.Log
import com.mpobjects.bdparsii.eval.Parser
import com.virogu.calculator.bean.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.floor

class CalculatorTool {

    companion object {
        private const val TAG = "CalculatorTool"
    }

    private val calculateList: MutableList<CalculatorEntity> = mutableListOf()

    private val mCalculatorString: MutableStateFlow<String> = MutableStateFlow("0")
    val calculatorStringFlow: StateFlow<String> = mCalculatorString

    private val mResult: MutableStateFlow<String> = MutableStateFlow("")
    val resultStringFlow: StateFlow<String> = mResult

    fun add(new: CalculatorEntity): Result<Any> {
        val r = dealOption(new)
        if (r.isSuccess) {
            //measureTimeMillis {
            //    calculate()
            //}.also {
            //    Log.v(TAG, "calculate spend $it ms")
            //}
            calculate()
        }
        return r
    }

    fun del(): Result<Any> {
        val r = delIml()
        if (r.isSuccess) {
            if (calculateList.isNotEmpty()) {
                calculate()
            }
        }
        return r
    }

    fun clear() {
        calculateList.clear()
        mCalculatorString.tryEmit("0")
        mResult.tryEmit("")
    }

    fun calculate(auto: Boolean = true): Result<String> {
        if (calculateList.isEmpty()) {
            return Result.failure(IllegalStateException("None need calculate"))
        }
        if (calculateList.last() is CalculatorOptionEntity) {
            if (auto) {
                refreshCalculatorString()
                return Result.failure(IllegalArgumentException("last is operator"))
            } else {
                calculateList.removeLast()
            }
        }
        val s = refreshCalculatorString().formatExpression()
        return try {
            Log.d(TAG, "calculator: $s")
            val r = Parser.parse(s).evaluate().toDouble().formatLong()
            Log.d(TAG, "calculator: $s = $r")
            mResult.tryEmit(r)
            Result.success(r)
        } catch (e: Throwable) {
            Log.w(TAG, e)
            mResult.tryEmit("计算出错")
            Result.failure(e)
        } finally {
            if (!auto) {
                calculateList.clear()
            }
        }
    }

    private fun refreshCalculatorString(): String {
        val s = calculateList.joinToString(separator = "") {
            it.value
        }
        mCalculatorString.tryEmit(s)
        return s
    }

    private fun delIml(): Result<Any> {
        return if (calculateList.isNotEmpty()) {
            when (val last = calculateList.last()) {
                is SingleNumber -> {
                    calculateList.removeLast()
                }
                is UnionNumber -> {
                    calculateList.removeLast()
                    if (last.value.length > 1) {
                        calculateList.add(UnionNumber(last.value.dropLast(1)))
                    }
                }
                is CalculatorOptionEntity -> {
                    calculateList.removeLast()
                }
                is CalculatorSpecialEntity -> {
                    calculateList.removeLast()
                }
            }
            if (calculateList.isEmpty()) {
                clear()
            }
            Result.success(0)
        } else {
            Result.failure(IllegalStateException("None"))
        }
    }

    private fun dealOption(new: CalculatorEntity): Result<Any> {
        when (new) {
            is CalculatorNumberEntity -> {
                if (calculateList.isEmpty()) {
                    calculateList.add(Zero)
                }
                val last = calculateList.last()
                if (calculateList.size == 1) {
                    if (last.value == Zero.value) {
                        if (new.value != DotNumber.value) {
                            calculateList.removeLast()
                        }
                    }
                }
                when (last) {
                    is CalculatorNumberEntity -> {
                        if (new is DotNumber) {
                            if (!calculateList.enableInputDot()) {
                                return Result.failure(IllegalArgumentException("不能多次输入小数点"))
                            }
                        }
                        calculateList.add(new)
                    }
                    is CalculatorOptionEntity -> {
                        calculateList.add(new)
                        return Result.success(0)
                    }
                    is CalculatorSpecialEntity -> {
                        calculateList.add(new)
                        return Result.success(0)
                    }
                }
            }
            is CalculatorOptionEntity -> {
                if (calculateList.isEmpty()) {
                    try {
                        val lastResult = mResult.value.toDouble().formatLong()
                        calculateList.add(UnionNumber(lastResult))
                    } catch (e: Throwable) {
                        calculateList.clear()
                        calculateList.add(Zero)
                    }
                    calculateList.add(new)
                    return Result.success(0)
                }
                when (calculateList.last()) {
                    is CalculatorNumberEntity -> {
                        calculateList.add(new)
                    }
                    is CalculatorOptionEntity -> {
                        calculateList.removeLast()
                        calculateList.add(new)
                    }
                    is CalculatorSpecialEntity -> {
                        calculateList.add(new)
                    }
                }
            }
            is CalculatorSpecialEntity -> {
                if (new is LeftBrackets && calculateList.lastOrNull() is CalculatorNumberEntity) {
                    calculateList.add(Multiply)
                }
                calculateList.add(new)

            }
        }
        return Result.success(0)
    }

    private fun List<CalculatorEntity>.enableInputDot(): Boolean {
        for (i in this.size - 1 downTo 0) {
            return when (val c = this[i]) {
                is DotNumber -> false
                is UnionNumber -> {
                    if (c.value.contains(DotNumber.value)) {
                        return false
                    } else {
                        continue
                    }
                }
                is CalculatorOptionEntity -> true
                else -> {
                    continue
                }
            }
        }
        return true
    }

    private fun String.formatExpression(): String {
        return this.replace("×", "*")
            .replace("÷", "/")
            .replace("E+", "*10^", true)
            .replace("E-", "/10^", true)
            .replace("E", "*10^", true)
    }

    @Suppress("unused")
    private fun Double.formatLong(): String {
        return if (this - floor(this) < 1e-10) {
            this.toString().removeSuffix(".0")
        } else {
            this.toString()
        }
    }

}