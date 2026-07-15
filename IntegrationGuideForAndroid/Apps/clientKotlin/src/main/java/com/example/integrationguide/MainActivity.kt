// Copyright 2023 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airwatch.sdk.SDKManager
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {

    @Volatile
    private var sdkManager: SDKManager? = null
    companion object {
        private const val NOTIFICATION_REQ_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.subtitle = getString(R.string.app_title)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_logs-> {
                sdkManager?.uploadApplicationLogs()
                toastHere("Sending Logs...")
                true
            }
            R.id.custom_attributes -> {
                startActivity(Intent(this, CustomAttributeDemoActivity::class.java))
                true
            }
            R.id.show_certificate_details -> {
                showCertificateDetails()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                        "\n enrollmentUsername: ", initSDKManager.getEnrollmentUsername(),
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

    /**
     * Displays the details of the certificates returned by the SDKManager.
     * If no certificates are returned, a message indicating that is shown instead.
     */
    private fun showCertificateDetails() {
        val manager = sdkManager ?: run {
            findViewById<TextView>(R.id.textViewIntegration).text = getString(R.string.sdk_not_initialized_error)
            return
        }
        thread {
            manager.requestCertificates { certDefinitionList ->
                val message = if (certDefinitionList.isNullOrEmpty()) {
                    getString(R.string.certificate_not_found_message)
                } else {
                    certDefinitionList.mapIndexed { index, certificate ->
                        listOf("Certificate #${index + 1}", "Id: ${certificate.id}",
                            "Name: ${certificate.name}", "Type: ${certificate.type}",
                            "Thumbprint: ${certificate.thumbprint}").joinToString(separator = "\n")
                    }.joinToString(separator = "\n\n")
                }
                runOnUiThread {
                    findViewById<TextView>(R.id.textViewIntegration).text = message
                }
            }
        }
    }
}
