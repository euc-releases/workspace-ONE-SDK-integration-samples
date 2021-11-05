//
//  IntegratedAuthenticationViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import AWSDK
internal class IntegratedAuthenticationViewController: UIViewController  {

    @IBOutlet weak var urlTextField: UITextField!
    @IBOutlet weak var httpStatusLabel: UILabel!
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet weak var updateCredentialsButton: UIBarButtonItem?

    var updateUserCredsStatus: Bool = false

    override func viewDidLoad() {

        // Checking the Workspace ONE SDK Account object. This is done to ensure Workspace ONE SDK's integrated auth logic
        // doesn't use stale credentials to handle network challenge. It is optional but a recommended check
        // before using Workspace ONE SDK's integrated auth APIs.
        self.checkSDKAccount { errorMessage in
            let updateAction = UIAlertAction(title: "Update", style: .default) { [weak self] _ in
                self?.updateCredentials()
            }
            let cancelAction = UIAlertAction(title: "Cancel", style: .default) { [weak self] _ in
                self?.showUpdateButton()
            }
            AlertHelper.showAlert(title: "Workspace ONE SDK Account", message: errorMessage, actions: [updateAction,cancelAction], presenter: self)
        }


        //Setting UI component on this View.
        loadingIndicator.hidesWhenStopped = true

        let borderColor = UIColor.black.cgColor
        webView.layer.borderColor = borderColor
        webView.layer.borderWidth = 2.0

        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tap)

        hideUpdateButton()
    }

    @IBAction func didTapGoButton(_ sender: AnyObject) {
        // Reset status label
        httpStatusLabel.text = ""

        // Grab URL and make request
        guard let url = self.urlTextField.url else {
            LoadingIndicator.hide()
            AlertHelper.showAlert(title:  Alert.invalidURL.title, message:  Alert.invalidURL.message, actions: [ Alert.invalidURL.dismissAction], presenter: self)
            return
        }

        LoadingIndicator.show()

        let sessionHandler = URLSessionHandler(requestURL: url)
        sessionHandler.delegate = self
        sessionHandler.initiateRequest()
    }


    @IBAction func didTapUpdateCredentials(_ sender: Any) {
        self.updateCredentials()
    }

    /// On some rare events AWEnrollmentAccount class shared Instance might become nil. This object is used by the Workspace ONE SDK challenge handler classes to seamlessly pass the credentials in response to the basic and NTLM type authentication challenge. We check if the shared instance is nil or corrupted below.
    /// - Parameter completion: completion handler to call when account is incomplete or user does not match  server user. Completion Handler takes String as parameter which hold of error message if any.
    internal  func checkSDKAccount(with completion: @escaping (String) -> () = { _ in }) {

        //  If the account object is nil we are calling updateUserCredentialsWithCompletion block that should repopulate the credentials in the instance

        guard let account = AWSDKHelper.shared.fetchEnrollmentAccountInfo(),
              account.username.isEmpty == false else {
            AWLogError("account username is empty")
            completion("Workspace ONE SDK Account object is incomplete")
            return
        }

        //  Sometimes when a device is re-enrolled with a different user or is checked out in the staging
        //  user flow, Account object might fail to update it's data accordingly. We use UserInformationController to
        //  get the username and compare it with the username retured by Account object
        AWSDKHelper.shared.retrieveUserInfo { [weak self] userInformation, error in

            guard let userInformation = userInformation,error == nil else {
                AWLogError("Error fetching information: \(String(describing: error))")
                if let presenter = self {
                    AlertHelper.showAlert(title:  Alert.unableToFetchUserInfo.title, message:  Alert.unableToFetchUserInfo.message, actions: [ Alert.unableToFetchUserInfo.dismissAction], presenter: presenter)
                }
                return
            }

            // In the event that the account object is not nil, but fails to update the username correctly, e.g.
            //  after a Check In / Check Out we call updateUserCredentialsWithCompletion that should correctly
            //  populate the data inside account object which is used by Workspace ONE SDK's challenge handler classes.

            // Fetch Server user
            let serverUser = userInformation.userName.domainStrippedUsername.lowercased()

            // Fetch local user
            guard let account = AWSDKHelper.shared.fetchEnrollmentAccountInfo() else {
                AWLogError("Error account is nill")
                if let presenter = self {
                    AlertHelper.showAlert(title:  Alert.unableToFetchUserInfo.title, message:  Alert.unableToFetchUserInfo.message, actions: [ Alert.unableToFetchUserInfo.dismissAction], presenter: presenter)
                }
                return
            }
            let sdkUser = account.username.domainStrippedUsername.lowercased()
            // Compare both users
            guard sdkUser == serverUser else {
                completion("Current Workspace ONE SDK User does not match Server User")
                return
            }
        }
    }

    /// This Workspace ONE SDK API will prompt user to authenticate and if the authentication is successufull with the backend, local credentials will be updated.
    private func updateCredentials(){
        AWSDKHelper.shared.updateUserCredentials { [weak self] (success, error) in
            guard success else {
                AWLogError("error occured: \(String(describing: error))")
                return
            }
            AWLogInfo("updated credentials and trying to log in with updated credentials")
            DispatchQueue.main.async {
                guard let presenter = self else { return }
                AlertHelper.showAlert(title:  Alert.tryAgain.title, message:  Alert.tryAgain.message, actions: [ Alert.tryAgain.dismissAction], presenter: presenter)
                self?.hideUpdateButton()
            }
        }
    }

    // MARK:- UI Actions

    ///  Set the labels based on the data/response values
    /// - Parameter withString: data/response value string.
    private  func updateLabel(_ withString: String) {
        DispatchQueue.main.async {
            self.httpStatusLabel.text?.append("-> \(withString)")
        }
    }

    /// update view for the type of authentication challenge received.
    /// - Parameter challenge: type of authentication challenge method.
    internal func updateView(with challenge: URLAuthenticationChallenge) {

        self.updateLabel("challenge type is : \(challenge.protectionSpace.authenticationMethod)")

        guard challenge.previousFailureCount == 1 else {
            return
        }

        guard self.updateUserCredsStatus else {
            // Handling the case where password might have changed in the AD.
            // Updating the credentials in the Workspace ONE SDK keychain once and promting user to try again.
            self.updateUserCredsStatus = true
            self.updateCredentials()
            return
        }

        // Checking updateUserCredsStatus to see if the local credentials have been already updated by
        // calling updateUserCreds method. If yes, then enrolled active user doesn't seem to have access to web service endpoint.
        AlertHelper.showAlert(title:  Alert.login.title, message:  Alert.login.message, actions: [ Alert.login.dismissAction], presenter: self)
    }


    /// update webView with the data that has been received after sucessfull auth challenge completion.
    /// - Parameters:
    ///   - response: metadata associated with response.
    ///   - data: data that needs to be loaded in webView
    internal func updateWebView(response: HTTPURLResponse, data: Data) {
        guard
            let url = response.url,
            let dataString = String(data: data, encoding: response.encoding)
        else { return }
        guard
            let mimeType = response.mimeType,
            let textEncodingName = response.textEncodingName
        else {
            DispatchQueue.main.async { self.webView.loadHTMLString(dataString, baseURL: url) }
            return
        }
        DispatchQueue.main.async {
            self.webView.load(data,
                              mimeType: mimeType,
                              textEncodingName: textEncodingName,
                              baseURL: url)
        }
    }

    private  func showUpdateButton() {
        self.httpStatusLabel.text = "Account Object needs to be updated. Please click update"
        self.updateCredentialsButton?.isEnabled = true
        self.updateCredentialsButton?.tintColor = UIColor(red: 14.0/255, green: 122.0/255, blue: 254.0/255, alpha: 1.0)
    }

    private func hideUpdateButton() {
        httpStatusLabel.text = ""
        updateCredentialsButton?.isEnabled = false
        updateCredentialsButton?.tintColor = UIColor.clear
    }
}
