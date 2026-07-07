//
//  WatermarkEntireView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2026 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AWSDK

struct WatermarkEntireView: View {
    var body: some View {
        GeometryReader { geometry in
            WatermarkViewRepresentable(
                swiftUIViewToBeWatermarked: AnyView(
                    ScrollView {
                        VStack(spacing: 20) {
                            Text("Mixed UI Elements")
                                .font(.title2)
                                .bold()

                            Image(systemName: "lock.shield.fill")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 80, height: 80)
                                .foregroundColor(.blue)

                            Text("This screen demonstrates the watermark overlay applied to the entire view. All UI elements are covered by a single watermark layer.")
                                .multilineTextAlignment(.center)
                                .font(.body)
                                .padding(.horizontal, 30)

                            TextField("Sample text field", text: .constant(""))
                                .textFieldStyle(.roundedBorder)
                                .padding(.horizontal, 40)

                            Button("Sample Button") {}
                                .font(.headline)
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                                .padding(.horizontal, 40)

                            Text("Watermark enabled: \(AWSDKWatermarkManager.sharedInstance.isWatermarkEnabled ? "true" : "false")\nWatermark text: \(AWSDKWatermarkManager.sharedInstance.watermarkText)")
                                .font(.caption)
                                .foregroundColor(.secondary)
                                .multilineTextAlignment(.center)
                        }
                        .padding()
                        .frame(width: geometry.size.width)
                    }
                )
            )
        }
    }
}
