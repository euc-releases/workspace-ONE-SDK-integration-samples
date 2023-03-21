// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airwatch.sdk.SDKManager;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    SDKManager sdkManager = null;
    private static final int NOTIFICATION_REQ_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();
        configureStatus();
        setUpPermissions();

        startSDK();
    }

    private void setUpPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int permission = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            );

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_REQ_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NOTIFICATION_REQ_CODE){
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                toastHere("Notification Permission has been denied by user");
            } else {
                toastHere("Notification Permission has been granted by user");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    final String profileMessage = sdkProfileJSON == null
                            ? getString(R.string.null_sdk_profile_json)
                            : (new JSONObject(sdkProfileJSON)).toString(4);
                    final String longMessage = messages(
                            "deviceUid: ", initSDKManager.getDeviceUid(),
                            "\ndeviceSerialId: ",
                            initSDKManager.getDeviceSerialId(),
                            "\n", profileMessage
                    );
                    showStatus(shortMessage, longMessage);
                } catch (Exception exception) {
                    sdkManager = null;
                    final String message = getString(R.string.status_ng);
                    toastHere(message);
                    showStatus(message, exception.toString());
                }
            }
    }).start(); }

    private String messages(final String... parts) {
        final StringBuilder message = new StringBuilder();
        for (String part: parts) {
            message.append(
                    part == null ? "null" : part.isEmpty() ? "empty" : part
            );
        }
        return message.toString();
    }

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
