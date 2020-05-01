// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.airwatch.sdk.context.SDKContextManager

class MainActivity : BaseActivity() {
    companion object {
        val channelID = "CHANNEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()

        val channel = NotificationChannel(
            channelID, "Channel", NotificationManager.IMPORTANCE_DEFAULT)
            .also { it.description = "Diagnostic channel" }
        val manager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        findViewById<View>(R.id.imageViewAppLogo)?.setOnClickListener {
            val notification = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.brand_logo_onecolour)
                .setContentTitle("Tapped")
                .setContentText("App Logo was tapped.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)) {
                notify(1, notification.build())
            }
        }

        val sdkContext = SDKContextManager.getSDKContext()
        toastHere("SDK state:${sdkContext.currentState.name}.")

        BrandingManager.getInstance(this).defaultBrandingManager
            .brandLoadingScreenLogo {
                findViewById<ImageView>(R.id.imageViewEnterpriseLogo)
                    .setImageBitmap(it)
            }

    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
