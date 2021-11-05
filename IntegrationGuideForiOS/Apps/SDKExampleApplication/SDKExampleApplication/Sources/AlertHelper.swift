//
//  AlertHelper.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit


/// Created this  helper class for AlertAction which will be used in future PR.
class AlertHelper: NSObject {

    /// Helper method to  display an alert
    /// - Parameters:
    ///   - title:  Alert Title.
    ///   - message: Alert Message.
    ///   - actions: list of UIAlertActions.
    ///   - presenter: A Viewcontroller that wants to present alert.
    ///   If you do not want any actions, you can set the Alert instance to an empty Array after initialization.
    static func showAlert(title: String?, message: String, actions: [UIAlertAction]? = nil, presenter: UIViewController) {
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        // Check if there are no actions given, add the default action.
        var localactions = actions
        if  localactions == nil {
            let defaultAction = UIAlertAction(title:"Ok", style: .cancel, handler: nil)
            localactions = [defaultAction]
        }
        localactions?.forEach { alertController.addAction($0) }
        DispatchQueue.main.async {
            presenter.present(alertController, animated: true)
        }
    }
}
