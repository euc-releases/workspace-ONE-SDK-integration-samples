// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.airwatch.login.branding.BrandingCallBack;
import com.airwatch.login.branding.DefaultBrandingManager;
import com.airwatch.simplifiedenrollment.views.AWInputField;
import com.airwatch.simplifiedenrollment.views.AWNextActionView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// This class is an example dynamic branding manager with real code. It should
// be a BrandingManager subclass but Java doesn't allow private constructors to
// be called, even from a subclass.

class BitmapBrandingManager
    implements com.airwatch.login.branding.BrandingManager
{
    // Singleton business.
    private static BitmapBrandingManager instance = null;
    public static BitmapBrandingManager getInstance(
        BrandingManager brandingManager
    ) {
        if (instance == null) {
            instance = new BitmapBrandingManager(
                brandingManager.getDefaultBrandingManager()
            );
        }
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

    private Bitmap makeBitmap(String title, int viewWidth, int viewHeight) {
        // The returned bitmap is always square.
        // The length of the sides will be the minimum of the specified width and height, but no more than 600 pixels,
        // and no less than 80 pixels, (80 <= bitmapDimension <= 600)
        int specifiedSize = min(min(viewWidth, viewHeight), 600);
        int bitmapDimension = max(specifiedSize, 80);

        Bitmap bitmap = Bitmap.createBitmap(bitmapDimension, bitmapDimension, Bitmap.Config.ARGB_8888);

        float height = (float)bitmap.getHeight();
        float width = (float)bitmap.getWidth();
        Canvas canvas = new Canvas(bitmap);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        paint.setAntiAlias(true);
        canvas.drawRect(0F, 0F, width, height, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(height / 4.5F);
        float x = width / 2F;
        float y = getTextHeight(paint);
        canvas.drawText(title, x, y, paint);
        paint.setTextSize(height / 8F);

        loadBrand();
        float subStringHeight = getTextHeight(paint);
        for (String brandMessage : brandMessages) {
            y += subStringHeight * 1.5F;
            canvas.drawText(brandMessage, x, y, paint);
        }

        if (primaryColorInt != null) {
            paint.setColor(primaryColorInt);
            paint.setStyle(Paint.Style.STROKE);
            float margin = min(10F, bitmapDimension/15f);
            paint.setStrokeWidth(margin / 2F);
            canvas.drawRect(
                margin, margin, width - margin, height - margin, paint);
        }

        return bitmap;
    }

    private Bitmap makeBitmap(String title) {
        return makeBitmap(title, 600, 600);
    }

    private float getTextHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    @Override
    public void brandLoadingScreenLogo(BrandingCallBack callback, int maxWidth, int maxHeight) {
        callback.onComplete(makeBitmap("Loading", maxWidth, maxHeight));
    }

    @Override
    public void brandLoadingScreenLogo(BrandingCallBack callback) {
        callback.onComplete(makeBitmap("Loading"));
    }

    @Override
    public void brandInputScreenLogo(BrandingCallBack callback, int maxWidth, int maxHeight) {
        callback.onComplete(makeBitmap("Input", maxWidth, maxHeight));
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
