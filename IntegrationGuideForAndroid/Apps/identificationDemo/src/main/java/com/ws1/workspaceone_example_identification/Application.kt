// Copyright 2023 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.ws1.workspaceone_example_identification

import com.airwatch.event.SDKClientConfig
import com.airwatch.event.WS1AnchorEvents

class Application: android.app.Application(), SDKClientConfig {

    override fun getEventHandler(): WS1AnchorEvents {
        return AppWS1AnchorEvents()
    }

}