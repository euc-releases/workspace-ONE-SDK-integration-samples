//
//  ScreenshotTextView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2025 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct ScreenshotTextView: View {
    @State private var text = "This is a TextView. It is editable!"
    
    var body: some View {
        TextEditor(text: $text)
            .padding()
            .border(Color.gray, width: 1)
            .font(.title)
            .frame(width: 300, height: 200)
            .padding()
    }
}
