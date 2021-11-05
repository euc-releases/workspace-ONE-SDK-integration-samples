//
//  DocumentInteractionViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

/// SDK Allows restriction on document sharing within mobile application. Set 'EnableSecureDocumentController' property to YES in AWSDKDefaultSettings.plist to control the sharing of document through UIDocumentInteractionController
class DocumentInteractionViewController: UIViewController {

    private var documentInteractionController: UIDocumentInteractionController?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        guard let filePath = Bundle.main.path(forResource: "PDF_Extension_check", ofType: "pdf") else {
            print("File not found")
            return
        }
        let fileURL = URL(fileURLWithPath: filePath)
        documentInteractionController = UIDocumentInteractionController(url: fileURL)
        documentInteractionController?.delegate = self
    }

    /// if 'EnableSecureDocumentController' is set to YES some sharing option in UIDocumentInteractionController won't be available.
    @IBAction func previewDocument(_ sender: Any) {
        self.documentInteractionController?.presentPreview(animated: true)
    }

    /// if 'EnableSecureDocumentController' is set to YES opens the list of authorised apps in which sharing of documents is allowed.
    @IBAction func openDocument(_ sender: UIButton) {
        self.documentInteractionController?.presentOpenInMenu(from: sender.frame, in: self.view, animated: true)
    }

    /// 'DisableActivityViewController' property restrict the sharing of Data through UIActivityViewController.                                                                                                         Don't set 'DisableActivityViewController' property to YES if we use UIDocumentInteractionController or  if 'EnableSecureDocumentController' property is set to YES.
    @IBAction func goToAppleSite(_ sender: UIButton) {
        guard let url = URL(string: "https://www.apple.com") else {
            return
        }
        let items = [url]
        let viewController = UIActivityViewController(activityItems: items , applicationActivities: [])
        if UIDevice.current.userInterfaceIdiom == .pad {
            if let popover = viewController.popoverPresentationController {
                popover.sourceView = sender
            }
        }
        present(viewController, animated: true)
    }
}

extension DocumentInteractionViewController: UIDocumentInteractionControllerDelegate {
    func documentInteractionControllerViewControllerForPreview(_ controller: UIDocumentInteractionController) -> UIViewController {
        return self
    }

    // Options menu presented/dismissed on document.  Use to set up any HI underneath.
    func documentInteractionControllerWillPresentOptionsMenu(_ controller: UIDocumentInteractionController) {
        NSLog("documentInteractionControllerWillPresentOptionsMenu")
    }

    func documentInteractionControllerDidDismissOptionsMenu(_ controller: UIDocumentInteractionController) {
        NSLog("documentInteractionControllerDidDismissOptionsMenu")
    }
}
