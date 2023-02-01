// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.widget.TextView
import com.example.integrationguide.BaseActivity
import com.example.integrationguide.R
import com.example.integrationguide.SDKProfilePresentation

class CountdownActivity : BaseActivity() {

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        findViewById<TextView>(R.id.textViewCountdown)?.setOnClickListener {
            resetTimer()
        }

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_countdown)
        }
    }

    override fun onResume() {
        super.onResume()
        resetTimer()
    }

    private fun showCountdown(seconds:Long?):Long? {
        findViewById<TextView>(R.id.textViewCountdown)?.text = seconds
            ?.let { DateUtils.formatElapsedTime(it) }
            ?: getString(R.string.policy_null)
        return seconds
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        countDownTimer = null

        // Get the policy setting and display it in the policy display section.
        SDKProfilePresentation.displayPasswordTimeOutSettings(
            findViewById(R.id.policyPasswordTimeOut)
        ).also {
            // Also display it in the countdown timer.
            showCountdown(it)?.also { seconds ->
                // If the setting isn't null, start a timer too.
                countDownTimer =
                    object : CountDownTimer(seconds.times(1000L), 1000L) {
                        override fun onTick(millis: Long) {
                            showCountdown(millis / 1000L)
                        }

                        override fun onFinish() {
                            countDownTimer = null
                            resetTimer()
                        }
                    }.start()
            }
        }
    }

}
