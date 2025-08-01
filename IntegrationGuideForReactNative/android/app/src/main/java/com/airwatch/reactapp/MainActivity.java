// Copyright 2022 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.airwatch.reactapp;

import com.workspaceonesdk.WorkspaceOneSdkActivity;

public class MainActivity extends WorkspaceOneSdkActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "example";
  }
}
