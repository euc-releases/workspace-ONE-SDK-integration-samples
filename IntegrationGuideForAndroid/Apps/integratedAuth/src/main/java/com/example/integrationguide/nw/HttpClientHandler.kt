// Copyright 2023 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import com.airwatch.gateway.auth.ApacheNTLMSchemeFactory
import com.airwatch.gateway.clients.AWAuthInterceptor
import com.airwatch.gateway.clients.AWCertAuthUtil
import com.airwatch.gateway.clients.AWHttpClient
import com.airwatch.util.Logger.e
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import cz.msebera.android.httpclient.client.config.AuthSchemes
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.SingleClientConnManager
import org.apache.http.params.HttpParams
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.Socket
import java.net.URL
import java.net.UnknownHostException
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class HttpClientHandler(listener: StatusListener? = null) : ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)

    companion object {
        private val TAG: String = HttpClientHandler::class.java.name
    }

    override fun connect(url: String) {
        invokeAWHttpClient(url, listener.get())
    }

//    private fun loadUrlInAwOkHttpClient(loadUrl: String, listener: StatusListener?) {
//        Thread {
//            try {
//                val client = OkHttpClient()
//                val request: Request = Request.Builder()
//                    .url(loadUrl)
//                    .build()
//                val response = AWOkHttpClient.newCall(client, request).execute()
//                val status = "Status code AWOkHttpClient : ${response.code}"
//                listener?.onStatusUpdate(status)
//                i(TAG, status)
//                response.body?.let {
//                    i(TAG, status)
//                    it.close()
//                }
//            } catch (e: Exception) {
//                val status = "Unable to send request AWOkHttpClient ${e.message}"
//                listener?.onStatusUpdate(status)
//                e(TAG, status)
//            }
//        }.start()
//    }


    /*
     * Default HTTP Client and AWHttpClient
     */
    private fun invokeAWHttpClient(url: String, listener: StatusListener?) {
        Thread { awHttpClientHandler(url, listener) }.start()
    }

    private fun invokeHttpClient(url: String, listener: StatusListener?) {
        Thread { httpClientHandler(url, listener) }.start()
    }

    //    private void awHttpClientHandler() {
    //        //The KeyManagers for SSL Client Cert IA are added in the AWTrustAllHttpClient implementation
    //        AWHttpClient httpclient = new AWTrustAllHttpClient(editView.getText().toString());
    //        httpclient.setCookieStore(new BasicCookieStore());
    //        try {
    //            HttpGet httpPost = new HttpGet(editView.getText().toString());
    //            final HttpResponse response = httpclient.execute(httpPost);
    //            final String responseCode = Integer.toString(response.getStatusLine().getStatusCode());
    //            showResultOnTextView(responseCode);
    //        } catch (Exception e) {
    //            Logger.e(TAG, e);
    //            showResultOnTextView("Error");
    //        } finally {
    //            httpclient.getConnectionManager().shutdown();
    //        }
    //    }

    private fun httpClientHandler(url: String, listener: StatusListener?) {
        //The KeyManagers for SSL Client Cert IA are added in the TrustAllHttpClient implementation
        val httpclient: DefaultHttpClient = TrustAllHttpClient(url)
        httpclient.cookieStore = BasicCookieStore()

        //Register the NTLMScheme for NTLM IA support
        httpclient.authSchemes.register(AuthSchemes.NTLM, ApacheNTLMSchemeFactory())
        httpclient.addRequestInterceptor(AWAuthInterceptor(false), 0)
        try {
            val httpPost: HttpGet = HttpGet(url)
            val response = httpclient.execute(httpPost)
            listener?.onStatusUpdate("${response.statusLine.statusCode}")
        } catch (e: java.lang.Exception) {
            e(TAG, "${e.message}", e)
        } finally {
            httpclient.connectionManager.shutdown()
        }
    }


    private fun awHttpClientHandler(url: String, listener: StatusListener?) {
        //The KeyManagers for SSL Client Cert IA are added in the AWTrustAllHttpClient implementation
        val httpclient: AWHttpClient = AWTrustAllHttpClient(url)
        httpclient.cookieStore = BasicCookieStore()
        try {
            val httpPost: HttpGet = HttpGet(url)
            val response = httpclient.execute(httpPost)
            listener?.onStatusUpdate("${response.statusLine.statusCode}")
        } catch (e: java.lang.Exception) {
            e(TAG, "${e.message}", e)
        } finally {
            httpclient.connectionManager.shutdown()
        }
    }

    private class TrustAllHttpClient(private val url: String) : DefaultHttpClient() {
        override fun createClientConnectionManager(): ClientConnectionManager {
            return getClientConnectionManager(params, url)
        }

        private fun getClientConnectionManager(
            params: HttpParams,
            url: String,
        ): ClientConnectionManager {
            val registry = SchemeRegistry()
            registry.register(Scheme("http", PlainSocketFactory.getSocketFactory(), 80))
            registry.register(Scheme("https", getTrustAllSslSocketFactory(), 443))
            if (url.trim { it <= ' ' }.startsWith("https")) {
                try {
                    val urlObj = URL(url.trim { it <= ' ' })
                    val customPort = urlObj.port
                    if (customPort != 443 && customPort != -1) {
                        registry.register(Scheme("https", getTrustAllSslSocketFactory(), customPort))
                    }
                } catch (e: MalformedURLException) {
                } //Ignore
            }
            return SingleClientConnManager(params, registry)
        }

        private fun getTrustAllSslSocketFactory(): SSLSocketFactory? {
            return try {
                val trustStore = KeyStore.getInstance("PKCS12")
                trustStore.load(null, null)
                val sslfactory = TrustAllSslSocketFactory(trustStore)
                sslfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                sslfactory
            } catch (e: java.lang.Exception) {
                e("Could not create All Trust SSL Socket Factory.", e)
                null
            }
        }
    }

    private class AWTrustAllHttpClient(private val url: String) : AWHttpClient() {
        override fun createClientConnectionManager(): ClientConnectionManager {
            return getClientConnectionManager(params, url)
        }

        private fun getClientConnectionManager(
            params: HttpParams,
            url: String,
        ): ClientConnectionManager {
            val registry = SchemeRegistry()
            registry.register(Scheme("http", PlainSocketFactory.getSocketFactory(), 80))
            registry.register(Scheme("https", getTrustAllSslSocketFactory(), 443))
            if (url.trim { it <= ' ' }.startsWith("https")) {
                try {
                    val urlObj = URL(url.trim { it <= ' ' })
                    val customPort = urlObj.port
                    if (customPort != 443 && customPort != -1) {
                        registry.register(Scheme("https", getTrustAllSslSocketFactory(), customPort))
                    }
                } catch (e: MalformedURLException) {
                } //Ignore
            }
            return SingleClientConnManager(params, registry)
        }

        private fun getTrustAllSslSocketFactory(): SSLSocketFactory? {
            return try {
                val trustStore = KeyStore.getInstance("PKCS12")
                trustStore.load(null, null)
                val sslfactory = TrustAllSslSocketFactory(trustStore)
                sslfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                sslfactory
            } catch (e: java.lang.Exception) {
                e("Could not create All Trust SSL Socket Factory.", e)
                null
            }
        }
    }

    private class TrustAllSslSocketFactory(truststore: KeyStore?) :
        SSLSocketFactory(truststore) {
        var sslContext = SSLContext.getInstance("TLS")

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(
            socket: Socket,
            host: String,
            port: Int,
            autoClose: Boolean,
        ): Socket {
            return sslContext.socketFactory.createSocket(socket, host, port, autoClose)
        }

        @Throws(IOException::class)
        override fun createSocket(): Socket {
            return sslContext.socketFactory.createSocket()
        }

        init {
            val clientCertAuthKeyManagers = AWCertAuthUtil.getCertAuthKeyManagers()

            val trustAllTrustManager: X509TrustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            }

            sslContext.init(clientCertAuthKeyManagers,
                arrayOf<TrustManager>(trustAllTrustManager),
                null)
        }

    }


}