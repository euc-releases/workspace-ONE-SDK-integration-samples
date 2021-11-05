//
//  LocalAuthViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

class LocalAuthViewController: UIViewController {
    
    // Populating the Data to be loading in the Walkthrough
    // for local authentication
    let detail1 = "An app passcode can ensure the app is only accessible to the entitled user and that the user identity is maintained over time. A biometric, such as TouchID, can typically be implemented as a layer above the PIN code to optimise the user experience."
    let detail2 = "Organisation can opt to choose Username and Password instead of a PIN to allow secure access to the Workspace ONE SDK application. These credentials are the same as the enrollment credentials"
    let detail3 = "SSO stands for single sign on. It basically allows a user to access multiple Workspace ONE SDK applications by only entering a single password/passcode once as opposed to multiple times for each application that the user needs. Please make sure to align the SSO settings under custom SDK profile and Default SDK profile as shown in the screenshot."
    
    var pageTitles: [String] {
        return [detail1, detail2, detail3]
    }

    let pageImages = ["Passcode", "UsernamePassword", "SSO"]

    @IBAction func didTapLearnMore(_ sender: UIButton) {
        guard
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "customSWT"),
            let learnMoreViewController = viewController as? WalkThroughViewController else {
            return
        }
        learnMoreViewController.pageDescription = self.pageTitles
        learnMoreViewController.pageMedia = self.pageImages
        self.navigationController?.pushViewController(learnMoreViewController, animated: true)
    }

}
