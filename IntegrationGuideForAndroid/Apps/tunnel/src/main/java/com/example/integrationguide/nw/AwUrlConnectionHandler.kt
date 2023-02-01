// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.util.Log
import com.airwatch.gateway.clients.AWUrlConnection
import com.airwatch.util.Logger.e
import com.airwatch.util.Logger.i
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AwUrlConnectionHandler(listener: StatusListener? = null) : ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)

    companion object {
        private val TAG: String = AwUrlConnectionHandler::class.java.name
    }

    override fun connect(url: String) {
        loadUrlInAwUrlConnection(url, listener.get())
    }

    private fun loadUrlInAwUrlConnection(loadUrl: String, listener: StatusListener?) {
        Thread(object : Runnable {
            override fun run() {
                try {
                    val url = URL(loadUrl)
                    val connection = AWUrlConnection.openConnection(url)

                    val urlConnection =
                        if (connection is HttpURLConnection) connection else connection as HttpsURLConnection
                    val status = "AWUrlConnection : ${urlConnection.responseCode}"

                    urlConnection.instanceFollowRedirects = true
                    listener?.onStatusUpdate(status)
                    logResponse(connection.inputStream)
                    i(TAG, status)
                } catch (e: Exception) {
                    listener?.onStatusUpdate("Connection failed ${e.message}")
                    e(TAG, "Unable to send request URLConnection")
                }
            }

            @Throws(IOException::class)
            private fun logResponse(inputStream: InputStream) {
                val sb = StringBuilder()
                val buf = ByteArray(128)
                while (inputStream.read(buf) != -1) {
                    sb.append(String(buf))
                }
                Log.e("AirWatch", "AWHttpURLConnection resp: $sb")
            }
        }).start()
    }

}