// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airwatch.sdk.SDKManager
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {

    private var sdkManager: SDKManager? = null
    companion object {
        private const val NOTIFICATION_REQ_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()
        configureStatus()
        setUpPermissions()
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

    private fun configureStatus() {
        findViewById<TextView>(R.id.textViewConfiguration).let {
            it.text = getString(R.string.status_placeholder)
            it.setOnClickListener { statusToggle() }
        }
    }

    private fun statusToggle() {
        val visible = findViewById<View>(R.id.scrollView).let { scrollView ->
            !(scrollView.visibility == View.VISIBLE).also {
                scrollView.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
        findViewById<View>(R.id.toggleView).visibility =
            if (visible) View.VISIBLE else View.GONE
    }

    private fun startSDK() { thread {
        try {
            val initSDKManager = SDKManager.init(this)
            sdkManager = initSDKManager
            getString(
                R.string.status_ok, initSDKManager.consoleVersion.toString()
            ).let {
                toastHere(it)
                showStatus(
                    it, listOf(
                        "deviceUid: ", initSDKManager.deviceUid,
                        "\ndeviceSerialId: ", initSDKManager.deviceSerialId,
                        "\n\ncustomSettings: ", initSDKManager.customSettings,
                        "\n\n", initSDKManager.sdkProfileJSONString?.run {
                            JSONObject(this).toString(4)
                        } ?: getString(R.string.null_sdk_profile_json)
                    ).map {
                        it?.run { if (it.isEmpty()) "empty" else it } ?: "null"
                    }.joinToString(separator = "")
                )
            }
        }
        catch (exception: Exception) {
            sdkManager = null
            getString(R.string.status_ng).let {
                toastHere(it)
                showStatus(it, exception.toString())
            }
        }
    }}

    private fun showStatus(shortMessage: String, longMessage: String) {
        runOnUiThread {
            findViewById<TextView>(R.id.textViewConfiguration).text =
                shortMessage
            findViewById<TextView>(R.id.textViewScrolling).text =
                longMessage
        }
    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
