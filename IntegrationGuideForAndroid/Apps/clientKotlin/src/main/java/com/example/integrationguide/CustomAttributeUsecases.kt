// Copyright 2026 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import com.airwatch.sdk.CustomAttributeData
import com.airwatch.sdk.SDKManager
import com.airwatch.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias Callback = (String) -> Unit

class CustomAttributeUsecases(
    private val context: Context
) {

    private val logTag = "CustomAttributeUsecases"

    private val GROUP_NAME = "my.group"
    private val KEY_PREFIX = "my.key."

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private fun executeCrudOperation(
        operation: suspend (SDKManager) -> String,
        callback: Callback
    ) {
        ioScope.launch {
            try {
                val sdkManager = SDKManager.init(context)
                val message = operation(sdkManager)
                callback.invoke(message)
            } catch (e: Exception) {
                val errorMessage = "Operation failed: ${e.message}"
                Logger.e(logTag, errorMessage, e)
                callback.invoke(errorMessage)
            }
        }
    }

    fun prepareCustomAttributeData(count: Int): List<CustomAttributeData> {
        val customAttributes = mutableListOf<CustomAttributeData>()
        for (i in 0 until count) {
            val key = "$KEY_PREFIX$i"
            val customAttribute = CustomAttributeData().apply {
                group = GROUP_NAME
                this.key = key
                this.value = (System.currentTimeMillis() + i * 1000L).toString()
            }
            customAttributes.add(customAttribute)
        }
        Logger.i(logTag, "Prepared ${customAttributes.size} custom attribute(s)")
        return customAttributes
    }

    fun readAttributes(count: Int, callback: Callback) {
        executeCrudOperation({ sdkManager ->
            val result = sdkManager.readCustomAttributes(prepareCustomAttributeData(count))
            Logger.i(logTag, "Read ${result.size} custom attribute(s)")
            result.forEach {
                Logger.i(logTag, "Custom Attribute - Key: ${it.key}, Value: ${it.value}, Group: ${it.group}")
            }
            "Read ${result.size} attributes successfully"
        }, callback)
    }

    fun writeAttributes(count: Int, callback: Callback) {
        executeCrudOperation({ sdkManager ->
            val customAttributes = prepareCustomAttributeData(count)
            val result = sdkManager.writeCustomAttributes(customAttributes)
            Logger.i(logTag, "Wrote $result custom attribute(s)")
            "Wrote $result attributes successfully"
        }, callback)
    }

    fun deleteAttributes(count: Int, callback: Callback) {
        executeCrudOperation({ sdkManager ->
            val customAttributes = prepareCustomAttributeData(count)
            val result = sdkManager.deleteCustomAttributes(customAttributes)
            Logger.i(logTag, "Deleted $result custom attribute(s)")
            "Deleted $result attributes successfully"
        }, callback)
    }
}


