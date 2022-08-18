//
//  AppDelegate.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    // Not need of UIWindow in AppDelegate when SceneDelete is implemented
    //var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        URLProtocol.registerClass(CustomHTTPProtocol.self)
        // This class helps to intialize and start the sdk
        AWSDKHelper.shared.initializeSDK()
        return true
    }

    //This is now moved scene delegate to handle the openurl
/*    func application(_ app: UIApplication,
                         open url: URL,
                         options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        // Handle if this URL is for the Application.
        let sourceApplication = options[.sourceApplication] as? String
        return AWSDKHelper.shared.handleOpenURL(url, fromApplication: sourceApplication)
    }
*/
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

// Handling Scene session
extension UIApplication {

    func application(
   _ application: UIApplication,
   configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions
    ) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

   func application(
   _ application: UIApplication,
   didDiscardSceneSessions sceneSessions: Set<UISceneSession> ) {
       // Called when the user discards a scene session.
       // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
       // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
   }
}
