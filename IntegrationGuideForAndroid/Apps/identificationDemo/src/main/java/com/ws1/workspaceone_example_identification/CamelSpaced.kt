// Copyright 2023 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.ws1.workspaceone_example_identification

private val capsRegex = Regex("[A-Z]")

fun String.camelSpaced(): String {
    return capsRegex.replace(this) {" " + it.value}
}
