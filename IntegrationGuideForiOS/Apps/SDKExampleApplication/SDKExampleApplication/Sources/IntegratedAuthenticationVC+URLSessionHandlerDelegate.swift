//
//  IntegratedAuthenticationVC+URLSessionHandlerDelegate.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//
import UIKit

extension IntegratedAuthenticationViewController : URLSessionHandlerDelegate {

    func urlSessionDidRecieveChallenge(challenge: URLAuthenticationChallenge, sdkSupported: Bool) {
        guard sdkSupported else {
            LoadingIndicator.hide()
            AlertHelper.showAlert(title:  Alert.authenticationUnsupported.title, message:  Alert.authenticationUnsupported.message, actions: [ Alert.authenticationUnsupported.dismissAction], presenter: self)
            return
        }
        self.updateView(with: challenge)
    }

    func urlSessionRequestDidComplete(response: HTTPURLResponse?, data: Data?, error: Error?) {
        LoadingIndicator.hide()
        guard let response = response, let data = data, error == nil else {
            //AWLogError("Error occured during URLSession \(String(describing: error))")
            return
        }
        DispatchQueue.main.async {
            self.httpStatusLabel.text = "HTTP Code: \(response.statusCode)"
        }
        self.updateWebView(response: response, data: data)
    }

    func awSDKDidCompleteSessionChallenge(result: Bool) {
        if result { return }
        AlertHelper.showAlert(title:  Alert.login.title, message:  Alert.login.message, actions: [ Alert.login.dismissAction], presenter: self)
    }
}
