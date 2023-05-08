// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import com.airwatch.event.SDKClientConfig
import com.airwatch.event.WS1AnchorEvents

class Application: android.app.Application(), SDKClientConfig {

    override fun getEventHandler(): WS1AnchorEvents {
        return AppWS1AnchorEvents()
    }

}