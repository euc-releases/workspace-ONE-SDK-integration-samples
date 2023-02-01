// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import org.jetbrains.annotations.NotNull;

public class DynamicBrandAWApplication extends AWApplication {
    @NotNull
    @Override
    public com.airwatch.login.branding.BrandingManager getBrandingManager() {
        return BitmapBrandingManager.getInstance(
            BrandingManager.getInstance(this));
    }

    @Override
    public int getNotificationIcon() {
        return R.drawable.brand_logo_onecolour;
    }
}
