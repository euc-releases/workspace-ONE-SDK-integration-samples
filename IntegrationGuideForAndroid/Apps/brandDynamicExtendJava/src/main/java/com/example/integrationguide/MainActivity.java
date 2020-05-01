// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.airwatch.sdk.context.SDKContext;
import com.airwatch.sdk.context.SDKContextManager;

public class MainActivity extends BaseActivity {
    static String channelID = "CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextView();

        final NotificationChannel channel = new NotificationChannel(
            channelID, "Channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Diagnostic channel");
        final NotificationManager manager = getApplicationContext()
            .getSystemService(NotificationManager.class);
        assert manager != null;
        manager.createNotificationChannel(channel);

        findViewById(R.id.imageViewAppLogo).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationCompat.Builder notification =
                        new NotificationCompat.Builder(
                            MainActivity.this, channelID)
                            .setSmallIcon(R.drawable.brand_logo_onecolour)
                            .setContentTitle("Tapped")
                            .setContentText("App Logo was tapped.")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat manager =
                        NotificationManagerCompat.from(MainActivity.this);
                    manager.notify(1, notification.build());
                }
            }
        );

        final SDKContext sdkContext = SDKContextManager.getSDKContext();
        toastHere("SDK state:" + sdkContext.getCurrentState().name() + ".");

        BrandingManager.getInstance(this).getDefaultBrandingManager()
            .brandLoadingScreenLogo( bitmap -> {
                ((ImageView)findViewById(R.id.imageViewEnterpriseLogo))
                    .setImageBitmap(bitmap);
            });
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
