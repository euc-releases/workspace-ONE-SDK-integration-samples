// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.airwatch.sdk.context.SDKContext;
import com.airwatch.sdk.context.SDKContextManager;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();

        final SDKContext sdkContext = SDKContextManager.getSDKContext();
        toastHere("SDK state:" + sdkContext.getCurrentState().name() + ".");
    }

    private void toastHere(final String message) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
