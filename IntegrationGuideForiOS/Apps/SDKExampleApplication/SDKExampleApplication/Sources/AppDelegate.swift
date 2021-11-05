//
//  AppDelegate.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // This class helps to intialize and start the sdk
        AWSDKHelper.shared.initializeSDK()
        return true
    }

    func application(_ app: UIApplication,
                         open url: URL,
                         options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        // Handle if this URL is for the Application.
        let sourceApplication = options[.sourceApplication] as? String
        return AWSDKHelper.shared.handleOpenURL(url, fromApplication: sourceApplication)
    }

    /// iOS calls this function when registration for Remote notification is successful.
      func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
        let token = tokenParts.joined()
        print("Device Token: \(token)")
        AWSDKHelper.shared.setDeviceToken(token)
    }

    /// iOS calls this function when registration for Remote notification is unsuccessful
    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Failed to register: \(error)")
    }

}

