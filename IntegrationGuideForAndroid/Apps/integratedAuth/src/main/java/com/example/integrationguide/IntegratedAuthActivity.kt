// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
package com.example.integrationguide

import android.app.AlertDialog
import com.airwatch.util.Logger.d
import com.airwatch.util.Logger.e
import android.widget.TextView
import android.widget.EditText
import com.airwatch.gateway.clients.AWWebView
import android.webkit.WebView
import android.widget.LinearLayout
import android.os.Bundle
import com.airwatch.gateway.clients.AWWebViewClient
import android.webkit.SslErrorHandler
import android.net.http.SslError
import android.content.DialogInterface
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.airwatch.login.SDKBaseActivity
import com.airwatch.sdk.AirWatchSDKException
import com.airwatch.sdk.SDKManager
import com.airwatch.sdk.certificate.CertificateFetchResult
import com.airwatch.sdk.certificate.CertificateFetchResult.Companion.CERT_FETCH_EXCEPTION
import com.airwatch.sdk.certificate.CertificateFetchResult.Companion.INVALID_SCEP_STATUS
import com.airwatch.sdk.certificate.CertificateFetchResult.Companion.MAX_RETRY_COUNT_EXCEEDED
import com.airwatch.sdk.certificate.CertificateFetchResult.Companion.RETRY_TIMEOUT_NOT_ELAPSED_RETRY_SCHEDULED
import com.airwatch.sdk.certificate.CertificateFetchResult.Companion.SCEP_INSTRUCTIONS_UNAVAILABLE
import com.airwatch.sdk.certificate.CertificateFetchResult.Companion.NO_ERROR
import com.airwatch.sdk.certificate.SCEPCertificateFetchListener
import com.airwatch.sdk.certificate.SCEPCertificateFetcher
import com.airwatch.sdk.certificate.SCEPContext
import com.airwatch.sdk.configuration.SDKConfigurationKeys
import com.airwatch.sdk.context.SDKContextManager
import com.airwatch.storage.SDKKeyStoreUtils
import com.airwatch.storage.SDKSecurePreferencesKeys.IA_CERT_ALIAS
import com.example.integrationguide.interfaces.StatusListener
import com.example.integrationguide.nw.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.lang.StringBuilder
import java.security.KeyStore
import java.security.cert.X509Certificate

/**
 * Activity to showcase NTLM and SSL Client Cert Authentication features for various
 * HTTP Clients and the WebView. This class contains code examples for the default HTTP clients
 * and the AW wrapper classes
 */
class IntegratedAuthActivity : SDKBaseActivity(), View.OnClickListener, StatusListener {
    private var textView: TextView? = null
    private var editView: EditText? = null
    private var mAwWebView: AWWebView? = null
    private var mWebView: WebView? = null
    private var iaEditText: EditText? = null
    private var credentialEditText: EditText? = null
    private var certificateEditText: EditText? = null
    var httpClientBtn: Button? = null
    var httpUrlConnectionBtn: Button? = null
    var okhttpClientBtn: Button? = null
    var webViewBtn: Button? = null
    var basicAuthBtn: Button? = null
    var rootLayout: LinearLayout? = null
    private val certStatusListener =
        SCEPCertificateFetchListener { certificateFetchResult: CertificateFetchResult ->
            handleResult(certificateFetchResult)
        }
    private val certificateFetcher = SCEPContext.getInstance().scepCertificateFetcher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ntlm_and_basic_auth)
        httpClientBtn = findViewById(R.id.httpclient_btn)
        webViewBtn = findViewById(R.id.webview_btn)
        httpUrlConnectionBtn = findViewById(R.id.httpurlconnection_btn)
        okhttpClientBtn = findViewById(R.id.okhttp_btn)
        rootLayout = findViewById(R.id.root_view)
        httpClientBtn?.setOnClickListener(this)
        httpUrlConnectionBtn?.setOnClickListener(this)
        webViewBtn?.setOnClickListener(this)
        okhttpClientBtn?.setOnClickListener(this)
        textView = findViewById(R.id.response_code)
        editView = findViewById(R.id.edit_text)
        mAwWebView = findViewById(R.id.web_view)
        basicAuthBtn = findViewById(R.id.basicauth)
        basicAuthBtn?.setOnClickListener(this)
        iaEditText = findViewById(R.id.integrated_authentication)
        credentialEditText = findViewById(R.id.use_enrollment_cred)
        certificateEditText = findViewById(R.id.use_certificate)
        mAwWebView?.setWebViewClient(object : AWWebViewClient(this) {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }
        })
        mWebView = WebView(this)
        mWebView!!.tag = "webview"
        mWebView!!.webViewClient = object : AWWebViewClient(this) {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }
        }
        if (rootLayout?.findViewById<View?>(R.id.web_view) == null) {
            rootLayout?.addView(mAwWebView)
        }
        certificateFetcher.registerFetchListener(certStatusListener)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val maxCount =
            pref.getInt(SCEP_MAX_COUNT_KEY, SCEPCertificateFetcher.DEFAULT_MAX_RETRY_COUNT)
        val retryInterval = pref.getInt(SCEP_RETRY_INTERVAL_KEY,
            SCEPCertificateFetcher.DEFAULT_RETRY_INTERVAL_SECONDS)
        certificateFetcher.setMaxRetryCount(maxCount)
        certificateFetcher.setRetryIntervalSeconds(retryInterval)
        updatePolicySettings()
    }

    override fun onResume() {
        super.onResume()
        if (certificateFetcher.isSCEPCertificatePending) {
            certificateFetcher.triggerPolling()
        }
    }

    override fun onPause() {
        super.onPause()
        pauseSCEPCertificatePolling()
    }

    override fun onDestroy() {
        super.onDestroy()
        certificateFetcher.unregisterFetchListener(certStatusListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.add(SCEP_SETTINGS_MENU)
        menu.add(SCEP_PAUSE_POLLING)
        menu.add(LOG_CERT_DETAILS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == SCEP_SETTINGS_MENU) {
            showSCEPSettings()
            return true
        } else if (item.title == SCEP_PAUSE_POLLING) {
            pauseSCEPCertificatePolling()
            return true
        } else if (item.title == LOG_CERT_DETAILS) {
            logCertDetails()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logCertDetails() {
        try {
            val keyStore = SDKKeyStoreUtils().getPKCS12(
                SDKContextManager.getSDKContext().sdkSecurePreferences.getString(IA_CERT_ALIAS,
                    "")!!)
            var certificates: Array<X509Certificate>? = null
            val sb = StringBuilder()
            val e = keyStore!!.aliases()
            while (e.hasMoreElements()) {
                val alias = e.nextElement()
                if (keyStore.isKeyEntry(alias)) {
                    val entry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
                    certificates = entry.certificateChain as Array<X509Certificate>
                    break
                }
            }
            if (certificates != null) {
                sb.append("Subject: ").append(certificates[0].subjectDN).append('\n')
                sb.append("Issuer: ").append(certificates[0].issuerDN).append('\n')
                if (certificates[0].subjectAlternativeNames == null) {
                    sb.append("SAN: null").append('\n')
                } else {
                    sb.append("SAN: not null").append('\n')
                    for (san in certificates[0].subjectAlternativeNames) {
                        sb.append("SAN entry: ").append(san).append('\n')
                        for (item in san) {
                            sb.append("SAN entry component: ").append(item).append('\n')
                            if (item is ByteArray) {
                                sb.append("SAN entry byte[] component: ").append(String(item))
                                    .append('\n')
                            }
                        }
                    }
                }
            } else {
                sb.append("No certs found")
            }
            d(TAG, "Cert details: $sb")
            showSnackbar("Cert details: $sb")
        } catch (ex: Exception) {
            e(TAG, "Error retrieving cert details", ex)
            showSnackbar("Error retrieving cert details: " + ex.message)
        }
    }

    private fun showSCEPSettings() {
        val scepDialog = AlertDialog.Builder(this)
        scepDialog.setTitle(SCEP_SETTINGS_MENU)
        scepDialog.setCancelable(true)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val maxCount =
            pref.getInt(SCEP_MAX_COUNT_KEY, SCEPCertificateFetcher.DEFAULT_MAX_RETRY_COUNT)
        val retryInterval = pref.getInt(SCEP_RETRY_INTERVAL_KEY,
            SCEPCertificateFetcher.DEFAULT_RETRY_INTERVAL_SECONDS)
        val tvMaxCount = TextView(this)
        tvMaxCount.text = "Max Retry Count:"
        val etMaxCount = EditText(this)
        etMaxCount.hint = "Max Retry Count"
        etMaxCount.setText(Integer.toString(maxCount))
        val tvRetryInterval = TextView(this)
        tvRetryInterval.text = "Retry Interval:"
        val etRetryInterval = EditText(this)
        etRetryInterval.hint = "Retry Interval"
        etRetryInterval.setText(Integer.toString(retryInterval))
        val dialogLayout = LinearLayout(this)
        dialogLayout.orientation = LinearLayout.VERTICAL
        dialogLayout.addView(tvMaxCount)
        dialogLayout.addView(etMaxCount)
        dialogLayout.addView(tvRetryInterval)
        dialogLayout.addView(etRetryInterval)
        scepDialog.setView(dialogLayout)
        scepDialog.setPositiveButton("Save") { dialog: DialogInterface?, whichButton: Int ->
            val changedMaxCount = etMaxCount.text.toString().trim { it <= ' ' }
                .toInt()
            val changedRetryInterval = etRetryInterval.text.toString().trim { it <= ' ' }.toInt()
            pref.edit().putInt(SCEP_MAX_COUNT_KEY, changedMaxCount)
                .putInt(SCEP_RETRY_INTERVAL_KEY, changedRetryInterval)
                .commit()
        }
        scepDialog.show()
    }

    private fun pauseSCEPCertificatePolling() {
        val cancelled = certificateFetcher.pausePolling()
        if (cancelled) {
            showSnackbar("Paused SCEP Certificate polling")
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    override fun onClick(v: View) {
        textView!!.text = ""

        //Clear the WebViews
        mAwWebView!!.loadUrl("about:blank")
        mWebView!!.loadUrl("about:blank")
        when (v.id) {
            R.id.httpurlconnection_btn -> AwUrlConnectionHandler(this, this).connect(
                editView!!.text.toString())
            R.id.httpclient_btn -> AwHttpClientHandler(this).connect(editView!!.text.toString())
            R.id.webview_btn -> AwWebViewHandler(mAwWebView,
                null).connect(editView!!.text.toString())
            R.id.okhttp_btn -> AwOkHttpClientHandler(this, this).connect(editView!!.text.toString())
            R.id.basicauth -> AwBasicAuthHandler(this, this).connect(editView!!.text.toString())
        }
    }

    /*
     * Private utility functions
     */
    private fun showResultOnTextView(responseCode: String) {
        runOnUiThread { textView!!.text = responseCode }
    }

    override fun onStatusUpdate(status: String) {
        showResultOnTextView(status)
    }

    private fun handleResult(result: CertificateFetchResult) {
        when (result.status) {
            CertificateFetchResult.Status.SUCCESS -> showSnackbar("SCEP certificate fetch succeeded")
            CertificateFetchResult.Status.FAILURE -> {
                val errorString = getErrorString(result.errorCode)
                showSnackbar("SCEP certificate fetch failed. $errorString")
            }
            CertificateFetchResult.Status.PENDING -> {
                val messageString = getErrorString(result.errorCode)
                val retryDataModel = result.pendingRetryDataModel
                val retryMessage =
                    "Attempts remaining: " + retryDataModel!!.retryAttemptsRemaining +
                            ". Time remaining for next retry: " + retryDataModel.timeRemainingForNextRetryAttempt
                showSnackbar("$messageString $retryMessage")
            }
        }
    }

    private fun getErrorString(code: Int): String {
        return when (code) {
            SCEP_INSTRUCTIONS_UNAVAILABLE -> "SCEP instructions unavailable."
            CERT_FETCH_EXCEPTION -> "Exception occurred while fetching SCEP certificate."
            INVALID_SCEP_STATUS -> "Invalid SCEP status."
            MAX_RETRY_COUNT_EXCEEDED -> "Max retry count exceeded."
            RETRY_TIMEOUT_NOT_ELAPSED_RETRY_SCHEDULED -> "Retry timeout not elapsed yet. Retry has been scheduled."
            NO_ERROR -> "Server returned the status as 'Pending'."
            else -> "Unknown error."
        }
    }

    private fun updateUiState(isIntegratedAuthEnabled: Boolean) {
        runOnUiThread {
            val isEnabled = if (isIntegratedAuthEnabled) R.string.enabled else R.string.disabled
            iaEditText!!.setText(resources.getString(isEnabled))
            credentialEditText!!.setText(isUserEnrollmentCredentialsEnabled)
            certificateEditText!!.setText(isUseCertificateEnabled)
        }
    }

    private fun updatePolicySettings() {
        object : Thread() {
            override fun run() {
                try {
                    val manager = SDKManager.init(applicationContext)
                    val value = manager.integratedAuthenticationProfile.integratedAuthEnabled
                    updateUiState(value == 1)
                } catch (e: AirWatchSDKException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private val isUseCertificateEnabled: String
        private get() {
            val id =
                if (isEnabled(SDKConfigurationKeys.USE_CERTIFICATE)) R.string.enabled else R.string.disabled
            return resources.getString(id)
        }
    private val isUserEnrollmentCredentialsEnabled: String
        private get() {
            val id =
                if (isEnabled(SDKConfigurationKeys.USE_ENROLL_CREDS)) R.string.enabled else R.string.disabled
            return resources.getString(id)
        }

    private fun isEnabled(type: String): Boolean {
        val settings = SDKContextManager.getSDKContext().sdkConfiguration
        return settings.getBooleanValue(SDKConfigurationKeys.TYPE_PASSCODE_POLICY, type)
    }

    companion object {
        private const val TAG = "IntegratedAuthActivity"
        private const val SCEP_SETTINGS_MENU = "SCEP Settings"
        private const val SCEP_PAUSE_POLLING = "Pause Polling"
        private const val LOG_CERT_DETAILS = "Log Cert Details"
        private const val SCEP_MAX_COUNT_KEY = "max_count"
        private const val SCEP_RETRY_INTERVAL_KEY = "retry_interval"
    }
}