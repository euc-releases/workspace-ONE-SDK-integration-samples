//
//  SDKUseCasesTableViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import AWSDK

// A static tableview class which act as a gateway to different Workspace ONE SDK Use cases
class SDKUseCasesTableViewController: UITableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        self.addNotificationObservers()

        //  THIS IS WHERE WE ARE SHOWING THE BLOCKER SCREEN  **
        //  We should wait for Workspace ONE SDK to initialize completely before attempting to utilize any Workspace ONE SDK resource for example trying to attempt tunneling. This blocker screens is completely optional but is shown to demonstrate good practise.
        AWLogVerbose("Displaying Blocking view while Workspace ONE SDK intializes")
        // Add transparent blocker
        LoadingIndicatorView.show(inView: self.parent!.view, loadingText: "Initializing Workspace ONE SDK...")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func addNotificationObservers() {
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(dismissBlockingView(_:)),
                                               name: .AWSDKInitialized,
                                               object: nil)
    }

    // MARK: Blocker View Screen
    @objc func dismissBlockingView(_ notification: Notification) {
        AWLogVerbose("Received AWSDKInitialized notification")
        DispatchQueue.main.async {
            if let error = notification.object {
                AWLogError("Received error while starting Workspace ONE SDK: \(String(describing: error))")
                let  action = (UIAlertAction(title: "Dismiss", style: .default, handler: nil))
                AlertHelper.showAlert(title: "AWSDK Initialization Report", message: "An error occured while initializing Workspace ONE SDK", actions: [action], presenter: self)
            }
            AWLogInfo("Removing Blocker view since \"controllerDidFinishInitialCheck\" was called...")
            LoadingIndicatorView.hide()
        }
    }

}

