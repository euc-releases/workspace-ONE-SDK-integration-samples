// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airwatch.sdk.context.SDKContext;
import com.airwatch.sdk.context.SDKContextManager;

import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private static final int NOTIFICATION_REQ_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();

        toastHere(configureStatus());
        setUpPermissions();
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

    private void toastHere(final String message) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public String configureStatus() {
        final SDKContext sdkContext = SDKContextManager.getSDKContext();
        final String status = getString(
            R.string.sdk_state, sdkContext.getCurrentState().name());

        final TextView textView = (TextView) findViewById(
            R.id.textViewConfiguration);
        textView.setText(status);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { statusToggle(); }
        });

        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(
            "c", Integer.toString(sdkContext.getCurrentState().getValue()));
        hashMap.put("n", sdkContext.getCurrentState().name());
        hashMap.put(
            "I", Integer.toString(SDKContext.State.INITIALIZED.getValue()));
        hashMap.put(
            "C", Integer.toString(SDKContext.State.CONFIGURED.getValue()));

        final String[] message = new String[] {
           sdkContext.getCurrentState() == SDKContext.State.IDLE ?
               getString(R.string.sdk_configuration_unavailable) :
               sdkContext.getSDKConfiguration().toString(),
            hashMap.toString()
        };
        ((TextView) findViewById(R.id.textViewScrolling))
            .setText(TextUtils.join("\n", message));

        return status;
    }

    private void statusToggle() {
        final View view = findViewById(R.id.scrollView);
        final boolean visible = (view.getVisibility() == View.VISIBLE);
        view.setVisibility(visible ? View.GONE : View.VISIBLE);
        findViewById(R.id.toggleView).setVisibility(
            visible ? View.VISIBLE : View.GONE);
    }

}
