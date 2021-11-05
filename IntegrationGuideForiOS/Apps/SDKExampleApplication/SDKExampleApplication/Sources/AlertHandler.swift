//
//  AlertHandler.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import UIKit

import AWSDK

enum Alert {
    case authenticationRequired
    case authenticationUnsupported
    case invalidURL
    case unableToFetchUserInfo
    case tryAgain
    case login

    var title: String {
        switch self {
            case .authenticationRequired:
                return "Authentication Required"

            case .authenticationUnsupported:
                return "Authentication Unsupported"

            case .invalidURL:
                return "Invalid URL"

            case .unableToFetchUserInfo:
                return "Failed to Fetch Info"

            case .tryAgain:
                return "Credentials Updated"

            case .login:
                return "Login Error"
        }
    }

    var message: String {
        switch self {
            case .authenticationRequired:
                return "Tunneling was successfull and we were able to hit the endpoint but this URL requires authentication. Please refer the Integrated Authentication ViewController OR access a URL that does not need authentication."

            case .authenticationUnsupported:
                return "Tunneling was successfull and we were able to hit the endpoint but this type of Authentication challenge is not supported by the Workspace ONE SDK."

            case .invalidURL:
                return "Please confirm the formatting of the URL."

            case .unableToFetchUserInfo:
                return "An Error occurred while Workspace ONE SDK was trying to perform Integrated Auth. Please make sure your enrollment credentials have access to this endpoint"

            case .tryAgain:
                return "Credentials Updated successfully, Please try again!"

            case .login:
                return "An Error occurred while Workspace ONE SDK was trying to perform Integrated Auth. Please make sure your enrollment credentials have access to this endpoint"
        }
    }

    var dismissAction: UIAlertAction {
        return UIAlertAction(title: "Dismiss", style: .default) { _ in
            AWLogInfo("Dismissed")
        }
    }

    var alertController: UIAlertController {
        return UIAlertController(title: self.title,
                                 message: self.message,
                                 preferredStyle: .alert)
    }
}

typealias AlertPresentationCompletion = () -> Void

extension UIAlertController {

    var `default`: UIAlertController {
        let dismissAction = UIAlertAction(title: "Dismiss", style: .default) { _ in
            AWLogInfo("Dismissed")
        }
        let alertController = self
        alertController.addAction(dismissAction)
        return alertController
    }

    func present(onViewController: UIViewController?, completion: @escaping AlertPresentationCompletion = {}) -> () {
        guard let viewController = onViewController else {
            return
        }
        AWLogInfo("Alert presenting \(String(describing: self.message))")
        DispatchQueue.main.async {
            viewController.present(self, animated: true, completion: completion)
        }
    }
}
