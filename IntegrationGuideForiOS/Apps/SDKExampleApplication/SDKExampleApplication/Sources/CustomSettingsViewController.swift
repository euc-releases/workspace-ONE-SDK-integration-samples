//
//  CustomSettingsViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

class CustomSettingsViewController: UIViewController {

    @IBOutlet weak var customSettingsTextView: UITextView!

    override func viewDidLoad() {
        super.viewDidLoad()

        self.customSettingsTextView.layer.borderColor = UIColor.black.cgColor
        self.customSettingsTextView.layer.borderWidth = 2.0
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func printSettings(_ sender: AnyObject) {

        // custom payload returns the custom settings.
        guard
            let customPayload = AWSDKHelper.shared.customPayload,
            let customSettings = customPayload.settings,
            customSettings != ""
        else {
            self.alertUser(withMessage: "Custom settings payload is either blank or not configured in SDK Profile")
            return
        }
        self.updateTextView(withString: customSettings)
    }

    // MARK: - Alert/UI
    private func alertUser(withMessage customMessage: String) {
        let action = (UIAlertAction(title: "Dismiss",
                                    style: UIAlertAction.Style.default,
                                    handler: nil))
        AlertHelper.showAlert(title: "Custom Settings", message: customMessage, actions: [action], presenter: self)
    }

    private func updateTextView(withString customSettings: String) {
        self.customSettingsTextView.text = customSettings
    }
}
