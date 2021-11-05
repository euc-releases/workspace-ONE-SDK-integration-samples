//
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import AWSDK

class LoadingIndicatorView {

    static var blockerScreen: UIView?

    static func show() {
        ensureOnMainThread {
            guard let blockerWindow = UIApplication.shared.keyWindow else {
                AWLogError("No main window.")
                return
            }
            self.show(blockerWindow)
        }
    }

    static func show(_ loadingText: String) {
        ensureOnMainThread {
            guard let blockerWindow = UIApplication.shared.keyWindow else {
                AWLogError("No main window.")
                return
            }
            self.show(inView: blockerWindow, loadingText: loadingText)
        }
    }

    static func show(_ callerViewTarget: UIView) {
        self.show(inView: callerViewTarget, loadingText: nil)
    }

    static func show(inView: UIView, loadingText: String?) {
        // Hiding the Blocker if it's already being shown
        self.hide()

        // Creaing the Blocker screen
        let localBlocker = UIView(frame: inView.frame)
        localBlocker.center = inView.center
        localBlocker.alpha = 0
        localBlocker.backgroundColor = UIColor.black
        inView.addSubview(localBlocker)
        inView.bringSubviewToFront(localBlocker)

        // Setting the Loading Spinner
        let loadingSpinner = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.white)
        loadingSpinner.center = localBlocker.center
        loadingSpinner.startAnimating()
        localBlocker.addSubview(loadingSpinner)

        // Setting the text for the loading spinner
        if let textString = loadingText {
            let loadingText = UILabel()
            loadingText.text = textString
            loadingText.textColor = UIColor.white
            loadingText.sizeToFit()
            loadingText.center = CGPoint(x: loadingSpinner.center.x, y: loadingSpinner.center.y + 30)
            localBlocker.addSubview(loadingText)
        }

        // Showing the blocker and animating
        UIView.beginAnimations(nil, context: nil)
        UIView.setAnimationDuration(0.5)
        localBlocker.alpha = localBlocker.alpha > 0 ? 0 : 0.5
        UIView.commitAnimations()

        self.blockerScreen = localBlocker
    }

    static func hide() {
        guard let blockerScreen = self.blockerScreen else {
            return
        }
        blockerScreen.removeFromSuperview()
        self.blockerScreen = nil
    }
}

