//
//  AvatarView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct AvatarView: View {
    let url: URL

    var body: some View {
        AsyncImage(
            url: url,
            transaction: Transaction(animation: .linear)
        ) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case .success(let image):
                image
                    .resizable()
                    .transition(.scale(scale: 0.1, anchor: .center))
                    .aspectRatio(contentMode: .fit)
                    
            case .failure:
                Image(systemName: "wifi.slash")
                    .foregroundColor(.white)
            @unknown default:
                EmptyView()
            }
        }
        .frame(minWidth: 44, idealWidth: 56, maxWidth: 100, minHeight: 44, idealHeight: 56, maxHeight: 100)
        .background(.gray)
        .clipShape(Circle())
    }
}
