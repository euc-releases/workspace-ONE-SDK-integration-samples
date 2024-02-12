// Copyright 2024 VMware LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

private val capsRegex = Regex("[A-Z]")

fun String.camelSpaced(): String {
    return capsRegex.replace(this) {" " + it.value}
}
