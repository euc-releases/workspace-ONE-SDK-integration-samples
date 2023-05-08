package com.vmware.workspaceone_example_identification

import com.airwatch.event.SDKClientConfig
import com.airwatch.event.WS1AnchorEvents

class Application: android.app.Application(), SDKClientConfig {

    override fun getEventHandler(): WS1AnchorEvents {
        return AppWS1AnchorEvents()
    }

}