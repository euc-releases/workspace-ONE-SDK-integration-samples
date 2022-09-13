// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    override func application(_ application: UIApplication,didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
      }
  
    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        let notif = Notification(name: Notification.Name(rawValue: "UIApplicationOpenURLOptionsSourceApplicationKey"), object: url, userInfo: options)
        NotificationCenter.default.post(notif)
        return true
    }
    
    
}
