// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airwatch.sdk.context.SDKContext;
import com.airwatch.sdk.context.SDKContextManager;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();

        toastHere(configureStatus());
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
