// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import com.airwatch.util.Logger.d
import android.widget.TextView
import android.widget.EditText
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.airwatch.login.SDKBaseActivity
import com.example.integrationguide.interfaces.StatusListener
import com.example.integrationguide.nw.PicassoImageLoaderConnectionHandler
import com.example.integrationguide.nw.RetrofitWithAWOKHttpClientConnectionHandler
import com.example.integrationguide.nw.VolleyWithAWOkHttpClientConnectionHandler

/*
 * From 19.6 SDK, its required to use AW Wrapped classes for making Http requests.
 * This is needed for authenticating the requests to the local proxy from Android 10 onwards.
 *
 * This activity provides sample code using AWOkHttpClient for REST APIs like Volley Retrofit and Picasso.
 * */
class AWClientExampleActivity : SDKBaseActivity(), View.OnClickListener, StatusListener {
    companion object{
        private val TAG = AWClientExampleActivity::class.java.simpleName
    }
    private var context: Context? = null
    private var statusMessage: TextView? = null
    private var imageView: ImageView? = null
    private var urlEditor: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aw_restapis_example_layout)
        context = this
        statusMessage = findViewById(R.id.status)
        urlEditor = findViewById(R.id.urleditor)
        imageView = findViewById(R.id.imageView)
        findViewById<View>(R.id.volleybtn).setOnClickListener(this)
        findViewById<View>(R.id.retrofitbtn).setOnClickListener(this)
        findViewById<View>(R.id.picassobtn).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        urlEditor?.isEnabled = true
        when (v.id) {
            R.id.volleybtn -> {
                if (TextUtils.isEmpty(urlEditor?.text)) {
                    Toast.makeText(context, "Please Enter URL", Toast.LENGTH_LONG).show()
                    return
                }
                var loadUrl = urlEditor?.text.toString()
                if (!loadUrl.startsWith("http://") && !loadUrl.startsWith("https://")) {
                    loadUrl = "http://$loadUrl"
                    urlEditor?.setText(loadUrl)
                }
                VolleyWithAWOkHttpClientConnectionHandler(this, this).connect(loadUrl)
            }
            R.id.retrofitbtn -> {
                urlEditor?.setText("https://reqres.in/")
                urlEditor?.isEnabled = false
                val loadUrl = urlEditor!!.text.toString()
                RetrofitWithAWOKHttpClientConnectionHandler(this, this).connect(loadUrl)
            }
            R.id.picassobtn -> {
                urlEditor?.setText("https://httpbin.org/image/jpeg")
                val loadUrl = urlEditor?.text.toString()
                PicassoImageLoaderConnectionHandler(this, imageView, this).connect(loadUrl)
            }
            else -> d(TAG, "No implementation defined.")
        }
    }

    override fun onStatusUpdate(status: String) {
        runOnUiThread { statusMessage!!.text = status }
    }
}