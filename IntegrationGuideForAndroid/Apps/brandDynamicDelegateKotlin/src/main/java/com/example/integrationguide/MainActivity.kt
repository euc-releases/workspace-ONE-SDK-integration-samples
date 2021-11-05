// Copyright 2021 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.airwatch.sdk.SDKManager
import com.airwatch.sdk.context.SDKContext
import com.airwatch.sdk.context.SDKContextManager

class MainActivity : BaseActivity() {
    companion object {
        val channelID = "CHANNEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()

        if (
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
        ) {
            val channel = NotificationChannel(
                channelID, "Channel", NotificationManager.IMPORTANCE_DEFAULT
            )
                .also { it.description = "Diagnostic channel" }
            val manager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            manager.createNotificationChannel(channel)

            findViewById<View>(R.id.toggleView).setOnClickListener {
                val notification = NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.drawable.brand_logo_onecolour)
                    .setContentTitle("Tapped")
                    .setContentText("App Logo was tapped.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    notify(1, notification.build())
                }
            }
        }

        toastHere(configureStatus())

        val enterpriseLogoImageView = findViewById<ImageView>(R.id.imageViewEnterpriseLogo)
        enterpriseLogoImageView.post {
            BrandingManager.getInstance(this).defaultBrandingManager.brandLoadingScreenLogo({ bitmap ->
                enterpriseLogoImageView.setImageBitmap(bitmap)
            }, enterpriseLogoImageView.width, enterpriseLogoImageView.height)
        }

    }

    private fun configureStatus():String {
        val sdkContext = SDKContextManager.getSDKContext()
        val sdkManager = SDKManager.init(applicationContext)
        val status = getString(
            R.string.sdk_state, sdkContext.currentState.name)

        findViewById<TextView>(R.id.textViewConfiguration).let {
            it.text = status
            it.setOnClickListener { statusToggle() }
        }

        val message = listOf(
            if (sdkContext.currentState == SDKContext.State.IDLE)
                getString(R.string.sdk_configuration_unavailable)
            else
                sdkContext.sdkConfiguration.toString(),
            mapOf(
                "c" to sdkContext.currentState.value,
                "n" to sdkContext.currentState.name,
                "I" to SDKContext.State.INITIALIZED.value,
                "C" to SDKContext.State.CONFIGURED.value
            ).toString()
        ).joinToString(separator = "\n")

        // Dump the message into the textView that is inside the scrolling
        // portion.
        findViewById<TextView>(R.id.textViewScrolling).text = message

        return status
    }

    fun statusToggle() {
        val visible = findViewById<View>(R.id.scrollView).let { scrollView ->
            (scrollView.visibility == View.VISIBLE).also {
                scrollView.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
        findViewById<View>(R.id.toggleView).visibility =
            if (visible) View.VISIBLE else View.GONE
    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
