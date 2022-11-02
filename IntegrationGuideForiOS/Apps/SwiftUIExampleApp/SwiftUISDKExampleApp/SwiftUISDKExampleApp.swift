//
//  SwiftExampleAppApp.swift
//  SwiftExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import SwiftUI
import AWSDK

class AppDelegate: NSObject, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        AWLogInfo("App Launched")
        URLProtocol.registerClass(CustomHTTPProtocol.self)
        AWSDKHelper.shared.startSDK()
        return true
    }

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return AWController.clientInstance().handleOpenURL(url, fromApplication: nil)
    }
}

@main
struct SwiftUISDKExampleApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    _ = AWSDKHelper.shared.handleOpenURL(url: url, sourceApplication: nil)
                }
        }
    }
}
