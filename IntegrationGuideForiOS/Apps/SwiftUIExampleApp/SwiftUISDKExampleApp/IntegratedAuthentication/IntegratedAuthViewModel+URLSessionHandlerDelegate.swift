//
//  IntegratedAuthViewModel+URLSessionHandlerDelegate.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//
import AWSDK

extension IntegratedAuthViewModel : URLSessionHandlerDelegate {
    
    func urlSessionDidRecieveChallenge(challenge: URLAuthenticationChallenge, sdkSupported: Bool) {
        guard sdkSupported else {
            self.displayAlert = CustomAlert(title: Alerts.authenticationUnsupported.title, message: Alerts.authenticationUnsupported.message)
            return
        }
        self.updateView(with: challenge)
    }

    func urlSessionRequestDidComplete(response: HTTPURLResponse?, data: Data?, error: Error?) {
        guard let response = response, let data = data, error == nil else {
            AWLogError("Error occured during URLSession \(String(describing: error))")
            return
        }
        DispatchQueue.main.async {
            self.statusText = "\(String(localized: "status")): \(response.statusCode)"
            self.updateWebView(response: response, data: data)
        }
    }

    func awSDKDidCompleteSessionChallenge(result: Bool) {
        if result { return }
        self.displayAlert = CustomAlert(title: Alerts.login.title, message: Alerts.login.message)
    }
}
