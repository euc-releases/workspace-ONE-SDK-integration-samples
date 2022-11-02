//
//  AlertHandler.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import UIKit

import AWSDK

enum Alerts {
    case authenticationRequired
    case authenticationUnsupported
    case invalidURL
    case unableToFetchUserInfo
    case tryAgain
    case login

    var title: String {
        switch self {
            case .authenticationRequired:
                return String(localized: "initFail")

            case .authenticationUnsupported:
                return String(localized: "AuthenticationUnsupported")

            case .invalidURL:
                return String(localized: "InvalidURL")

            case .unableToFetchUserInfo:
                return String(localized: "FailedFetchInfo")

            case .tryAgain:
                return String(localized: "CredentialsUpdated")

            case .login:
                return String(localized: "LoginError")
        }
    }

    var message: String {
        switch self {
            case .authenticationRequired:
                return String(localized: "AuthenticationRequiredMessage")

            case .authenticationUnsupported:
                return String(localized: "authenticationUnsupportedMessage")


            case .invalidURL:
                return String(localized: "invalidURLMessage")

            case .unableToFetchUserInfo:
                return String(localized: "FailedFetchInfoMessage")

            case .tryAgain:
                return String(localized: "TryAgainMessage")

            case .login:
                return String(localized: "LoginFailedMessage")
        }
    }
}
