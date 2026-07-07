//
//  WatermarkButtonView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2026 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct WatermarkButtonView: View {
    var body: some View {

            WatermarkViewRepresentable(
                swiftUIViewToBeWatermarked: AnyView(
                    Button("Outlined Button") {}
                        .font(.headline)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color.blue, lineWidth: 2)
                        )
                )
            )
            .frame(height: 50)
            .padding(.horizontal, 40)

    }
}
