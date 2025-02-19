# Appendix: Keychain Clearance Sample Code
If you want to add a keychain clearance option to your application, you can use
the code here. The use for keychain clearance is discussed in the
[Appendix: Troubleshooting](../22Appendix_Troubleshooting/readme.md).

The sample code is also published here.  
[github.com/euc-releases/…/KeychainClearance.swift](https://github.com/euc-releases/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForiOS/Guides/23BaseIntegration/23Appendix_Keychain-Clearance-Sample-Code/KeychainClearance.swift)


<p class="allow-page-break" />

    //  Copyright 2023 Omnissa, Inc.
    //  SPDX-License-Identifier: BSD-2-Clause

    import Foundation
    import UIKit
    import SwiftUI

    // Handy extension to get an error message from an OSStatus.
    extension OSStatus {
        var secErrorMessage: String {
            return (SecCopyErrorMessageString(self, nil) as String?) ?? "\(self)"
        }
    }

    func clearAppKeyChain() {
        [
            // List here is in the same order as in the reference documentation.
            kSecClassGenericPassword,
            kSecClassInternetPassword,
            kSecClassCertificate,
            kSecClassKey,
            kSecClassIdentity
        ].forEach {secClass in
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

Acknowledgements

-   Code for the extension to get an error message from an OSStatus.  
    [github.com/omnissa-archive/captive-web-view/…/StoredKey.swift#L37](https://github.com/omnissa-archive/captive-web-view/blob/061c9140fc16f88e3ab44cc170eb0d1f057adc0b/forApple/CaptiveCrypto/CaptiveCrypto/StoredKey.swift#L37)

-   Reference documentation for `kSecClass` constants.  
    [developer.apple.com/…/keychain_items/…](https://developer.apple.com/documentation/security/keychain_services/keychain_items/item_class_keys_and_values#1678477)

-   Query to find all items in a security class.  
    [github.com/omnissa-archive/captive-web-view/…/StoredKey.swift#L184](https://github.com/omnissa-archive/captive-web-view/blob/061c9140fc16f88e3ab44cc170eb0d1f057adc0b/forApple/CaptiveCrypto/CaptiveCrypto/StoredKey.swift#L184)

## License

This software is licensed under the [Omnissa Software Development Kit (SDK) License Agreement](https://static.omnissa.com/sites/default/files/omnissa-sdk-agreement.pdf); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

This software may also utilize Third-Pary Open Source Software as detailed within the [open_source_licenses.txt](open_source_licenses.txt) file.
