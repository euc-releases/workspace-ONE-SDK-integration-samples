// Copyright 2024 VMware LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.Manifest

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airwatch.util.Logger
import com.airwatch.util.ShareLogUtil
class MainActivity : BaseActivity() {

    private val TAG = MainActivity::class.java.simpleName
    companion object {
        private const val NOTIFICATION_REQ_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.subtitle = getString(R.string.app_title)
        setUpPermissions()
    }

    private fun setUpPermissions() {
        Logger.i(TAG,"set up permissions")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Logger.i(TAG,"POST_NOTIFICATIONS permission not yet granted")
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
                    Logger.i(TAG,"Notification Permission has been denied by user")
                    toastHere("Notification Permission has been denied by user")
                } else {
                    Logger.i(TAG,"Notification Permission has been granted by user")
                    toastHere("Notification Permission has been granted by user")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun toastHere(message: String) = runOnUiThread {
        Logger.i(TAG,"show toast here")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_logs-> {
                ShareLogUtil.sendLogsToConsole()
                toastHere("Sending Logs...")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
