package com.virogu.calculator.tool

import android.util.Log
import com.virogu.calculator.bean.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.floor

class CalculatorTool {

    companion object {
        private const val TAG = "CalculatorTool"
    }

    private val calculateList: MutableList<CalculatorEntity> = mutableListOf(Zero)

    private val mCalculatorString: MutableStateFlow<String> = MutableStateFlow("0")
    val calculatorStringFlow: StateFlow<String> = mCalculatorString

    private val mResult: MutableStateFlow<String> = MutableStateFlow("")
    val resultStringFlow: StateFlow<String> = mResult

    fun add(new: CalculatorEntity): Result<Any> {
        val r = dealOption(new)
        if (r.isSuccess) {
            calculator()
        }
        return r
    }

    fun del(): Result<Any> {
        val r = delIml()
        if (r.isSuccess) {
            calculator()
        }
        return r
    }

    fun clear() {
        calculateList.clear()
        mCalculatorString.tryEmit("0")
        mResult.tryEmit("")
    }

    fun calculator(auto: Boolean = true) {
        if (calculateList.isEmpty()) {
            return
        }
        if (calculateList.last() is CalculatorOptionEntity) {
            if (auto) {
                refreshCalculatorString()
                return
            } else {
                calculateList.removeLast()
            }
        }
        val s = refreshCalculatorString()
        try {
            //val builder = ExpressionBuilder(s).build()
            //if (auto && !builder.validate().isValid) {
            //    return
            //}
            val r = Calculator.conversion(s).formatLong()
            Log.d(TAG, "calculator: $s = $r")
            mResult.tryEmit(r)
        } catch (e: Throwable) {
            Log.w(TAG, "e: ${e.localizedMessage}")
            mResult.tryEmit("计算出错")
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
        mCalculatorString.tryEmit(s.replace("*", "×").replace("/", "÷"))
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

    private fun Double.formatLong(): String {
        return if (this - floor(this) < 1e-10) {
            this.toString().removeSuffix(".0")
        } else {
            this.toString()
        }
    }
}