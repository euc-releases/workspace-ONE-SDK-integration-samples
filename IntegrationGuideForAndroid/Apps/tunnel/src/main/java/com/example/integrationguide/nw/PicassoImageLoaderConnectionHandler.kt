// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.content.Context
import android.widget.ImageView
import com.airwatch.gateway.clients.AWOkHttpClient
import com.airwatch.util.Logger.d
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class PicassoImageLoaderConnectionHandler(
    context: Context,
    imageView: ImageView?,
    listener: StatusListener? = null,
) :
    ConnectionHandler {

    private var context: WeakReference<Context> = WeakReference(context)
    private var imageView: WeakReference<ImageView> = WeakReference(imageView)
    private var listener: WeakReference<StatusListener> = WeakReference(listener)

    companion object {
        private val TAG: String = PicassoImageLoaderConnectionHandler::class.java.name
    }

    override fun connect(url: String) {
        getImageLoadingFromPicasso(url, listener.get())
    }

    /**
     * Integrates OkHttpClient as network stack for Picasso.
     * tested:"https://httpbin.org/image/jpeg"
     */
    private fun getImageLoadingFromPicasso(loadUrl: String, listener: StatusListener?) {

        context.get()?.let {
            val client = OkHttpClient()
            //can use
            val awOkhttpClient = AWOkHttpClient.copyWithDefaults(it, client, trustAllTrustManager)

            //Uses OkHttp3Downloader which consumes OkHttpClient
            val picasso = Picasso.Builder(it)
                .downloader(OkHttp3Downloader(awOkhttpClient))
                .build()

            //Load the required url into imageview. Added callback to get the status.
            picasso.load(loadUrl)
                .into(imageView.get(), object : Callback {
                    override fun onSuccess() {
                        val status = "Picasso callback: Success"
                        d(TAG, status)
                        listener?.onStatusUpdate(status)
                    }

                    override fun onError(e: Exception) {
                        val status = """
                 Picasso Error received. ${e.message}
                 Check if entered URL returns an Image.
                 Eg: https://httpbin.org/image/jpeg
                 """.trimIndent()
                        listener?.onStatusUpdate(status)
                        d(TAG, "$status ${e.message}")
                    }
                })
        }
    }

    private val trustAllTrustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    }

}