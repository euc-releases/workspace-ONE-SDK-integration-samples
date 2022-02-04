// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.content.Context;
import android.os.Bundle;
import com.airwatch.sdk.AirWatchSDKBaseIntentService;
import com.airwatch.sdk.profile.AnchorAppStatus;
import com.airwatch.sdk.profile.ApplicationProfile;
import com.airwatch.sdk.shareddevice.ClearReasonCode;

public class AirWatchSDKIntentService extends AirWatchSDKBaseIntentService {
    @Override
    protected void onApplicationConfigurationChange(
        Bundle applicationConfiguration
    ) {
    }

    @Override
    protected void onApplicationProfileReceived(
        Context context,
        String profileId,
        ApplicationProfile awAppProfile
    ) {
    }

    @Override
    protected void onClearAppDataCommandReceived(
        Context context,
        ClearReasonCode reasonCode
    ) {
    }

    @Override
    protected void onAnchorAppStatusReceived(
        Context context,
        AnchorAppStatus appStatus
    ) {
    }

    @Override
    protected void onAnchorAppUpgrade(Context context, boolean isUpgrade) {
    }
}
