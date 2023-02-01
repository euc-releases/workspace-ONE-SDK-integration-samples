// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
package com.example.integrationguide

import android.content.Context
import android.util.AttributeSet
import com.airwatch.gateway.clients.AWWebView
import com.airwatch.gateway.clients.AWWebViewClient

/**
 * @author Gaurav Arora Sep 5, 2014
 */
class TestWebView : AWWebView {
    private var mContext: Context
    private var mAWWebViewClient: AWWebViewClient? = null

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, arg1: AttributeSet?) : super(context, arg1) {
        mContext = context
        init()
    }

    constructor(context: Context, arg1: AttributeSet?, arg2: Int) : super(context, arg1, arg2) {
        mContext = context
        init()
    }

    private fun init() {
        mAWWebViewClient = AWWebViewClient(mContext)
        settings.userAgentString =
            settings.userAgentString + USER_SUFFIX
        settings.javaScriptEnabled = true
        webViewClient = mAWWebViewClient!!
    }

    fun callHttpAuthHandler(url: String?) {
        if (mAWWebViewClient != null) {
            mAWWebViewClient!!.onReceivedHttpAuthRequest(this, null, url, null)
        }
    }

    companion object {
        const val USER_SUFFIX = " AirWatch Browser"
    }
}