// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import androidx.annotation.NonNull;

import com.airwatch.event.SDKClientConfig;
import com.airwatch.event.WS1AnchorEvents;

public class Application extends android.app.Application implements SDKClientConfig {
    @NonNull
    @Override
    public WS1AnchorEvents getEventHandler() {
        return new AppWS1AnchorEvents();
    }
}
