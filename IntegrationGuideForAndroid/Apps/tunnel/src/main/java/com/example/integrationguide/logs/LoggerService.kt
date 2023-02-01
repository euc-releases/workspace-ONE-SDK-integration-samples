// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.logs

import android.app.Service
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.*
import java.lang.Exception

class LoggerService : Service() {
    private var mLSThread: LoggerServiceThread? = null
    private var mLogcatCmd: String? = null
    private var isServiceRunning = false

    private inner class LoggerServiceThread : Thread() {
        protected var mLogFileWriter: FileWriter? = null
        protected var mLogBufWriter: BufferedWriter? = null
        protected var mLogCurFile: File? = null
        protected var miFileCount = 0
        protected var mRunThread: Boolean? = null
        fun setRunState(status: Boolean) {
            mRunThread = status
        }

        override fun run() {
            var line: String?
            var process: Process? = null
            var bufRdr: BufferedReader? = null
            try {
                process = Runtime.getRuntime().exec(mLogcatCmd)
                bufRdr = BufferedReader(InputStreamReader(process.inputStream))
                while (bufRdr.readLine().also { line = it } != null && mRunThread!!) {
                    if (isServiceRunning) {
                        LogsContainer.getInstance().addLogs(line)
                        if (LogsContainer.getInstance().isLoggingStopped) break
                    } else {
                        stopThread()
                    }
                }
                LogsContainer.getInstance().addLogs(line)
            } catch (e: IOException) {
                Log.e(TAG, "run: Got an IO Exception")
            } finally {
                //Close all the streams
                if (bufRdr != null) {
                    try {
                        bufRdr.close()
                    } catch (e: IOException) {
                        Log.e(TAG, "run: Got an IO Exception")
                    } finally {
                        bufRdr = null
                    }
                }
                process?.destroy()
            }
        }

        //Initializing the variables
        init {
            mLogFileWriter = null
            mLogBufWriter = null
            mLogCurFile = null
            miFileCount = 0
            mRunThread = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
    }

    private fun stopThread() {
        //Stop the thread if present
        if (mLSThread != null) {
            mLSThread!!.setRunState(false)
            //We need to log some information as the thread will be blocking on the read line
            Log.i(TAG, "Logging for stopping the thread.")
            mLSThread = null
        } else {
            Toast.makeText(applicationContext, "Logger not running", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //Start the thread here, before starting check whether the
        isServiceRunning = true
        if (mLSThread != null) {
            Toast.makeText(applicationContext, "Logger already started", Toast.LENGTH_SHORT).show()
        } else {
            mLSThread = LoggerServiceThread()
            mLSThread!!.setRunState(true)
            mLSThread!!.start()
            Toast.makeText(applicationContext, "Logger Started", Toast.LENGTH_SHORT).show()
        }
        return START_STICKY
    }

    companion object {
        private val TAG = LoggerService::class.java.simpleName

        fun startService(context: Context): Boolean {
            try {
                //Start the service
                val intent = Intent(context, LoggerService::class.java)
                context.startService(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Not able to start the logger service: $e", e)
                return false
            }
            return true
        }

        fun stopService(cntxt: Context): Boolean {
            try {
                //Start the service
                val srvcIntnet = Intent(cntxt, LoggerService::class.java)
                cntxt.stopService(srvcIntnet)
                Toast.makeText(cntxt, "Logger Service Stopped", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e(TAG, "Not able to stop the logger service: $e", e)
                return false
            }
            return true
        }
    }

    //Initializing member variables
    init {
        mLogcatCmd = "logcat AirWatch_:D AirWatch_Proxy:D AirWatch_ClientVerifierImpl:D *:S"
        mLSThread = null
    }
}