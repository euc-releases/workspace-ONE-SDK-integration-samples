//
//  NotificationHelper.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import UIKit
import UserNotifications

internal class NotificationHelper{

    /// This function asks user permission to show notifications. completion handler receives a bool denoting whether
    /// permission is granted or not.
    static func registerForPushNotifications() {
        UNUserNotificationCenter.current()
            .requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
                print("Permission granted: \(granted)")
                guard granted else { return }
                self.getNotificationSettings()
            }
    }

    /// This function will register for remote notifications once user allows to show push notifications.
    static func getNotificationSettings() {
        UNUserNotificationCenter.current().getNotificationSettings { settings in
            print("Notification settings: \(settings)")
            guard settings.authorizationStatus == .authorized else {
                print("Can not register for remote notification since not authorized.")
                return
            }
            DispatchQueue.main.async {
                UIApplication.shared.registerForRemoteNotifications()
            }
        }
    }
    
}

/// Put this in extension class
public extension Notification.Name {
    static let AWSDKInitialized = Notification.Name("com.air-watch.sdk.initialized")
}



