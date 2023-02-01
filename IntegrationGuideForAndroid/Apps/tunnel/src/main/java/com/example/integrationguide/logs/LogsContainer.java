// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.logs;


import com.example.integrationguide.interfaces.LogsUpdateListener;

import java.util.ArrayList;

public class LogsContainer {

    private static LogsContainer mSelfInsance = new LogsContainer();
    private static int MAX_SIZE = 100;
    
    private LogsUpdateListener mLogsUpdateListener =  null;
    private boolean allowLogs   = false;
    private boolean stopLogging = false;
    
    public boolean isLoggingStopped(){
        return stopLogging;
    }

    private ArrayList<String> mLogsList = new ArrayList<>();
 
    public void setLogsUpdateListener(LogsUpdateListener listener){
            mLogsUpdateListener = listener;
            stopLogging = false;
    }
    
    public static LogsContainer getInstance(){
        return mSelfInsance;
    }

    public void setAllowLogs(boolean allow){
        allowLogs = allow;
    }
    
    public void addLogs(String logs){
        if(!allowLogs)
            return;                      
        if(mLogsList.size() > MAX_SIZE)         //To Avoid out of memory issue.
            clearLogs();
        mLogsList.add(logs);
      
        if(mLogsUpdateListener != null)
            mLogsUpdateListener.update();
    }

    public String getLogs(){
        StringBuffer buffer = new StringBuffer("");
        for(int i = 0; i < mLogsList.size();i++){
            buffer.append(mLogsList.get(i));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public void clearLogs(){
        mLogsList.clear();
        allowLogs = false;
    }
    
    public void unregisterListener(){
        stopLogging = true;
        mLogsUpdateListener = null;
    }
}
