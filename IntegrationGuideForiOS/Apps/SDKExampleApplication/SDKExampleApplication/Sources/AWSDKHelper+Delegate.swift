//
//  AWSDKHelper+AWControllerDelegate.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import AWSDK

extension AWSDKHelper: AWControllerDelegate {
    func controllerDidFinishInitialCheck(error: NSError?) {
        // This method gets called when Workspace ONE SDK finishes its setup.
        // At this point, Workspace ONE SDK has verified the enrollment, sets up Tunnel (if configured)
        // and applied Settings configured by your admin.

        // This method gets called once per launch of the application. Expect to recieve this
        // even if the application is launched from background (warm-start)

        // Note: This method is not guaranteed to be called on Main Thread. So executing any UI
        // related code in this callback may cause problems.
        if let error = error {
            // At this point, we should take away access to all secure resources and should update the application
            // to display an error describing why user can not access the resource.
            AWLogError("Application recieved initial check Error: \(error)")
        } else {
            // EVerything looks fine, Let's update the UI to present any secure content.
            AWLogInfo("Controller did complete initial check.")

            // Register for push notifications
            NotificationHelper.registerForPushNotifications()
        }

        // Update the interested components (generally UI) about Workspace ONE SDK's Start process status.
        NotificationCenter.default.post(name: .AWSDKInitialized, object: error)
    }

    func controllerDidReceive(profiles: [Profile]) {
        // This method to designed to provide a profile for application immediately after the controller has received
        //
        //
        // Usually, Applications receive Workspace ONE SDK Settings as a Profile.
        // Application will receive a profile updated by admin according to the following rules.
        //  1. When App is being launched for the first time.
        //  2. When Application is being killed and relaunched (cold-boot).
        //  3. After launched from the background and the last profile updated was more than 4 hours ago.
        //
        // In other cases, the cached profile will be returned through this method.
        //
        // Note: First time install and launch of the application requires a profile to be available.
        // Otherwise Workspace ONE SDK's Controller Start process will be halted and will be reported as an error.
        // Generally, this method will be called after `controllerDidFinishInitialCheck(error:)` except in
        // the instance of first time launch of the application.
        //
        AWLogVerbose("Workspace ONE SDK received \(profiles.count) profiles.")

        guard profiles.count > 0 else {
            AWLogError("No profile received")
            return
        }

        AWLogInfo("Now printing profiles received: \n")
        profiles.forEach { AWLogInfo("\(String(describing: $0))") }
    }

    func controllerDidReceive(enrollmentStatus: AWSDK.EnrollmentStatus) {
        // This optional method will be called when enrollment status is retrieved from Workspace ONE console.
        // You will receive one of the following.
        //      When Device was never enrolled:
        //        `deviceNotFound`:
        //
        //      When device is in process of enrollment:
        //        'discovered'
        //        'registered'
        //        'enrollmentInProgress'
        //
        //      When device is enrolled and compliant:
        //        'enrolled'
        //
        //      When device is unenrolled or detected as non-compliant:
        //        `enterpriseWipePending`
        //        `deviceWipePending`
        //        `retired`
        //        `unenrolled`
        //      When network is not reachable or server sends a status that can not be parsed to one of the above.
        //         `unknown`
        AWLogInfo("Current Enrollment Status: \(String(describing: enrollmentStatus))")
    }

    func controllerDidWipeCurrentUserData() {
        // Please check for this method to handle cases when this device was unenrolled, or user tried to unlock with more than allowed attempts,
        // or other cases of compromised device detection etc. You may receive this callback at anytime during the app run.
        AWLogError("Application should wipe all secure data")
    }

    func controllerDidLockDataAccess() {
        // This optional method will give opportunity to prepare for showing lock screen and thus saving any sensitive data
        // before showing lock screen. This method requires your admin to set up authentication type as either passcode or
        // username/password.
        AWLogInfo("Controller did lock data access.")
    }

    func controllerWillPromptForPasscode() {
        // This optional method will be called right before showing unlock screen. It is intended to take care of any UI related changes
        // before Workspace ONE SDK's Controller will present its screens. This method requires your admin to set up authentication type as either passcode or
        // username/password.
        AWLogInfo("Controller did lock data access.")
    }

    func controllerDidUnlockDataAccess() {
        // This method will be called once user enters right passcode or username/password on lock screen.
        // This method requires your admin to set up authentication type as either passcode or
        // username/password.
        AWLogInfo("User successfully unlocked")
    }

    func applicationShouldStopNetworkActivity(reason: AWSDK.NetworkActivityStatus) {
        // This method gets called when your admin restricts offline access but detected that the network is offline.
        // This method will also be called when admin whitelists specific SSIDs that this device should be connected while using this
        // application and user is connected to different/non-whitelisted WiFi network.
        //
        // Application should look this callback and stop making any network calls until recovered. Look for `applicationCanResumeNetworkActivity`.
        AWLogError("Workspace ONE SDK Detected that device violated network access policy set by the admin. reason: \(String(describing: reason))")
    }

    func applicationCanResumeNetworkActivity() {
        // This method will be called when device recovered from restrictions set by admin regarding network access.
        AWLogInfo("Application can resume network activity.")
    }

}


