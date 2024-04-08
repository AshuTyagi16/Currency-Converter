package com.multicurrency.app.core_base.shared.util

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import kotlin.math.abs
import kotlin.math.log10

fun Double.roundUp(n: Long): Double {
    val decimalMode = DecimalMode(
        decimalPrecision = this.toInt().length().toLong(),
        roundingMode = RoundingMode.CEILING,
        scale = n
    )
    return BigDecimal.fromDouble(this, decimalMode).doubleValue(exactRequired = false)
}

fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}