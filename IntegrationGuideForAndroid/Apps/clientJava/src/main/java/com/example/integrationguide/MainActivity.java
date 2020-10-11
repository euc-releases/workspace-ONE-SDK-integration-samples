// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airwatch.sdk.SDKManager;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    SDKManager sdkManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();
        configureStatus();

        startSDK();
    }

    private void configureStatus() {
        final TextView textView = (TextView) findViewById(
            R.id.textViewConfiguration);
        textView.setText(R.string.status_placeholder);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { statusToggle(); }
        });
    }

    private void statusToggle() {
        final View view = findViewById(R.id.scrollView);
        final boolean visible = (view.getVisibility() == View.VISIBLE);
        view.setVisibility(visible ? View.GONE : View.VISIBLE);
        findViewById(R.id.toggleView).setVisibility(
            visible ? View.VISIBLE : View.GONE);
    }

    private void startSDK() { new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SDKManager initSDKManager = SDKManager.init(
                        MainActivity.this);
                    sdkManager = initSDKManager;
                    final String shortMessage = getString(
                        R.string.status_ok,
                        Float.toString(initSDKManager.getConsoleVersion())
                    );
                    toastHere(shortMessage);
                    final String sdkProfileJSON =
                        initSDKManager.getSDKProfileJSONString();
                    final String longMessage = sdkProfileJSON == null
                        ? getString(R.string.null_sdk_profile_json)
                        : (new JSONObject(sdkProfileJSON)).toString(4);
                    showStatus(shortMessage, longMessage);
                } catch (Exception exception) {
                    sdkManager = null;
                    final String message = getString(R.string.status_ng);
                    toastHere(message);
                    showStatus(message, exception.toString());
                }
            }
    }).start(); }

    private void showStatus(
        final String shortMessage, final String longMessage
    ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.textViewConfiguration))
                    .setText(shortMessage);
                ((TextView) findViewById(R.id.textViewScrolling))
                    .setText(longMessage);
            }
        });
    }

    private void toastHere(final String message) { runOnUiThread(new Runnable() {
        @Override
        public void run() { Toast.makeText(
            MainActivity.this, message, Toast.LENGTH_LONG
        ).show(); }
    }); }
}
