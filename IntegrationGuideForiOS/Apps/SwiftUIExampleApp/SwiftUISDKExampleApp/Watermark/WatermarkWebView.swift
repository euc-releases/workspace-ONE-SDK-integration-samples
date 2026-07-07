//
//  WatermarkWebView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2026 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import WebKit

struct WatermarkWebView: View {
    let urlString = "https://www.omnissa.com"

    var body: some View {
        VStack {
            Spacer()
            WatermarkViewRepresentable(
                swiftUIViewToBeWatermarked: AnyView(
                    WebViewWrapper(urlString: urlString)
                )
            )
            .frame(width: 300, height: 600)
            Spacer()
        }
    }
}

struct WebViewWrapper: UIViewRepresentable {
    let urlString: String

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        if let url = URL(string: urlString) {
            webView.load(URLRequest(url: url))
        }
        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {}
}
