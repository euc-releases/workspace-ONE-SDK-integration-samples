// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.webkit.WebView
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import java.lang.ref.WeakReference

class AwWebViewConnectionHandler(view: WebView?, listener: StatusListener? = null) :
    ConnectionHandler {

    private var webView: WeakReference<WebView> = WeakReference(view)
    private var listener: WeakReference<StatusListener> = WeakReference(listener)

    override fun connect(url: String) {
        loadUrlInAwWebView(url)
    }

    private fun loadUrlInAwWebView(loadUrl: String) {
        webView.get()?.loadUrl(loadUrl)
        listener.get()?.onStatusUpdate("URL loaded")
    }
}