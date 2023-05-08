//  Copyright 2023 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause

import Foundation
import UIKit
import SwiftUI

// Handy extension to get an error message from an OSStatus.
// TOTH https://github.com/vmware/captive-web-view/blob/061c9140fc16f88e3ab44cc170eb0d1f057adc0b/forApple/CaptiveCrypto/CaptiveCrypto/StoredKey.swift#L37
extension OSStatus {
    var secErrorMessage: String {
        return (SecCopyErrorMessageString(self, nil) as String?) ?? "\(self)"
    }
}

func clearAppKeyChain() {
    [
        // List here is in the same order as in the reference documentation. https://developer.apple.com/documentation/security/keychain_services/keychain_items/item_class_keys_and_values#1678477
        kSecClassGenericPassword,
        kSecClassInternetPassword,
        kSecClassCertificate,
        kSecClassKey,
        kSecClassIdentity
    ].forEach {secClass in
        // TOTH  https://github.com/vmware/captive-web-view/blob/061c9140fc16f88e3ab44cc170eb0d1f057adc0b/forApple/CaptiveCrypto/CaptiveCrypto/StoredKey.swift#L184

        // Query to find all items of this security class.
        let query: [CFString: Any] = [kSecClass: secClass]
        let status = SecItemDelete(query as CFDictionary)
        if status == errSecSuccess || status == errSecItemNotFound {
            print("Deleted \"\(secClass)\" from keychain.")
        }
        else {
            print("Failed to delete \"\(secClass)\" from keychain:"
                  , " \(status.secErrorMessage)")
        }
    }
}

private let alertTitle = "Delete the app keychain?"
private let alertMessage =
"Everything will be deleted and you will have to enrol again."
private let alertOKLabel = "Delete"
private let alertCancelLabel = "Cancel"

extension View {
    func alertClearAppKeyChain(isPresented: Binding<Bool>) -> some View {
        // TOTH https://flaviocopes.com/swiftui-alert/
        // Uses the deprecated Alert but still useful to get started.
        return self.alert(alertTitle, isPresented:isPresented) {
            Button(alertCancelLabel, role: .cancel) {}
            Button(alertOKLabel, role: .destructive) { clearAppKeyChain() }
        } message: {
            Text(alertMessage)
        }
    }
}

func alertClearAppKeyChain(_ viewController:UIViewController) {
    let confirm = UIAlertAction(title: alertOKLabel, style: .destructive) {_ in
        clearAppKeyChain()
    }
    let cancel = UIAlertAction(title: alertCancelLabel, style: .cancel) {_ in}
    let alert = UIAlertController(
        title: alertTitle,
        message: alertMessage,
        preferredStyle: .alert
    )
    alert.addAction(confirm)
    alert.addAction(cancel)
    viewController.present(alert, animated: true)
}
