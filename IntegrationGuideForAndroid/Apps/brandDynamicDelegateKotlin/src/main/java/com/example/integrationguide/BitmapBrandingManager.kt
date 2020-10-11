// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.airwatch.login.branding.BrandingCallBack
import com.airwatch.login.branding.DefaultBrandingManager
import com.airwatch.simplifiedenrollment.views.AWNextActionView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// This is a singleton class. However, it can't use `object` instead of `class`
// because object constructors aren't allowed parameters.

open class BitmapBrandingManager private constructor (
    val defaultBrandingManager: DefaultBrandingManager
) : com.airwatch.login.branding.BrandingManager by defaultBrandingManager
{
    companion object {
        // Singleton business.
        private var instance: BitmapBrandingManager? = null
        fun getInstance(brandingManager: BrandingManager):BitmapBrandingManager
        {
            return instance ?:
            BitmapBrandingManager(brandingManager.defaultBrandingManager).also {
                instance = it
            }
        }
    }

    private val brandDate = Date()
    private val brandMessages get() = listOf(
        SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM).format(brandDate),
        SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(brandDate),
        defaultBrandingManager.primaryColor?.let {String.format(
            "#%02x%02x%02x", Color.red(it), Color.green(it), Color.blue(it)
        )} ?: "null"
    )
    // Following is shorter but toUInt was flagged as experimental.
    // colourInt?.toUInt()?.toString(radix = 16)?.padStart(6, '0')

    // Still, better than this:
    // val colourHex = colourInt?.let { listOf(
    //     Color.red(colourInt).toString(16).padStart(2, '0'),
    //     Color.green(colourInt).toString(16).padStart(2, '0'),
    //     Color.blue(colourInt).toString(16).padStart(2, '0')
    // ).joinToString(separator = "") }

    private val paint = Paint().apply {
        textAlign = Paint.Align.CENTER
    }

    private fun makeBitmap(title:String): Bitmap {
        val bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)

        val height = bitmap.height.toFloat()
        val width = bitmap.width.toFloat()
        val canvas = Canvas(bitmap)

        paint.style = Paint.Style.FILL
        paint.color = Color.LTGRAY
        canvas.drawRect(0F, 0F, width, height, paint)

        paint.color = Color.BLACK
        paint.textSize = height / 4F
        val x = width / 2F
        var y = paint.textSize
        canvas.drawText(title, x, y, paint)
        paint.textSize = height / 7F

        brandMessages.forEach {
            y += paint.textSize * 1.5F
            canvas.drawText(it, x, y, paint)
        }

        defaultBrandingManager.primaryColor?.also {
            paint.color = it
            paint.style = Paint.Style.STROKE
            val margin = 10F
            paint.strokeWidth = margin / 2F
            canvas.drawRect(
                margin, margin, width - margin, height - margin, paint)
        }

        return bitmap
    }

    override fun brandLoadingScreenLogo(callback: BrandingCallBack?) {
        callback?.onComplete(makeBitmap("Loading"))
    }

    override fun brandInputScreenLogo(callback: BrandingCallBack?) {
        callback?.onComplete(makeBitmap("Input"))
    }

    override fun getPrimaryColor(): Int? { return Color.RED }

    override fun applyBranding(nextActionView: AWNextActionView) {
        // Apply default overall.
        defaultBrandingManager.applyBranding(nextActionView)

        // Apply a specific override.
        nextActionView.setBackgroundColor(Color.RED)
    }

}
