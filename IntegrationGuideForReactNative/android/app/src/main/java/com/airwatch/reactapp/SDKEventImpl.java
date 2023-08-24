// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.airwatch.reactapp;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import org.koin.core.component.KoinComponent;
import com.airwatch.sdk.profile.AnchorAppStatus;
import com.airwatch.sdk.profile.ApplicationProfile;
import com.airwatch.sdk.shareddevice.ClearReasonCode;
import com.airwatch.event.WS1AnchorEvents;
import org.koin.core.Koin;
import org.koin.mp.KoinPlatformTools;

public class SDKEventImpl implements WS1AnchorEvents, KoinComponent {
    @Override
    public void onApplicationConfigurationChange(Bundle bundle, Context context) {

    }

    @Override
    public void onApplicationProfileReceived(Context context, String s, ApplicationProfile applicationProfile) {
        Log.d("SDK Init","onApplicationProfileReceived");
    }

    @Override
    public void onClearAppDataCommandReceived(Context context, ClearReasonCode clearReasonCode) {
        Log.d("SDK Init","onClearAppDataCommandReceived");

    }

    @Override
    public void onAnchorAppStatusReceived(Context context, AnchorAppStatus anchorAppStatus) {

    }

    @Override
    public void onAnchorAppUpgrade(Context context, boolean b) {

    }

    @Override
    public Koin getKoin() {
        return KoinPlatformTools.INSTANCE.defaultContext().get();
    }
}