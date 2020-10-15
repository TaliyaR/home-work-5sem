package com.example.homework5sem

import io.reactivex.Single
import java.util.concurrent.TimeUnit

class CalculatorApi {

    private var lastValue: Pair<String, String>? = null
    private var preLastValue: Pair<String, String>? = null

    fun calculateFirstNum(second: Int, third: Int): Single<Triple<Int, Int, Int>> {
        return Single.just(Triple((third - second), second, third))
            .delay(5, TimeUnit.SECONDS)
    }

    fun calculateSecondNum(first: Int, third: Int): Single<Triple<Int, Int, Int>> {
        return Single.just(Triple(first, (third - first), third))
            .delay(5, TimeUnit.SECONDS)
    }

    fun calculateThirdNum(first: Int, second: Int): Single<Triple<Int, Int, Int>> {
        return Single.just(Triple(first, second, (first + second)))
            .delay(5, TimeUnit.SECONDS)
    }

    fun calculate(newValue: Pair<String, String>): Single<Triple<Int, Int, Int>> {
        return if (lastValue != null && lastValue?.first != newValue.first) {
            val mLastValue = lastValue
            preLastValue = lastValue
            lastValue = newValue
            calculation(newValue, mLastValue!!)
        } else if (preLastValue != null && lastValue?.first == newValue.first && preLastValue?.first != newValue.first) {
            lastValue = newValue
            calculation(newValue, preLastValue!!)
        } else {
            lastValue = newValue
            Single.error(Exception("Error"))
        }
    }

    private fun calculation(
        lastValue: Pair<String, String>,
        preLastValue: Pair<String, String>
    ): Single<Triple<Int, Int, Int>> {
        this.lastValue = lastValue
        this.preLastValue = preLastValue
        try {
            val lastNumber = Integer.parseInt(lastValue.second)
            val preLastNumber = Integer.parseInt(preLastValue.second)
            return when (lastValue.first) {
                "First" -> {
                    when (preLastValue.first) {
                        "Second" -> {
                            calculateThirdNum(
                                lastNumber,
                                preLastNumber
                            )
                        }
                        "Third" -> {
                            calculateSecondNum(
                                lastNumber,
                                preLastNumber
                            )
                        }
                        else -> {
                            Single.error(Exception("Error"))
                        }
                    }
                }
                "Second" -> {
                    when (preLastValue.first) {
                        "First" -> {
                            calculateThirdNum(
                                preLastNumber,
                                lastNumber
                            )
                        }
                        "Third" -> {
                            calculateFirstNum(
                                lastNumber,
                                preLastNumber
                            )
                        }
                        else -> {
                            Single.error(Exception("Error"))
                        }
                    }
                }
                "Third" -> {
                    when (preLastValue.first) {
                        "First" -> {
                            calculateSecondNum(
                                preLastNumber,
                                lastNumber
                            )
                        }
                        "Second" -> {
                            calculateFirstNum(
                                preLastNumber,
                                lastNumber
                            )
                        }
                        else -> {
                            Single.error(Exception("Error"))
                        }
                    }
                }
                else -> {
                    Single.error(Exception("Error"))
                }
            }
        } catch (e: NumberFormatException) {
            return Single.error(Exception("Error"))
        }
    }
}
