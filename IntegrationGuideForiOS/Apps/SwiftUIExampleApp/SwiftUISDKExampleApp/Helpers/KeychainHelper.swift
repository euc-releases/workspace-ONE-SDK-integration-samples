//
//  KeychainHelper.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation

class ClearKeychainHelper {

    static let secItemClasses = [kSecClassGenericPassword,
                                 kSecClassInternetPassword,
                                 kSecClassCertificate,
                                 kSecClassKey,
                                 kSecClassIdentity];

    static func clearKeychain(completion: @escaping (()->Void) = {} ) {
        self.secItemClasses.forEach{ _ = SecItemDelete([String(kSecClass): $0] as CFDictionary) }
        completion()
    }
}
