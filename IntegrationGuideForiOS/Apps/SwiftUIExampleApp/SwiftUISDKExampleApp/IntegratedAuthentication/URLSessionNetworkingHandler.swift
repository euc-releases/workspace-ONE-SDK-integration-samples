//
//  URLSessionNetworkingHandler.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import Foundation
import AWSDK

/// WebViewModel will  delegate the networking logic to URLSessionNetworkingHandler and will get notififed of networking activity by confirming to URLSessionHandlerDelegate protocol
public protocol URLSessionHandlerDelegate{
    func urlSessionDidRecieveChallenge(challenge: URLAuthenticationChallenge, sdkSupported: Bool)
    func urlSessionRequestDidComplete(response: HTTPURLResponse?, data: Data?, error: Error?)
    func awSDKDidCompleteSessionChallenge(result: Bool)
}

public class URLSessionHandler: NSObject, URLSessionTaskDelegate, URLSessionDelegate {

    /// delegate : variable holding the instance of the class confirming to URLSessionHandlerDelegate protocol.
    var delegate: URLSessionHandlerDelegate?
    /// url : URL passed by the caller class to initiate  request.
    var url: URL?
    /// sessionManager : Global varialbe to hold  session manager instance.
    var session: URLSession?

    init(requestURL: URL) {
        self.url = requestURL
    }

    /// Creating a request and starting the session
    internal  func initiateRequest() {
        if let requestURL = self.url {
            let request = URLRequest(url: requestURL)
            let configuration = Foundation.URLSession.shared.configuration
            session = URLSession(configuration: configuration, delegate: self, delegateQueue: nil)
            AWLogInfo("Request URL: \(String(describing: request.url))")
            self.sessionGetRequest(request: request)
        }
    }

    // MARK:- URLSession

    /// Creates the session task and retieve tha data for the specified URL
    /// - Parameter request: URLrequest to fetch the Data
    internal func sessionGetRequest(request: URLRequest) {
        guard  let session = self.session else { return }
        let task = session.dataTask(with: request, completionHandler: {
            taskData, taskResponse, error in

            if let data = taskData, let response: HTTPURLResponse = taskResponse as? HTTPURLResponse {

                if let sessionDelegate = self.delegate {
                    sessionDelegate.urlSessionRequestDidComplete(response: response, data: data, error: error)
                    AWLogInfo("Clearing out the session for security purposes")
                    session.invalidateAndCancel()
                    session.finishTasksAndInvalidate()
                }

            }
        })

        AWLogInfo("Starting session")
        task.resume()
    }

    /// Delegate callbacks implementation to handle the authentication challenge returned by the server
    public func urlSession(_ session: URLSession, task: URLSessionTask, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {

        if let sessionDelegate = self.delegate {
            AWLogInfo("URLSession Challenge type : \(challenge.protectionSpace.authenticationMethod)")
            switch challenge.protectionSpace.authenticationMethod {
                case NSURLAuthenticationMethodServerTrust:
                    let isTrusted = AWController.clientInstance().validate(serverTrust: challenge.protectionSpace.serverTrust!,
                                                                                               trustStore: .deviceAndCustom, strictness: .ignore)
                    if isTrusted {
                        let creds = URLCredential(trust: challenge.protectionSpace.serverTrust!)
                        completionHandler(.useCredential, creds);
                    } else {
                        completionHandler(.performDefaultHandling, nil);
                    }
                    break

                case NSURLAuthenticationMethodHTTPBasic, NSURLAuthenticationMethodNTLM, NSURLAuthenticationMethodClientCertificate :
                    self.offloadAuthenticationToAWSDK(challenge: challenge, completionHandler: completionHandler)
                    sessionDelegate.urlSessionDidRecieveChallenge(challenge: challenge, sdkSupported: true)
                    break

                default:
                    AWLogInfo("Workspace ONE SDK can't handle this type of authentication challenge")
                    completionHandler(.cancelAuthenticationChallenge, nil)
                    sessionDelegate.urlSessionDidRecieveChallenge(challenge: challenge, sdkSupported: false)
                    break
            }
        }
    }

    /// Using Workspace ONE SDK's API to handle the supported auth challenge
    /// - Parameters:
    ///   - challenge: authentication challenge returned by the server
    ///   - completionHandler: completionhandler  with credential and authetication disposittion.
    private func offloadAuthenticationToAWSDK(challenge : URLAuthenticationChallenge,completionHandler: @escaping ((URLSession.AuthChallengeDisposition, URLCredential?) -> Swift.Void))  {
        let awAuthHandleStatus = AWSDKHelper.shared.handleChallengeForURLSession(challenge: challenge, completionHandler: completionHandler)

        // Notifying the ViewController that Workspace ONE SDK had to cancel the authentication challenge
        if let sessionDelegate = self.delegate, !awAuthHandleStatus {
            sessionDelegate.awSDKDidCompleteSessionChallenge(result: false)
        }
    }

}

