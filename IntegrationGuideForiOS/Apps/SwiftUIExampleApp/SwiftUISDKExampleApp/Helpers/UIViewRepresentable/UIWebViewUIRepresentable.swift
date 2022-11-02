//
//  UIWebViewUIRepresentable.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import SwiftUI
import WebKit

struct AWUIWebView: UIViewRepresentable {
    typealias UIViewType = UIWebView

    let webUIView: UIWebView

    func makeUIView(context: Context) -> UIWebView {
        return webUIView
    }

    func updateUIView(_ uiView: UIWebView, context: Context) { }
}
