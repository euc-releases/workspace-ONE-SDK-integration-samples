//
//  WKWebViewUIRepresentable.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import SwiftUI
import WebKit

struct AWWKWebView: UIViewRepresentable {
    typealias UIViewType = WKWebView

    let webWKView: WKWebView

    func makeUIView(context: Context) -> WKWebView {
        return webWKView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) { }
}
