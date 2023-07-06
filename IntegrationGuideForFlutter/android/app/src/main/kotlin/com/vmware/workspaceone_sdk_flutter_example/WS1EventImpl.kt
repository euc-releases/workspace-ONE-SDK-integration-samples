// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.vmware.workspaceone_sdk_flutter_example
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.airwatch.sdk.profile.AnchorAppStatus
import com.airwatch.sdk.profile.ApplicationProfile
import com.airwatch.sdk.shareddevice.ClearReasonCode
import com.airwatch.event.WS1AnchorEvents
import org.koin.core.component.KoinComponent

class WS1EventImpl : WS1AnchorEvents,KoinComponent {
    override fun onApplicationConfigurationChange(bundle: Bundle?, context: Context) {}
    override fun onApplicationProfileReceived(
        context: Context,
        s: String,
        applicationProfile: ApplicationProfile
    ) {
        Log.d("SDK Init", "onApplicationProfileReceived")
    }

    override fun onClearAppDataCommandReceived(context: Context, clearReasonCode: ClearReasonCode) {
        Log.d("SDK Init", "onClearAppDataCommandReceived")
    }

    override fun onAnchorAppStatusReceived(context: Context, anchorAppStatus: AnchorAppStatus) {}
    override fun onAnchorAppUpgrade(context: Context, b: Boolean) {}
}