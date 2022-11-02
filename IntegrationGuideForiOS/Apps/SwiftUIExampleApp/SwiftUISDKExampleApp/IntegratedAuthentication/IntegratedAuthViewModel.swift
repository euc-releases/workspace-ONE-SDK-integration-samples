//
//  IntegratedAuthViewModel.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import AWSDK
import Combine

class IntegratedAuthViewModel: ObservableObject {
    private var cancellable = Set<AnyCancellable>()
    @Published var statusText: String = String(localized: "status")
    var updateUserCredsStatus: Bool = false
    @Published var displayAlert: CustomAlert?
    var loadHtmlString = PassthroughSubject<(dataString: String, baseURL: URL), Never>()
    var loadData = PassthroughSubject<(data: Data, mimeType: String, encodingName: String, baseURL: URL), Never>()
}

extension IntegratedAuthViewModel  {

    func goButtonTapped(urlString: String) {
        // Reset status label
        statusText = ""

        // Grab URL and make request
        guard let url = URL(string: urlString) else {
            //LoadingIndicator.hide()
            self.displayAlert = CustomAlert(title: Alerts.invalidURL.title, message: Alerts.invalidURL.message)
            return
        }

        let sessionHandler = URLSessionHandler(requestURL: url)
        sessionHandler.delegate = self
        sessionHandler.initiateRequest()
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
        AWSDKHelper.shared.retrieveUserInfo()
            .sink { completion in
                switch completion {
                case .failure(let error) :
                    AWLogError("Error fetching information: \(String(describing: error))")
                    self.displayAlert = CustomAlert(title: Alerts.unableToFetchUserInfo.title, message: Alerts.unableToFetchUserInfo.message)
                    return
                case .finished: ()
                }
            } receiveValue: { userInformations in
                // In the event that the account object is not nil, but fails to update the username correctly, e.g.
                //  after a Check In / Check Out we call updateUserCredentialsWithCompletion that should correctly
                //  populate the data inside account object which is used by Workspace ONE SDK's challenge handler classes.
                
                // Fetch Server user
                let serverUser = userInformations.userName.domainStrippedUsername.lowercased()
                
                // Fetch local user
                guard let account = AWSDKHelper.shared.fetchEnrollmentAccountInfo() else {
                    AWLogError("Error account is nill")
                    self.displayAlert = CustomAlert(title: Alerts.unableToFetchUserInfo.title, message: Alerts.unableToFetchUserInfo.message)
                    return
                }
                let sdkUser = account.username.domainStrippedUsername.lowercased()
                // Compare both users
                guard sdkUser == serverUser else {
                    completion("Current Workspace ONE SDK User does not match Server User")
                    return
                }
            }
            .store(in: &cancellable)
    }

    /// This Workspace ONE SDK API will prompt user to authenticate and if the authentication is successufull with the backend, local credentials will be updated.
    private func updateCredentials(){
        AWSDKHelper.shared.updateUserCredentials { [weak self] (success, error) in
            guard success else {
                AWLogError("error occured: \(String(describing: error))")
                return
            }
            AWLogInfo("updated credentials and trying to log in with updated credentials")
            self?.displayAlert = CustomAlert(title: Alerts.tryAgain.title, message: Alerts.tryAgain.message)
        }
    }

    // MARK:- UI Actions
    ///  Set the labels based on the data/response values
    /// - Parameter withString: data/response value string.
    private  func updateLabel(_ withString: String) {
        DispatchQueue.main.async {
            self.statusText = self.statusText.appending("-> \(withString)")
        }
    }

    /// update view for the type of authentication challenge received.
    /// - Parameter challenge: type of authentication challenge method.
    internal func updateView(with challenge: URLAuthenticationChallenge) {
        DispatchQueue.main.async {
        self.statusText = " \(String(localized: "challengeType")): \(challenge.protectionSpace.authenticationMethod)"
        }
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

        self.displayAlert = CustomAlert(title: Alerts.login.title, message: Alerts.login.message)

        // Checking updateUserCredsStatus to see if the local credentials have been already updated by
        // calling updateUserCreds method. If yes, then enrolled active user doesn't seem to have access to web service endpoint.
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
            DispatchQueue.main.async { self.loadHtmlString.send((dataString: dataString, baseURL: url)) }
            return
        }
        DispatchQueue.main.async {
            self.loadData.send((data: data, mimeType: mimeType, encodingName: textEncodingName, baseURL: url))
        }
    }
}


extension String {
    // MARK:- Helper methods
    public var domainStrippedUsername: String {
        let usernameComponents = self.components(separatedBy: "\\")
        return usernameComponents.count > 1 ? usernameComponents[1] : self
    }
}

extension URLResponse {
    var encoding: String.Encoding {
        let usedEncoding = String.Encoding.utf8
        guard let encodingName = self.textEncodingName as CFString? else {
            return usedEncoding
        }
        let encoding = CFStringConvertEncodingToNSStringEncoding(CFStringConvertIANACharSetNameToEncoding(encodingName))
        guard encoding != UInt(kCFStringEncodingInvalidId) else {
            return usedEncoding
        }
        return String.Encoding(rawValue: encoding)
    }
}

