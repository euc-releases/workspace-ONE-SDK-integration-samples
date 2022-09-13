// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.airwatch.reactapp;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.airwatch.sdk.profile.AnchorAppStatus;
import com.airwatch.sdk.profile.ApplicationProfile;
import com.airwatch.sdk.shareddevice.ClearReasonCode;
import com.workspaceonesdk.WorkspaceOneSDKIntentService;

public class SDKIntentService extends WorkspaceOneSDKIntentService {
    @Override
    protected void onApplicationConfigurationChange(Bundle bundle) {

    }

    @Override
    protected void onApplicationProfileReceived(Context context, String s, ApplicationProfile applicationProfile) {
        Log.d("SDK Init","onApplicationProfileReceived");
    }

    @Override
    protected void onClearAppDataCommandReceived(Context context, ClearReasonCode clearReasonCode) {
        Log.d("SDK Init","onClearAppDataCommandReceived");

    }

    @Override
    protected void onAnchorAppStatusReceived(Context context, AnchorAppStatus anchorAppStatus) {

    }

    @Override
    protected void onAnchorAppUpgrade(Context context, boolean b) {

    }

}
