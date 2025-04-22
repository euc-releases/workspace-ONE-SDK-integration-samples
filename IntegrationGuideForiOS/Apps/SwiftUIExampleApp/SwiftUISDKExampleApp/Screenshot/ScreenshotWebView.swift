//
//  ScreenshotWebView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2025 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import WebKit

struct ScreenshotWebView: View {
    let urlString = "https://www.google.com"

    var body: some View {
        WebViewRepresentable(urlString: urlString)
            .edgesIgnoringSafeArea(.all)
            .frame(width: 300, height: 200)
    }
}

struct WebViewRepresentable: UIViewRepresentable {
    let urlString: String
    
    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        if let url = URL(string: urlString) {
            let request = URLRequest(url: url)
            webView.load(request)
        }
        return webView
    }
    
    func updateUIView(_ uiView: WKWebView, context: Context) {}
}
