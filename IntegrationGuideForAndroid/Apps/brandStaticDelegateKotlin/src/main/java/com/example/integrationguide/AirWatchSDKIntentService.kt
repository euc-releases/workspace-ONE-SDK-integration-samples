// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.airwatch.sdk.AirWatchSDKBaseIntentService
import com.airwatch.sdk.profile.AnchorAppStatus
import com.airwatch.sdk.profile.ApplicationProfile
import com.airwatch.sdk.shareddevice.ClearReasonCode

class AirWatchSDKIntentService: AirWatchSDKBaseIntentService() {
    companion object {
        private val TAG = AirWatchSDKIntentService::class.java.simpleName
    }

    override fun onApplicationConfigurationChange(applicationConfiguration: Bundle) {
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