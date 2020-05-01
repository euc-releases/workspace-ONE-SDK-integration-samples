// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.airwatch.sdk.SDKManager;

public class MainActivity extends BaseActivity {
    SDKManager sdkManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();

        startSDK();
    }

    private void startSDK() { new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                final SDKManager initSDKManager = SDKManager.init(
                    MainActivity.this);
                sdkManager = initSDKManager;
                toastHere(
                    "Workspace ONE console version:"
                    + initSDKManager.getConsoleVersion());
            }
            catch (Exception exception) {
                sdkManager = null;
                toastHere(
                    "Workspace ONE failed " + exception + ".");
            }
        }
    }).start(); }

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
