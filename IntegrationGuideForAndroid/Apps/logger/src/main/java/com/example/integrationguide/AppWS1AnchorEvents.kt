// Copyright 2024 VMware LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.airwatch.event.WS1AnchorEvents
import com.airwatch.sdk.profile.AnchorAppStatus
import com.airwatch.sdk.profile.ApplicationProfile
import com.airwatch.sdk.shareddevice.ClearReasonCode

class AppWS1AnchorEvents: WS1AnchorEvents {
    companion object {
        private val TAG = AppWS1AnchorEvents::class.java.simpleName
    }

    override fun onApplicationConfigurationChange(applicationConfiguration: Bundle, context: Context) {
        Log.d(TAG, "onApplicationConfigurationChange(${applicationConfiguration})")
    }

    override fun onApplicationProfileReceived(
        context: Context,
        profileId: String,
        appProfile: ApplicationProfile
    ){
        report("onApplicationProfileReceived(,${profileId},${appProfile})", context)
    }

    override fun onClearAppDataCommandReceived(context: Context, reasonCode: ClearReasonCode) {
        report("onClearAppDataCommandReceived(,${reasonCode})", context)
    }

    override fun onAnchorAppStatusReceived(context: Context, appStatus: AnchorAppStatus) {
        report("onAnchorAppStatusReceived(,${appStatus})", context)
    }

    override fun onAnchorAppUpgrade(context: Context, isUpgrade: Boolean) {
        report("onAnchorAppUpgrade(,${isUpgrade})", context)
    }

    private fun report(message: String, context: Context) {
        val alert = AlertDialog.Builder(context)
        alert.setMessage(message)
        alert.show()
    }
}