package com.multicurrency.app.core_base.shared.util

fun <T> Boolean.select(a: T, b: T): T = if (this) {
    a
} else {
    b
}