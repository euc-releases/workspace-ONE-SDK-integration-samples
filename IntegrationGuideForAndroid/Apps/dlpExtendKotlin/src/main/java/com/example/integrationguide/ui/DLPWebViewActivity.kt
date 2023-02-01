// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.airwatch.ui.widget.CopyEnabledWebView
import com.example.integrationguide.BaseActivity
import com.example.integrationguide.R
import com.example.integrationguide.SDKProfilePresentation

class DLPWebViewActivity : BaseActivity() {

    var ws1WebChromeClient:WebChromeClient? = null
    var plainWebChromeClient:WebChromeClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlp_webview)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_dlp_web_views)
        }

        SDKProfilePresentation.displayCopyPasteSettings(
            findViewById(R.id.policyOutbound),
            findViewById(R.id.policyInbound)
        )

        configureWebViews()
        configureAddressEntry()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebViews() {
        val ws1WebView = findViewById<CopyEnabledWebView>(R.id.ws1WebView)!!
        val plainWebView = findViewById<WebView>(R.id.plainWebView)!!
        val uiHolder = findViewById<ConstraintLayout>(R.id.uiHolder)!!

        listOf(ws1WebView, plainWebView).forEach {
            // Enable JS because a lot of websites don't really work without it.
            it.settings.javaScriptEnabled = true

            // Make the web view content scrollable by blocking interception of
            // scroll events in the parent hierarchy.
            it.setOnTouchListener(scrollTaker)

            // Set a WebViewClient so that navigation continues in the same web
            // view control instead of opening the browser app.
            //
            // TOTH https://stackoverflow.com/questions/29023299/webview-stop-from-opening-links-in-browser-and-keep-in-app#comment46297615_29024095
            it.webViewClient = android.webkit.WebViewClient()
        }

        // Associate each web view with a progress display via a WebChromeClient
        // subclass instance.
        ws1WebChromeClient = WebViewProgress(
            findViewById(R.id.ws1WebViewProgress),
            findViewById(R.id.ws1WebViewHolder),
            uiHolder
        ).also { ws1WebView.webChromeClient = it }

        plainWebChromeClient = WebViewProgress(
            findViewById(R.id.plainWebViewProgress),
            findViewById(R.id.plainWebViewHolder),
            uiHolder
        ).also { plainWebView.webChromeClient = it }
    }

    // TOTH for getting scroll events into a child View:
    // https://stackoverflow.com/a/11554823/7657675
    val scrollTaker = object : View.OnTouchListener {
        override fun onTouch(
            view: View?, event: MotionEvent?): Boolean
        {
            view?.parent?.requestDisallowInterceptTouchEvent(true)
            return false
        }
    }

    class WebViewProgress(
        private val progressBar: ProgressBar,
        private val holder: View,
        private val layout: ConstraintLayout
    ) : WebChromeClient()
    {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress

            // When the load is in progress, hide the web view and show the
            // progress bar. When loading finishes, show the web view and hide
            // the progress bar.
            if (newProgress >= 100) {
                progressBar.visibility = View.GONE
                view?.visibility = View.VISIBLE
                constrain(view?.rootView)
            }
            else {
                progressBar.visibility = View.VISIBLE
                view?.visibility = View.GONE
            }
        }

        private fun constrain(rootView: View?) {
            val maxHeight =  rootView?.measuredHeight?.times(0.7)?.toInt()
                ?: return

            // TOTH getting started with programmatic access to constraints
            // https://stackoverflow.com/a/44700437/7657675
            val constraintSet = ConstraintSet()
            constraintSet.clone(layout)
            constraintSet.constrainMaxHeight(holder.id, maxHeight)
            constraintSet.applyTo(layout)
        }
    }

    private fun configureAddressEntry() {
        findViewById<View>(R.id.webViewGoButton).setOnClickListener {
            loadWebViews(
                findViewById<EditText>(R.id.webViewAddressBar).text.toString())
        }
        findViewById<EditText>(R.id.webViewAddressBar).also {
            it.setOnEditorActionListener(goResponder)
            it.setOnFocusChangeListener { view:View, hasFocus:Boolean ->
                if (hasFocus) return@setOnFocusChangeListener
                view as EditText
                loadWebViews(view.text.toString())
            }
            it.setText(R.string.web_view_initial_url)
        }
        loadWebViews(getString(R.string.web_view_initial_url))
    }

    val goResponder = object : TextView.OnEditorActionListener {
        override fun onEditorAction(
            textView:TextView?, actionID:Int, keyEvent: KeyEvent?
        ): Boolean {
            // Not quite sure what the correct check should be on the keyEvent.
            // Checking for non-null seems to work.
            //
            // TOTH for the basics: https://stackoverflow.com/a/6832095/7657675
            if (
                actionID != EditorInfo.IME_ACTION_GO || keyEvent != null
            ) return false
            textView?.text?.toString()?.also {
                // Load the URL by forcing the EditText control to lose focus.
                // TOTH for isFocusableInTouchMode
                // https://stackoverflow.com/a/3234884/7657675
                findViewById<View>(R.id.webViewGoButton)?.run {
                    isFocusableInTouchMode = true
                    requestFocus()
                    return true
                }
            }
            return false
        }
    }

    private fun loadWebViews(urlString: String) {
        var uri = Uri.parse(urlString)
        if (uri.scheme == null || uri.authority == null) {
            val builder = uri.buildUpon()
            if (uri.scheme == null) builder.scheme("https")
            if (uri.authority == null) {
                builder.path(null)
                uri.pathSegments.forEachIndexed {
                        index:Int, segment:String ->
                    if (index == 0) {
                        builder.authority(segment)
                    }
                    else {
                        builder.appendPath(segment)
                    }
                }
            }
            uri = builder.build()
        }

        findViewById<CopyEnabledWebView>(R.id.ws1WebView).run {
            ws1WebChromeClient?.onProgressChanged(this, 0)
            loadUrl(uri.toString())
        }
        findViewById<WebView>(R.id.plainWebView).run {
            plainWebChromeClient?.onProgressChanged(this, 0)
            loadUrl(uri.toString())
        }
    }

}
