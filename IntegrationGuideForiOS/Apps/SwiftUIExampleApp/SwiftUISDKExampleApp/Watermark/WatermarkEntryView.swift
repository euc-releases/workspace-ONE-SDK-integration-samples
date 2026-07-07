//
//  WatermarkEntryView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2026 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AWSDK

enum WatermarkViewType: Identifiable {
    case webView
    case imageView
    case textView
    case buttonView
    case entireView

    var id: String {
        switch self {
        case .webView: return "WatermarkWebView"
        case .imageView: return "WatermarkImageView"
        case .textView: return "WatermarkTextView"
        case .buttonView: return "WatermarkButtonView"
        case .entireView: return "WatermarkEntireView"
        }
    }

    var title: String {
        switch self {
        case .webView: return "Watermark on WebView"
        case .imageView: return "Watermark on ImageView"
        case .textView: return "Watermark on TextView"
        case .buttonView: return "Watermark on Button"
        case .entireView: return "Watermark on Entire View"
        }
    }

    @ViewBuilder
    func destinationView() -> some View {
        switch self {
        case .webView:
            WatermarkWebView()
        case .imageView:
            WatermarkImageView()
        case .textView:
            WatermarkTextView()
        case .buttonView:
            WatermarkButtonView()
        case .entireView:
            WatermarkEntireView()
        }
    }
}

struct WatermarkEntryView: View {
    let items: [WatermarkViewType] = [.webView, .imageView, .textView, .buttonView, .entireView]

    var body: some View {
        List(items) { item in
            NavigationLink(destination: item.destinationView()) {
                Text(item.title)
            }
        }
        .navigationBarTitle(Text("Watermark"), displayMode: .inline)
    }
}

// MARK: - Reusable Watermark UIViewRepresentable
/// A single reusable UIViewRepresentable that hosts a SwiftUI view and applies
/// the SDK watermark overlay on it. Each content view uses this internally
/// to wrap only the specific element that needs the watermark.
struct WatermarkViewRepresentable: UIViewRepresentable {
    var swiftUIViewToBeWatermarked: AnyView

    class Coordinator: NSObject {
        var notificationObserver: NSObjectProtocol?
        var hostingController: UIHostingController<AnyView>?
        weak var watermarkedView: UIView?

        override init() {
            super.init()
            notificationObserver = NotificationCenter.default.addObserver(
                forName: .refreshWatermark,
                object: nil,
                queue: .main
            ) { [weak self] _ in
                guard let view = self?.watermarkedView else { return }
                view.removeWaterMarkOverlayView()
                view.addWaterMarkOverlayView()
            }
        }

        deinit {
            if let observer = notificationObserver {
                NotificationCenter.default.removeObserver(observer)
            }
        }
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> UIView {
        let hostingController = UIHostingController(rootView: swiftUIViewToBeWatermarked)
        hostingController.view.clipsToBounds = true
        hostingController.view.addWaterMarkOverlayView()
        context.coordinator.hostingController = hostingController
        context.coordinator.watermarkedView = hostingController.view
        return hostingController.view
    }

    func updateUIView(_ uiView: UIView, context: Context) {
        context.coordinator.hostingController?.rootView = swiftUIViewToBeWatermarked
    }
}
