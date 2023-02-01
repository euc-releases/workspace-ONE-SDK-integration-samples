// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.content.Context
import com.airwatch.gateway.clients.AWCertAuthUtil
import com.airwatch.gateway.clients.AWUrlConnection
import com.airwatch.gateway.clients.NtlmHttpURLConnection
import com.airwatch.util.Logger.e
import com.airwatch.util.Logger.i
import com.airwatch.util.Logger.d
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.ref.WeakReference
import java.net.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class AwUrlConnectionHandler(context: Context, listener: StatusListener? = null) :
    ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)
    private var context: WeakReference<Context> = WeakReference(context)

    companion object {
        private val TAG: String = AwUrlConnectionHandler::class.java.name
    }

    override fun connect(url: String) {
        invokeAWHttpUrlConnection(url, listener.get())
    }

    /*
     * AWUrlConnection
     */
    private fun invokeAWHttpUrlConnection(loadUrl: String, listener: StatusListener?) {
        Thread {
            context.get()?.let {
                d(TAG, "awHttpUrlConnectionHandler ")
                awHttpUrlConnectionHandler(it, loadUrl, listener)
            }
        }.start()
    }

    private fun awHttpUrlConnectionHandler(
        context: Context,
        loadUrl: String,
        listener: StatusListener?,
    ) {
        var ntlmHttpURLConnection: NtlmHttpURLConnection? = null
        try {
            // Create a trust manager that does not validate certificate chains
            allowSelfSigned()
            val url = URL(loadUrl)
            val cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)
            CookieHandler.setDefault(cookieManager)
            val httpURLConnection = AWUrlConnection.openConnection(url) as HttpURLConnection
            if (url.protocol.equals("https", ignoreCase = true)) {
                val sslContext = SSLContext.getInstance("TLS")

                //Get KeyManagers from AWCertAuthUtil.getCertAuthKeyManagers() for SSL Client Cert IA
                sslContext.init(
                    AWCertAuthUtil.getCertAuthKeyManagers(),
                    arrayOf<TrustManager>(trustAllTrustManager),
                    SecureRandom())
                (httpURLConnection as HttpsURLConnection?)!!.sslSocketFactory =
                    sslContext.socketFactory
            }

            //Decorate any HttpUrlConnection with NtlmHttpUrlConnection to get Basic and NTLM auth.
            ntlmHttpURLConnection = NtlmHttpURLConnection(context, httpURLConnection)
            ntlmHttpURLConnection.useCaches = false
            ntlmHttpURLConnection.connectTimeout = 15000
            ntlmHttpURLConnection.connect()
            val responseCode = ntlmHttpURLConnection.responseCode
            listener?.onStatusUpdate(responseCode.toString())
        } catch (e: Exception) {
            listener?.onStatusUpdate("Error")
            e(TAG, "${e.message}", e)
        }
        try {
            // If any response body is returned with a 400 status code, URLConnection provides this
            // body in the getErrorStream() method instead of getInputStream().
            if (ntlmHttpURLConnection!!.responseCode >= 400) {
                logResponse(ntlmHttpURLConnection.errorStream)
            } else {
                logResponse(ntlmHttpURLConnection.inputStream)
            }
        } catch (e: Exception) {
            e(TAG, "${e.message}", e)
        } finally {
            ntlmHttpURLConnection?.disconnect()
        }
    }

    private var trustAllTrustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    }

    @Throws(IOException::class)
    private fun logResponse(inputStream: InputStream) {
        val sb = StringBuilder()
        val buf = ByteArray(128)
        while (inputStream.read(buf) != -1) {
            sb.append(String(buf))
        }
        i(TAG, "AwHttpUrlConnection response: $sb")
    }

    private fun allowSelfSigned() {
        // Install the all-trusting trust manager
        try {
            val sc = SSLContext.getInstance("SSL")
            sc.init(null,
                arrayOf<TrustManager>(trustAllTrustManager),
                SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e(TAG, "${e.message}", "error in SSL context init")
        }
        // Create all-trusting host name verifier
        val allHostsValid =
            HostnameVerifier { hostname, session -> true }
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }

}