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
    
    var id: String {
        switch self {
        case .webView: return "WebView"
        case .imageView: return "ImageView"
        case .textView: return "TextView"
        case .avPlayerView: return "AVPlayerView"
        }
    }
    
    var title: String {
        switch self {
        case .webView: return "WebView"
        case .imageView: return "ImageView"
        case .textView: return "TextView"
        case .avPlayerView: return "AVPlayerView"
        }
    }
    
    func destinationView() -> AnyView {
        switch self {
        case .webView:
            return AnyView(ScreenshotWebView())
        case .imageView:
            return AnyView(ScreenshotImageView())
        case .textView:
            return AnyView(ScreenshotTextView())
        case .avPlayerView:
            return AnyView(ScreenshotAVView())
        }
    }
}

struct ScreenshotEntryView: View {
    let items: [ViewType] = [.webView, .imageView, .textView, .avPlayerView]
    
    @State var refreshViewsId: UUID = UUID()
    
    var body: some View {
        List(items) { item in
            let view = item.destinationView()
            let secureView = AnyView(SecureViewRepresentable(refreshTrigger: $refreshViewsId, swiftUIViewToBeSecured: view))
            
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
    
    static func dismantleUIView(_ uiView: UILabel, coordinator: ()) {
        NotificationCenter.default.removeObserver(self, name: AWSDKHelper.shared.refreshSecureViewNotification, object: nil)
    }
}
