// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import com.airwatch.login.branding.DefaultBrandingManager
import com.airwatch.sdk.context.SDKContextManager
import com.airwatch.sdk.context.awsdkcontext.SDKDataModelImpl

// This class can be pasted into the Integration Guide.
//
// This is a singleton class. However, it can't use `object` instead of `class`
// because object constructors aren't allowed parameters.

open class BrandingManager private constructor (
    val defaultBrandingManager: DefaultBrandingManager
) : com.airwatch.login.branding.BrandingManager by defaultBrandingManager
{
    companion object {
        // Helper function.
        fun createDefaultBrandingManager(
            application: android.app.Application
        ): DefaultBrandingManager
        {
            return DefaultBrandingManager(
                SDKContextManager.getSDKContext().sdkConfiguration,
                SDKDataModelImpl(application.applicationContext),
                application.applicationContext,
                true
            )
        }

        // Singleton business.
        private var instance: BrandingManager? = null
        fun getInstance(application: android.app.Application):BrandingManager {
            return instance ?:
            BrandingManager(createDefaultBrandingManager(application)).also {
                instance = it
            }
        }
        fun getInstance(activity: android.app.Activity):BrandingManager {
            return getInstance(activity.application)
        }
        fun getInstance():BrandingManager? {
            return instance
        }
    }

}
