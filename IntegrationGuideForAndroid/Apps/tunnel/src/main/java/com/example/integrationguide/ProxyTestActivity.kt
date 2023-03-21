// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.airwatch.gateway.GatewayException
import com.airwatch.gateway.GatewayManager
import com.airwatch.gateway.GatewayStatusCodes
import com.airwatch.gateway.IGatewayStatusListener
import com.airwatch.gateway.config.GatewayConfigManager
import com.airwatch.gateway.enums.ProxySetupType
import com.airwatch.login.SDKBaseActivity
import com.airwatch.sdk.configuration.SDKConfigurationKeys
import com.airwatch.sdk.context.SDKContextManager
import com.airwatch.util.Logger.d
import com.airwatch.util.Logger.e
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.LogsUpdateListener
import com.example.integrationguide.interfaces.StatusListener
import com.example.integrationguide.logs.LoggerService
import com.example.integrationguide.logs.LogsContainer
import com.example.integrationguide.nw.*

class ProxyTestActivity : SDKBaseActivity(), View.OnClickListener, StatusListener {
    private var mEditUrl: EditText? = null
    private var mTextLogs: TextView? = null
    private var mTextStatus: TextView? = null
    private var mLogsScrollView: ScrollView? = null
    var mWebViewHolder: RelativeLayout? = null
    private var mAwWebView: TestWebView? = null
    private var context: Context? = null
    private val mGatewayManager: GatewayManager? = null
    private var mProgressDialog: ProgressDialog? = null
    private var imageView: ImageView? = null
    private val mTestComponentArray = arrayOf("AWWebView",
        "AWUrlConnection",
        "AWOkHttpClient",
        "Volley using AWOkHttpClient",
        "Retrofit using AWOkHttpClient",
        "Picasso using AWOkHttpClient")
    private var mTestComponentAdapter: ArrayAdapter<String>? = null
    private var mSpTestComponent: Spinner? = null
    private var mConnectionHandler: ConnectionHandler? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mProgressDialog?.dismiss()
        }
    }

    private fun reset() {
        mEditUrl?.setText("")
        mEditUrl?.isEnabled = true
        mConnectionHandler = null
    }

    private val onItemSelectedListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                reset()
                when (position) {
                    0 -> {
                        imageView?.visibility = View.GONE
                        mAwWebView?.visibility = View.VISIBLE
                        mConnectionHandler = AwWebViewConnectionHandler(mAwWebView)
                    }
                    1 -> mConnectionHandler = AwUrlConnectionHandler(this@ProxyTestActivity)
                    2 -> mConnectionHandler =
                        AwOkHttpClientConnectionHandler(this@ProxyTestActivity)
                    3 -> mConnectionHandler =
                        VolleyWithAWOkHttpClientConnectionHandler(this@ProxyTestActivity, this@ProxyTestActivity)
                    4 -> {
                        mEditUrl?.isEnabled = false
                        mEditUrl?.setText(resources.getString(R.string.sample_url))
                        mConnectionHandler =
                            RetrofitWithAWOKHttpClientConnectionHandler(this@ProxyTestActivity,
                                this@ProxyTestActivity)
                    }
                    5 -> {
                        imageView?.visibility = View.VISIBLE
                        mAwWebView?.visibility = View.GONE
                        mEditUrl?.setText("https://httpbin.org/image/jpeg")
                        mConnectionHandler =
                            PicassoImageLoaderConnectionHandler(this@ProxyTestActivity, imageView)
                    }
                    else -> mTextStatus?.text = resources.getString(R.string.invalid_selection)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_ERROR)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter(intentFilter))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.proxy_activity)
        context = this
        LogsContainer.getInstance().setLogsUpdateListener(mLogsListener)
        initViews()
        LoggerService.startService(applicationContext)
        if (isTunnellingEnabled) {
            setupProxy()
        }
        updateUiState()
    }

    private val isTunnellingEnabled: Boolean
        private get() = SDKContextManager.getSDKContext().sdkConfiguration.getBooleanValue(
            SDKConfigurationKeys.TYPE_APP_TUNNELING,
            SDKConfigurationKeys.ENABLE_APP_TUNNEL)

    /**
     * this is where we start the proxy manager that handle the proxy setup.
     */
    private fun setupProxy() {
        try {
            val gatewayManager = GatewayManager.getInstance(applicationContext)
            if (!gatewayManager.isRunning) {
                launchRingDialog()
            } else {
                mTextStatus?.text = resources.getString(R.string.proxy_started)
            }
            gatewayManager.registerGatewayStatusListener(ProxyListener())
            gatewayManager.autoConfigureProxy()
        } catch (e: GatewayException) {
            e(TAG, "Getting exception while creating proxy " + e.message)
        }
    }

    private fun initViews() {
        mAwWebView = findViewById<View>(R.id.act_main_test_result_wv) as TestWebView
        mWebViewHolder = findViewById<View>(R.id.lyt_webview_holder) as RelativeLayout
        if (mWebViewHolder?.findViewById<View?>(R.id.act_main_test_result_wv) == null) {
            mWebViewHolder?.addView(mAwWebView)
        }
        findViewById<View>(R.id.act_main_start_test_btn).setOnClickListener(this)
        findViewById<View>(R.id.clear_logs).setOnClickListener(this)
        imageView = findViewById(R.id.imageView)
        mEditUrl = findViewById<View>(R.id.url_edit_text) as EditText
        mEditUrl?.setText("")
        mSpTestComponent = findViewById<View>(R.id.act_main_test_component_sp) as Spinner
        setupSpinners()
        mTextLogs = findViewById<View>(R.id.logs_text_view) as TextView
        mTextStatus = findViewById<View>(R.id.text_status_code) as TextView
        mLogsScrollView = findViewById<View>(R.id.logs_scroll_view) as ScrollView
    }

    private fun setupSpinners() {
        mTestComponentAdapter = ArrayAdapter(applicationContext,
            android.R.layout.simple_spinner_item,
            mTestComponentArray)
        mTestComponentAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
        mSpTestComponent?.adapter = mTestComponentAdapter
        mSpTestComponent?.onItemSelectedListener = onItemSelectedListener
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.act_main_start_test_btn -> {
                if (TextUtils.isEmpty(mEditUrl?.text)) {
                    Toast.makeText(this, "Please Enter URL", Toast.LENGTH_LONG).show()
                    return
                }
                var loadUrl = mEditUrl?.text.toString()
                if (!loadUrl.startsWith("http://") && !loadUrl.startsWith("https://")) {
                    loadUrl = "http://$loadUrl"
                    mEditUrl?.setText(loadUrl)
                }
                LogsContainer.getInstance()
                    .setAllowLogs(true) // Allow logs just before loading the url.
                mConnectionHandler?.connect(loadUrl)
                mTextStatus?.text = resources.getString(R.string.status)
            }
            R.id.clear_logs -> {
                LogsContainer.getInstance().setAllowLogs(false)
                LogsContainer.getInstance().clearLogs()
                mTextLogs?.text = resources.getString(R.string.no_logs)
                mTextStatus?.text = resources.getString(R.string.status)
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        try {
            mGatewayManager?.stop()
        } catch (e: GatewayException) {
            e.printStackTrace()
        }
        LogsContainer.getInstance().clearLogs()
        LogsContainer.getInstance().unregisterListener()
        LoggerService.stopService(applicationContext)
        super.onDestroy()
    }

    inner class ProxyListener : IGatewayStatusListener {
        override fun onError(magErrorCode: Int) {
            d(TAG, "Airwatch::reportMAGError() - errorCode -$magErrorCode")

            runOnUiThread {
                mProgressDialog?.dismiss()
                mTextStatus?.text = "Failed"
                updateUiState()
            }
        }

        override fun onStatusChange(gatewayStatus: Int) {
            when (gatewayStatus) {
                GatewayStatusCodes.STATE_CONFIGURING, GatewayStatusCodes.STATE_ESTABLISHING_CONNECTION -> d(
                    TAG,
                    "Airwatch::onProxyStateChange() state-$gatewayStatus")
                GatewayStatusCodes.STATE_STARTED -> onProxyStartComplete()
                GatewayStatusCodes.STATE_STOPPED -> onProxyStopComplete()
                else -> {
                }
            }
        }

        private fun onProxyStartComplete() {
            Log.i(TAG, "onProxyStartComplete")
            Log.i(TAG, "isSuccess")

            runOnUiThread {
                mProgressDialog?.dismiss()
                mTextStatus?.text = resources.getString(R.string.proxy_started)
                updateUiState()
            }
        }

        private fun onProxyStopComplete() {
            d(TAG, "Airwatch::onProxyStopComplete()")
        }
    }

    private fun launchRingDialog() {
        mProgressDialog = ProgressDialog.show(this, "Please wait ...",
            "Starting Proxy ...", true)
        mProgressDialog?.setCancelable(true)
    }

    //region StatusListener

    override fun onStatusUpdate(status: String) {
        runOnUiThread {
            mTextStatus?.text = status
        }
    }

    //endregion StatusListener

    private var mLogsListener = object : LogsUpdateListener {
        override fun update() {
            runOnUiThread {
                mTextLogs?.text = LogsContainer.getInstance().logs
                mLogsScrollView?.post {
                    mLogsScrollView?.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }
        }
    }

    companion object {
        private val TAG = ProxyTestActivity.javaClass.simpleName
        const val ACTION_ERROR = "errorAction"
    }

    private fun getProxyType(type: ProxySetupType): String{
        return when(type){
            ProxySetupType.MAG -> "VMware Tunnel - Proxy"
            ProxySetupType.TUNNEL -> "VMware Tunnel"
            ProxySetupType.BASIC_USERNAME_PASSWORD -> "VMware Basic UserName/Password"
            ProxySetupType.PACV2 -> "VMware PACV2"
            else -> "Vmware Default"
        }
        return type.toString()
    }

    private fun updateUiState(){
        runOnUiThread{
            val proxyState = if (isTunnellingEnabled) R.string.enabled else R.string.disabled
            findViewById<EditText>(R.id.proxy_state).setText(resources.getString(proxyState))
            val gatewayManager = GatewayManager.getInstance(applicationContext)

            findViewById<EditText>(R.id.proxy_type).setText(getProxyType(GatewayConfigManager.init(applicationContext).appTunnelType))
        }
    }

}