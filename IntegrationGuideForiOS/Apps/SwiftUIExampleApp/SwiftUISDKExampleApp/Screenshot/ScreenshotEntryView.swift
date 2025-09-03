//
//  ScreenshotEntryView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2025 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

enum ViewType: Identifiable {
    case webView
    case imageView
    case textView
    case avPlayerView
    case webViewWithMessage
    case imageViewWithMessage
    case textViewWithMessage
    case avplayerViewWithMessage
    case webViewShowMessageFalse
    case imageViewShowMessageFalse
    case textViewShowMessageFalse
    case avplayerViewShowMessageFalse
    
    var id: String {
        switch self {
        case .webView: return "WebView"
        case .imageView: return "ImageView"
        case .textView: return "TextView"
        case .avPlayerView: return "AVPlayerView"
        case .webViewWithMessage: return "WebViewWithMessage"
        case .imageViewWithMessage: return "ImageViewWithMessage"
        case .textViewWithMessage: return "TextViewWithMessage"
        case .avplayerViewWithMessage: return "AVPlayerViewWithMessage"
        case .webViewShowMessageFalse: return "WebViewWithMessageFalse"
        case .imageViewShowMessageFalse: return "ImageViewWithMessageFalse"
        case .textViewShowMessageFalse: return "TextViewWithMessageFalse"
        case .avplayerViewShowMessageFalse: return "AVPlayerViewWithMessageFalse"
        }
    }
    
    var title: String {
        switch self {
        case .webView: return "WebView - Deprecated API"
        case .imageView: return "ImageView - Deprecated API"
        case .textView: return "TextView - Deprecated API"
        case .avPlayerView: return "AVPlayerView - Deprecated API"
        case .webViewWithMessage: return "WebView ShouldShowMessage true"
        case .imageViewWithMessage: return "ImageView ShouldShowMessage true"
        case .textViewWithMessage: return "TextView ShouldShowMessage true"
        case .avplayerViewWithMessage: return "AVPlayerView ShouldShowMessage true"
        case .webViewShowMessageFalse: return "WebView ShouldShowMessage false"
        case .imageViewShowMessageFalse: return "ImageView ShouldShowMessage false"
        case .textViewShowMessageFalse: return "TextView ShouldShowMessage false"
        case .avplayerViewShowMessageFalse: return "AVPlayerView ShouldShowMessage false"
        }
    }
    
    func destinationView() -> AnyView {
        switch self {
        case .webView, .webViewWithMessage, .webViewShowMessageFalse:
            return AnyView(ScreenshotWebView())
        case .imageView, .imageViewWithMessage, .imageViewShowMessageFalse:
            return AnyView(ScreenshotImageView())
        case .textView, .textViewWithMessage, .textViewShowMessageFalse:
            return AnyView(ScreenshotTextView())
        case .avPlayerView, .avplayerViewWithMessage, .avplayerViewShowMessageFalse:
            return AnyView(ScreenshotAVView())
        }
    }
}

struct ScreenshotEntryView: View {
    let itemsWithDeprecatedAPI: [ViewType] = [.webView, .imageView, .textView, .avPlayerView]
    let itemsWithShowMessageTrue: [ViewType] = [.webViewWithMessage, .imageViewWithMessage, .textViewWithMessage, .avplayerViewWithMessage]
    let itemsWithShowMessageFalse: [ViewType] = [.webViewShowMessageFalse, .imageViewShowMessageFalse, .textViewShowMessageFalse, .avplayerViewShowMessageFalse]
    
    @State var refreshViewsId: UUID = UUID()
    
    var body: some View {
        List(itemsWithDeprecatedAPI) { item in
            let view = item.destinationView()
            let secureView = AnyView(SecureViewRepresentable(refreshTrigger: $refreshViewsId, swiftUIViewToBeSecured: view))
            
            NavigationLink(destination: secureView.id(refreshViewsId)) {
                Text(item.title)
            }
        }
        
        List(itemsWithShowMessageTrue) { item in
            let view = item.destinationView()
            let secureView = AnyView(SecureViewWithMessageRepresentable(refreshTrigger: $refreshViewsId, swiftUIViewToBeSecured: view, shouldShowMessage: true))
            
            NavigationLink(destination: secureView.id(refreshViewsId)) {
                Text(item.title)
            }
        }
        
        List(itemsWithShowMessageFalse) { item in
            let view = item.destinationView()
            let secureView = AnyView(SecureViewWithMessageRepresentable(refreshTrigger: $refreshViewsId, swiftUIViewToBeSecured: view, shouldShowMessage: false))
            
            NavigationLink(destination: secureView.id(refreshViewsId)) {
                Text(item.title)
            }
        }
        .navigationBarTitle(Text(String(localized: "ScreenshotNavigationTitle")), displayMode: .inline)
    }
}


// SwiftUI wrapper since secureView from SDK is an UIView
struct SecureViewRepresentable: UIViewRepresentable {
    
    // A trigger to force a reload
    @Binding var refreshTrigger: UUID
    var swiftUIViewToBeSecured:AnyView
    
    class Coordinator: NSObject {
        var notificationObserver: NSObjectProtocol?
        var parent: SecureViewRepresentable
        
        init(parent: SecureViewRepresentable) {
            self.parent = parent
            
            super.init()
            
            notificationObserver = NotificationCenter.default.addObserver(forName: AWSDKHelper.shared.refreshSecureViewNotification, object: nil, queue: .main) { [weak self] _ in
                // Trigger refresh by updating the refreshID
                self?.parent.refreshTrigger = UUID()
            }
        }
    }
    
    func makeCoordinator() -> Coordinator {
        return Coordinator(parent: self)
    }
    
    func makeUIView(context: Context) -> UIView {
        let hostingController = UIHostingController(rootView: swiftUIViewToBeSecured)
        
        let secureView = AWSDKHelper.shared.getSDKSecureView()
        secureView.addSubview(hostingController.view)
        
        hostingController.view.translatesAutoresizingMaskIntoConstraints = false
        return secureView
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {
    }
    
    static func dismantleUIView(_ uiView: UIView, coordinator: ()) {
        NotificationCenter.default.removeObserver(self, name: AWSDKHelper.shared.refreshSecureViewNotification, object: nil)
    }
}

struct SecureViewWithMessageRepresentable: UIViewRepresentable {
    
    // A trigger to force a reload
    @Binding var refreshTrigger: UUID
    var swiftUIViewToBeSecured:AnyView
    var shouldShowMessage: Bool = false
    
    class Coordinator: NSObject {
        var notificationObserver: NSObjectProtocol?
        var parent: SecureViewWithMessageRepresentable
        
        init(parent: SecureViewWithMessageRepresentable) {
            self.parent = parent
            
            super.init()
            
            notificationObserver = NotificationCenter.default.addObserver(forName: AWSDKHelper.shared.refreshSecureViewNotification, object: nil, queue: .main) { [weak self] _ in
                // Trigger refresh by updating the refreshID
                self?.parent.refreshTrigger = UUID()
            }
        }
    }
    
    func makeCoordinator() -> Coordinator {
        return Coordinator(parent: self)
    }
    
    func makeUIView(context: Context) -> UIView {
        let hostingController = UIHostingController(rootView: swiftUIViewToBeSecured)
        let secureView = AWSDKHelper.shared.getSecuredView(for: hostingController.view, shouldShowRestrictionMessage: shouldShowMessage)
        secureView.translatesAutoresizingMaskIntoConstraints = true
        return secureView
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {
    }
    
    static func dismantleUIView(_ uiView: UIView, coordinator: ()) {
        NotificationCenter.default.removeObserver(self, name: AWSDKHelper.shared.refreshSecureViewNotification, object: nil)
    }
}
