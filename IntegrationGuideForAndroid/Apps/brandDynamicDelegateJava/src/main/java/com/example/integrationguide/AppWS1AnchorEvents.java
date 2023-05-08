// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.content.Context;
import android.os.Bundle;

import com.airwatch.event.WS1AnchorEvents;
import com.airwatch.sdk.profile.AnchorAppStatus;
import com.airwatch.sdk.profile.ApplicationProfile;
import com.airwatch.sdk.shareddevice.ClearReasonCode;

public class AppWS1AnchorEvents implements WS1AnchorEvents {
    @Override
    public void onApplicationConfigurationChange(
        Bundle applicationConfiguration, Context context
    ) {
    }

    @Override
    public void onApplicationProfileReceived(
        Context context,
        String profileId,
        ApplicationProfile awAppProfile
    ) {
    }

    @Override
    public void onClearAppDataCommandReceived(
        Context context,
        ClearReasonCode reasonCode
    ) {
    }

    @Override
    public void onAnchorAppStatusReceived(
        Context context,
        AnchorAppStatus appStatus
    ) {
    }

    @Override
    public void onAnchorAppUpgrade(Context context, boolean isUpgrade) {
    }
}
