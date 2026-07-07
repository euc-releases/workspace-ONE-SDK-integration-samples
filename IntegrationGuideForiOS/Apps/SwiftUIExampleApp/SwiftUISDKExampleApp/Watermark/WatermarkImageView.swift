//
//  WatermarkImageView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2026 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct WatermarkImageView: View {
    var body: some View {
        VStack {
            Spacer()
            WatermarkViewRepresentable(
                swiftUIViewToBeWatermarked: AnyView(
                    Image("ImageSDKIcon")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                )
            )
            .frame(width: 300, height: 600)
            Spacer()
        }
    }
}
