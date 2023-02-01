// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;

// This class is for subclass creation only.
@SuppressLint("Registered")
public class BaseActivity extends Activity {
    protected String[] uiTexts = null;
    private int uiTextIndex = 0;

    protected String[] generateUITexts() {
        final int uiMode = getResources().getConfiguration().uiMode;
        return new String[]{
            getResources().getString(R.string.ui_placeholder),
            wrappable(getResources().getString(R.string.MODULE_NAME)),
            (uiMode & Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES ? "Dark mode" : "Not dark mode"
        };
    }

    private String wrappable(String camelCased) {
        return camelCased.replaceAll("[A-Z]", " $0");
    }

    protected TextView configureTextView(int textViewID) {
        if (uiTexts == null) {
            uiTexts = generateUITexts();
        }
        final TextView textView = (TextView)findViewById(textViewID);
        textView.setText(uiTexts[0]);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiTextIndex = (uiTextIndex + 1) % uiTexts.length;
                ((TextView) view).setText(uiTexts[uiTextIndex]);
            }
        });
        return textView;
    }
    protected TextView configureTextView() {
        return configureTextView(R.id.textViewIntegration);
    }
}
