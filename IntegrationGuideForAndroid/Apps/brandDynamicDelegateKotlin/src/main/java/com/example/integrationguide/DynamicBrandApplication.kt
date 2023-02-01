// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

class DynamicBrandApplication: Application() {

    override fun getBrandingManager():
            com.airwatch.login.branding.BrandingManager
    {
        return BrandingManager.getInstance(this).let {
            BitmapBrandingManager.getInstance(it)
        }
    }

    override fun getNotificationIcon(): Int {
        return R.drawable.brand_logo_onecolour
    }
}