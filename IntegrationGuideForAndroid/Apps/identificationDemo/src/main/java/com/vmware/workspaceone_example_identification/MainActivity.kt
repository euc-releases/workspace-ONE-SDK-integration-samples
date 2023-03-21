// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.vmware.workspaceone_example_identification

import android.Manifest
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airwatch.sdk.SDKManager
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : Activity() {

    private var sdkManager: SDKManager? = null
    companion object {
        private const val NOTIFICATION_REQ_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpPermissions()
        showStatus(getString(R.string.status_placeholder))
        startSDK()
    }

    private fun setUpPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_REQ_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            NOTIFICATION_REQ_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toastHere("Notification Permission has been denied by user")
                } else {
                    toastHere("Notification Permission has been granted by user")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startSDK() { thread {
        val devicePolicyManager = getSystemService(
            DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admins = devicePolicyManager.activeAdmins?.toString()
        try {
            val initSDKManager = SDKManager.init(this)
            sdkManager = initSDKManager
            val consoleMessage = getString(
                R.string.console_version,
                initSDKManager.consoleVersion.toString()
            )
            toastHere(consoleMessage)
            showStatus(
                getString(R.string.status_ok),
                consoleMessage,
                "customSettings: ",
                try { initSDKManager.customSettings }
                catch (exception: Exception) { exception.toString() },
                "deviceUid: ", initSDKManager.deviceUid,
                "deviceSerialId: ", initSDKManager.deviceSerialId,
                "sdkProfileJSONString: ",
                initSDKManager.sdkProfileJSONString?.run {
                    JSONObject(this).toString(4)
                },
                "activeAdmins: ", admins
            )
        }
        catch (exception: Exception) {
            sdkManager = null
            getString(R.string.status_ng).let {
                toastHere(it)
                showStatus(
                    it, exception.toString(), "activeAdmins: ", admins)
            }
        }
    }}

    private fun showStatus(shortMessage: String, vararg longMessage: String?) {
        runOnUiThread {
            findViewById<TextView>(R.id.textViewIntegration).text =
                shortMessage
            findViewById<TextView>(R.id.textViewScrolling).text =
                longMessage.flatMapIndexed { index, message ->
                    (message?.let { if (it.isEmpty()) "empty" else it }
                        ?: "null").let {
                        if (index % 2 == 1) listOf("\n\n", it) else listOf(it)
                    }
                }.joinToString(separator = "")
        }
    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
