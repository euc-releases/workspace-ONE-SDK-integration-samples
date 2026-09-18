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

    /**
     * Reads a single SDK attribute without letting a failure prevent the other
     * attributes from being displayed.
     */
    private fun attribute(getter: () -> String?): String = try {
        getter()?.run { if (isEmpty()) "empty" else this } ?: "null"
    }
    catch (exception: Exception) {
        "unavailable: $exception"
    }

    /**
     * Describes the application profile first, then the certificates it carries,
     * so that an empty or partly populated profile is still reported.
     */
    private fun applicationProfileReport(manager: SDKManager): String {
        val profile = try {
            manager.applicationProfile
        }
        catch (exception: Exception) {
            return "appProfile: unavailable: $exception"
        } ?: return "appProfile: null"

        val lines = mutableListOf(
            "appProfile id: ${attribute { profile.profileId }}",
            "appProfile name: ${attribute { profile.name }}"
        )

        val certificates = try {
            profile.certificates
        }
        catch (exception: Exception) {
            lines.add("appProfile certificates: unavailable: $exception")
            return lines.joinToString(separator = "\n")
        }

        if (certificates.isNullOrEmpty()) {
            lines.add("appProfile certificates: none")
            return lines.joinToString(separator = "\n")
        }

        lines.add("appProfile certificates: ${certificates.size}")
        certificates.forEachIndexed { index, certificate ->
            lines.add("\n  certificate #${index + 1}")
            lines.add("  thumbprint: ${attribute { certificate.thumbprint }}")
            lines.add("  type: ${attribute { certificate.type }}")
            lines.add("  name: ${attribute { certificate.name }}")
            lines.add("  certificate String: ${attribute { certificate.certificateString }}")
        }
        return lines.joinToString(separator = "\n")
    }

    private fun startSDK() { thread {
        val initSDKManager = try {
            SDKManager.init(this)
        }
        catch (exception: Exception) {
            sdkManager = null
            getString(R.string.status_ng).let {
                toastHere(it)
                showStatus(it, exception.toString())
            }
            return@thread
        }

        sdkManager = initSDKManager
        getString(
            R.string.status_ok, attribute { initSDKManager.consoleVersion.toString() }
        ).let {
            toastHere(it)
            showStatus(
                it, listOf(
                    "deviceUid: ", attribute { initSDKManager.deviceUid },
                    "\ndeviceSerialId: ", attribute { initSDKManager.deviceSerialId },
                    "\n\n", applicationProfileReport(initSDKManager),
                    "\n\ncustomSettings: ", attribute { initSDKManager.customSettings },
                    "\n enrollmentUsername: ", attribute { initSDKManager.getEnrollmentUsername() },
                    "\n\n", attribute {
                        initSDKManager.sdkProfileJSONString?.run {
                            JSONObject(this).toString(4)
                        } ?: getString(R.string.null_sdk_profile_json)
                    }
                ).joinToString(separator = "")
            )
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
