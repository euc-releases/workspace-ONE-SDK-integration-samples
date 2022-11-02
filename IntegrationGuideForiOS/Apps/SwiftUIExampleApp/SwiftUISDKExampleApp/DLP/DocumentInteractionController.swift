//
//  DocumentInteractionController.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause


import SwiftUI

/*
 "Limit Documents to Open Only in Approved Apps" DLP setting has to be set to true.
 EnableSecureDocumentController Bool has to be set in AWSDKDefaultSettings.plist
 DisableActivityViewController Bool has to be set in AWSDKDefaultSettings.plist
 Note: When DisableActivityViewController is set to true, remember to disable EnableSecureDocumentController.
 */

struct DocumentInteractionController: View {
    @State private var openAppleSite = false
    var documentController = DocumentController()
    var body: some View {
        VStack(alignment: .center, spacing: 20, content: {
            
            Button {
                documentController.presentDocument()
            } label: {
                Text(String(localized: "previewPdf"))
            }

            Button {
                documentController.presentShare()
            } label: {
                Text(String(localized: "sharePDF"))
            }

            Button {
                openAppleSite = true
            } label: {
                Text(String(localized: "openApple"))
            }
            .sheet(isPresented: $openAppleSite, onDismiss: {
                print("Dismiss")
            }, content: {
                ActivityViewController(activityItems: [URL(string: "https://www.apple.com")!])
            })
        })
    }
}

class DocumentController: NSObject, ObservableObject, UIDocumentInteractionControllerDelegate {
    let controller = UIDocumentInteractionController(url: Bundle.main.url(forResource: "PDF_Extension_check", withExtension: "pdf")!)
    func presentDocument() {
        controller.delegate = self
        controller.presentPreview(animated: true)
    }

    func presentShare() {
        controller.delegate = self
        controller.presentOpenInMenu(from: CGRect(), in: (UIApplication.shared.rootViewController?.view)!, animated: true)
    }

    func documentInteractionControllerViewControllerForPreview(_: UIDocumentInteractionController) -> UIViewController {
        return UIApplication.shared.rootViewController!
    }
}


struct ActivityViewController: UIViewControllerRepresentable {

    var activityItems: [Any]
    var applicationActivities: [UIActivity]? = nil

    func makeUIViewController(context: UIViewControllerRepresentableContext<ActivityViewController>) -> UIActivityViewController {
        let controller = UIActivityViewController(activityItems: activityItems, applicationActivities: applicationActivities)
        return controller
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: UIViewControllerRepresentableContext<ActivityViewController>) {}
}

extension UIApplication {
    var currentKeyWindow: UIWindow? {
        UIApplication.shared.connectedScenes
            .filter { $0.activationState == .foregroundActive }
            .map { $0 as? UIWindowScene }
            .compactMap { $0 }
            .first?.windows
            .filter { $0.isKeyWindow }
            .first
    }

    var rootViewController: UIViewController? {
        currentKeyWindow?.rootViewController
    }
}
