//
//  CryptionViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import UIKit
import AWSDK

final class CryptionViewController: UIViewController {

    @IBOutlet var contentTextView: UITextView!

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = "Data-at-Rest Encryption"
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Encrypt", style: .plain, target: self, action: #selector(encrypt))
    }


    /// encrypts the data  and shows in UI
    @objc func encrypt() {
        self.navigationItem.rightBarButtonItem?.isEnabled = false
        do {
            // Using Encrypt required user to unlock the container if it locked with a passcode or username/password.
            // Expect to see an error thrown if the container is not unlocked.
            self.contentTextView.text = try AWSDKHelper.shared.encrypt(self.contentTextView.text.data).hexString
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Decrypt", style: .plain, target: self, action: #selector(decrypt))
            self.contentTextView.isEditable = false
        } catch let error {
            AlertHelper.showAlert(title: "Encryption Failed", message: "Error occured when trying to encrypt:\n\(error.localizedDescription)", presenter: self)
        }
        self.navigationItem.rightBarButtonItem?.isEnabled = true
    }


    /// decrypts the data and shows in UI
    @objc func decrypt() {
        self.navigationItem.rightBarButtonItem?.isEnabled = false
        do {
            self.contentTextView.text = try AWSDKHelper.shared.decrypt(self.contentTextView.text.hexData).string
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Encrypt", style: .plain, target: self, action: #selector(encrypt))
            self.contentTextView.isEditable = true
        } catch let error {
            AlertHelper.showAlert(title: "Decryption Failed", message: "Error occured when trying to encrypt:\n\(error.localizedDescription)", presenter: self)
        }
        self.navigationItem.rightBarButtonItem?.isEnabled = true
    }
}

private extension Array where Element == UInt8 {
    var data: Data {
        return Data.init(bytes: self, count: self.count)
    }
}

private extension String {
    var data: Data {
        return self.utf8.map { UInt8(bitPattern: Int8($0)) }.data
    }

    var hexData: Data {
        return zip(
            self.enumerated().filter { $0.offset % 2 == 0 }.map { $0.element },
            self.enumerated().filter { $0.offset % 2 == 1 }.map { $0.element }
        ).map { UInt8(String([$0.0, $0.1]), radix: 16) }.compactMap { $0 }.data
    }
}

private extension Data {
    var string: String {
        return String(data: self, encoding: .utf8) ?? "Encrypted"
    }

    var hexString: String {
        return Array(self)
            .map { String(format: "%02x", $0) }
            .reduce("", +)
    }
}

