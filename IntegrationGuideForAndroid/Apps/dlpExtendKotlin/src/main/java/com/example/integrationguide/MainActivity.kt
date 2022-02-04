// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.integrationguide.ui.AboutActivity
import com.example.integrationguide.ui.CountdownActivity
import com.example.integrationguide.ui.DLPEditTextActivity
import com.example.integrationguide.ui.DLPTextActivity
import com.example.integrationguide.ui.DLPWatermarkActivity
import com.example.integrationguide.ui.DLPWebViewActivity
import com.example.integrationguide.ui.SDKProfileActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.subtitle = getString(R.string.app_title)

        toastHere(SDKProfilePresentation.sdkState(this))
    }

    private fun toastHere(message: String) = runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.options_menu, menu)

        // Hmm. The getDrawable doesn't seem to select a dark-mode icon
        // automatically.
        menu?.findItem(R.id.about)?.icon = ContextCompat.getDrawable(this,
            if (darkMode) R.drawable.ic_action_info_dark
            else R.drawable.ic_action_info
        )

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.sdk -> {
                startActivity(Intent(this, SDKProfileActivity::class.java))
                true
            }
            R.id.dlp_edittext_views -> {
                startActivity(Intent(this, DLPEditTextActivity::class.java))
                true
            }
            R.id.dlp_text_views -> {
                startActivity(Intent(this, DLPTextActivity::class.java))
                true
            }
            R.id.dlp_watermark -> {
                startActivity(Intent(this, DLPWatermarkActivity::class.java))
                true
            }
                R.id.dlp_web_views -> {
                startActivity(Intent(this, DLPWebViewActivity::class.java))
                true
            }
            R.id.inactivity_countdown -> {
                startActivity(Intent(this, CountdownActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
