//
//  Utils.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//
import Foundation
import AWSDK
import UIKit

extension String {

    // MARK:- Helper methods

    public var domainStrippedUsername: String {
        let usernameComponents = self.components(separatedBy: "\\")
        return usernameComponents.count > 1 ? usernameComponents[1] : self
    }

}

public func ensureOnMainThread(completion: () -> Void) {
    if Thread.isMainThread {
        completion()
    } else {
        DispatchQueue.main.sync(execute: completion)
    }
}

enum LoadingIndicator {
    static func show() -> () {
        ensureOnMainThread {
            UIApplication.shared.isNetworkActivityIndicatorVisible = true
        }
    }

    static func hide() -> () {
        ensureOnMainThread {
            UIApplication.shared.isNetworkActivityIndicatorVisible = false
        }
    }
}


extension UIImageView {

    /// function to download the image from URL and assign to ImageView
    /// - Parameter url: URL to load images
    func load(url: URL) {
        DispatchQueue.global().async { [weak self] in
            if let data = try? Data(contentsOf: url) {
                if let image = UIImage(data: data) {
                    DispatchQueue.main.async {
                        guard let weakSelf = self else {
                          return
                        }
                        weakSelf.image = image
                    }
                }
            }
        }
    }
}


