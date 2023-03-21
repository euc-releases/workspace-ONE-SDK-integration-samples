// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airwatch.privacy.*

// This code is pasted into the Integration Guide with alterations noted in line
// and following.
//
// -   Remove comment lines other than where noted and those starting with a
//     number.

// Class in the integration guide inherits from Activity not BaseActivity.
open class MainActivity : BaseActivity() {

    companion object {
        private const val NOTIFICATION_REQ_CODE = 101
    }

    // 1. Helper instantiation.................................................
    // Integration guide initial instructions use the helper base class.
    // private val privacy by lazy {PrivacyBase(this)}
    private val privacy by lazy {Privacy(this)}

    // 2. Separate user interface initialization...............................
    private fun initializeUserInterface() {
        setContentView(R.layout.activity_main)
        // ...
        // Any other user interface initialization from the onCreate goes here.

        // Comment lines above are left in the guide but following code is
        // removed.
        configureTextView()
        configureInteraction(false)
    }

    // 3. Privacy result receipt...............................................
    // Integration guide initial instructions use the privacyCallbackBase, here,
    // but call it privacyCallback. Delete "Base" when pasting in.
    private fun privacyCallbackBase(result: AWPrivacyResult) {
        toastHere(
            "Privacy agreement ${result.privacyResultType} ${result.privacyUserOptInStatus}")
        initializeUserInterface()
    }

    private fun toastHere(message: String) = runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 4. Check consent requirement........................................
        // If the privacy agreement hasn't been accepted, prompt the user to
        // accept now and don't render any UI.
        if (privacy.consentRequired) {
            // Internet permission will be needed to open the enterprise privacy
            // policy. Don't include this comment or the following line in the
            // integration guide.
            toastUnlessInternet()

            // 5. Consent flow launch..........................................
            // Uncomment in case you want to use a callback that is the same as
            // the initial instructions in the integration guide.
            // privacy.startPrivacyFlow(this, this::privacyCallbackBase)
            privacy.startPrivacyFlow(this, this::privacyCallback)
        }
        else {
            // Integration guide initial instructions call initializeUserInterface without parameters.
            // initializeUserInterface()
            initializeUserInterface(false)
        }
        setUpPermissions()
    }

    private fun setUpPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_REQ_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            NOTIFICATION_REQ_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toastHere("Notification Permission has been denied by user")
                } else {
                    toastHere("Notification Permission has been granted by user")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // The `initialized` property isn't used in the integration guide.
    var initialized = false

    private fun initializeUserInterface(consentRequired: Boolean) {
        initialized = true
        setContentView(R.layout.activity_main)
        configureTextView()
        configureInteraction(consentRequired)
    }

    private val canInternet
        get() = this.packageManager.checkPermission(
            Manifest.permission.INTERNET, this.packageName
        ) == PackageManager.PERMISSION_GRANTED

    private fun toastUnlessInternet() {
        if (!canInternet) {
            toastHere("Missing permission android.permission.INTERNET")
        }
    }

    private fun privacyCallback(result: AWPrivacyResult) {
        val rejected = result.privacyResultType == AWPrivacyResultType.CANCEL
        val consentRequired = rejected

        if (initialized) {
            // If the UI has been initialized, just reconfigure the
            // interaction for the new acceptance status.
            configureInteraction(consentRequired)
        }
        else {
            // UI hasn't been initialized yet; do so now.
            initializeUserInterface(consentRequired)
        }
    }

    fun configureInteraction(consentRequired:Boolean) {
        val callback = this::privacyCallback
        with(findViewById<TextView>(R.id.textViewPrivacyAgreement)) {
            text = getString(
                if (consentRequired) R.string.privacy_accept
                else R.string.privacy_review
            )
            setOnClickListener(
                if (consentRequired) View.OnClickListener {
                    toastUnlessInternet()
                    privacy.startPrivacyFlow(context, callback)
                }
                else View.OnClickListener {
                    toastUnlessInternet()
                    privacy.reviewPrivacy(context, callback)
                }
            )
        }

        with(findViewById<TextView>(R.id.textViewPrivacyReset)) {
            paintFlags =
                if (consentRequired)
                    paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            setOnClickListener(
                if (consentRequired) null else View.OnClickListener {
                    AWPrivacyController.reset()
                    toastUnlessInternet()
                    configureInteraction(true)
                }
            )
        }
    }

}
