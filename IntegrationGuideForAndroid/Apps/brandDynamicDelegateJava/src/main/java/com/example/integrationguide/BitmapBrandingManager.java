// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.airwatch.login.branding.BrandingCallBack;
import com.airwatch.login.branding.DefaultBrandingManager;
import com.airwatch.sdk.context.SDKContextManager;
import com.airwatch.sdk.context.awsdkcontext.SDKDataModelImpl;
import com.airwatch.simplifiedenrollment.views.AWInputField;
import com.airwatch.simplifiedenrollment.views.AWNextActionView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// This class is an example dynamic branding manager with real code. It should
// be a BrandingManager subclass but Java doesn't allow private constructors to
// be called, even from a subclass.

class BitmapBrandingManager
    implements com.airwatch.login.branding.BrandingManager
{
    // Helper function.
    @NotNull
    static DefaultBrandingManager createDefaultBrandingManager(
        android.app.Application application
    ) {
        return BrandingManager.createDefaultBrandingManager(application);
    }

    // Singleton business.
    private static BitmapBrandingManager instance = null;
    public static BitmapBrandingManager getInstance(
            android.app.Application application
    ) {
        if (instance == null) {
            instance = new BitmapBrandingManager(
                createDefaultBrandingManager(application));
        }
        return instance;
    }
    public static BitmapBrandingManager getInstance(
        @NotNull android.app.Activity activity
    ) {
        return getInstance(activity.getApplication());
    }
    public static BitmapBrandingManager getInstance() {
        return instance;
    }

    // Singleton constructor.
    private BitmapBrandingManager(
        DefaultBrandingManager defaultBrandingManager
    ) {
        this.defaultBrandingManager = defaultBrandingManager;

        loadBrand();
        paint.setTextAlign(Paint.Align.CENTER);
    }

    // Property, also used for delegation.
    private com.airwatch.login.branding.BrandingManager defaultBrandingManager;
    public com.airwatch.login.branding.BrandingManager
    getDefaultBrandingManager() {
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

    // Actual implementation.
    private Date brandDate = new Date();
    private String[] brandMessages;
    private Integer primaryColorInt = null;

    private Paint paint = new Paint();

    private void loadBrand() {
        primaryColorInt = defaultBrandingManager.getPrimaryColor();
        brandMessages = new String[]{
            SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM)
                .format(brandDate),
            SimpleDateFormat.getDateInstance(DateFormat.SHORT)
                .format(brandDate),
            primaryColorInt == null ? "null" : String.format(
                "#%02x%02x%02x",
                Color.red(primaryColorInt),
                Color.green(primaryColorInt),
                Color.blue(primaryColorInt)
            )
        };
    }

    private Bitmap makeBitmap(String title) {
        Bitmap bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);

        float height = (float)bitmap.getHeight();
        float width = (float)bitmap.getWidth();
        Canvas canvas = new Canvas(bitmap);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(0F, 0F, width, height, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(height / 4F);
        float x = width / 2F;
        float y = paint.getTextSize();
        canvas.drawText(title, x, y, paint);
        paint.setTextSize(height / 7F);

        loadBrand();
        for (String brandMessage : brandMessages) {
            y += paint.getTextSize() * 1.5F;
            canvas.drawText(brandMessage, x, y, paint);
        }

        if (primaryColorInt != null) {
            paint.setColor(primaryColorInt);
            paint.setStyle(Paint.Style.STROKE);
            float margin = 10F;
            paint.setStrokeWidth(margin / 2F);
            canvas.drawRect(
                margin, margin, width - margin, height - margin, paint);
        }

        return bitmap;
    }

    @Override
    public void brandLoadingScreenLogo(BrandingCallBack callback) {
        callback.onComplete(makeBitmap("Loading"));
    }

    @Override
    public void brandInputScreenLogo(BrandingCallBack callback) {
        callback.onComplete(makeBitmap("Input"));
    }

    @Override
    public Integer getPrimaryColor() { return Color.RED; }

    @Override
    public void applyBranding(AWNextActionView nextActionView) {
        // Apply default overall.
        defaultBrandingManager.applyBranding(nextActionView);

        // Apply a specific override.
        nextActionView.setBackgroundColor(Color.RED);
    }

}
