// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import com.airwatch.login.branding.BrandingCallBack;
import com.airwatch.login.branding.DefaultBrandingManager;
import com.airwatch.sdk.context.SDKContextManager;
import com.airwatch.sdk.context.awsdkcontext.SDKDataModelImpl;
import com.airwatch.simplifiedenrollment.views.AWInputField;
import com.airwatch.simplifiedenrollment.views.AWNextActionView;

// This class can be pasted into the Integration Guide. Remove the comments so
// that it fits on one page.

class BrandingManager implements com.airwatch.login.branding.BrandingManager
{
    // Helper function.
    static DefaultBrandingManager createDefaultBrandingManager(
        android.app.Application application
    ) {
        return new DefaultBrandingManager(
            SDKContextManager.getSDKContext().getSDKConfiguration(),
            new SDKDataModelImpl(application.getApplicationContext()),
            application.getApplicationContext(),
            true
        );
    }

    // Singleton business.
    private static BrandingManager instance = null;
    public static BrandingManager getInstance(android.app.Application application) {
        if (instance == null) {
            instance = new BrandingManager(createDefaultBrandingManager(application));
        }
        return instance;
    }
    public static BrandingManager getInstance(android.app.Activity activity) {
        return getInstance(activity.getApplication());
    }
    public static BrandingManager getInstance() { return instance; }

    // Singleton constructor.
    private BrandingManager(DefaultBrandingManager defaultBrandingManager) {
        this.defaultBrandingManager = defaultBrandingManager;
    }

    // Property, also used for delegation.
    private DefaultBrandingManager defaultBrandingManager;
    public DefaultBrandingManager getDefaultBrandingManager() {
        return this.defaultBrandingManager;
    }

    // Delegated methods.
    @Override
    public void applyBranding(android.app.Activity activity) {
        defaultBrandingManager.applyBranding(activity);
    }

    @Override
    public void applyBranding(AWInputField inputField) {
        defaultBrandingManager.applyBranding(inputField);
    }

    @Override
    public void brandLoadingScreenLogo(BrandingCallBack callback) {
        defaultBrandingManager.brandLoadingScreenLogo(callback);
    }

    @Override
    public void brandInputScreenLogo(BrandingCallBack callback) {
        defaultBrandingManager.brandInputScreenLogo((callback));
    }

    @Override
    public Integer getPrimaryColor() {
        return defaultBrandingManager.getPrimaryColor();
    }

    @Override
    public void applyBranding(AWNextActionView nextActionView) {
        defaultBrandingManager.applyBranding(nextActionView);
    }
}
