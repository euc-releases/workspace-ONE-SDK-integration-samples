// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.vmware.workspaceone_example_identification

private val capsRegex = Regex("[A-Z]")

fun String.camelSpaced(): String {
    return capsRegex.replace(this) {" " + it.value}
}
