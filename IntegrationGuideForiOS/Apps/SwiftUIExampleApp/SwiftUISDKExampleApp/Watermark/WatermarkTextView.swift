//
//  WatermarkTextView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2026 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct WatermarkTextView: View {
    @State private var text = "This is a TextView with watermark overlay. It is editable!"

    var body: some View {
        VStack {
            Spacer()
            WatermarkViewRepresentable(
                swiftUIViewToBeWatermarked: AnyView(
                    TextEditor(text: $text)
                        .font(.title)
                )
            )
            .frame(width: 300, height: 600)
            .border(Color.gray, width: 1)
            Spacer()
        }
    }
}
