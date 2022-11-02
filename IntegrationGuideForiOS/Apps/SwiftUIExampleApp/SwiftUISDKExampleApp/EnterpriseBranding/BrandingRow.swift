//
//  BrandingRow.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct BrandingRow: View {
    var brandingInfo: BrandingModel
    var body: some View {
        switch brandingInfo.brandValue {
            case  .colorValue(let color):
                HStack{
                    VStack(alignment: .leading){
                        Text(brandingInfo.key)
                        Text(color.hexString)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    Spacer()
                    Rectangle()
                        .frame(width: 50, height: 50, alignment: .trailing)
                        .foregroundColor(color)
                }
            case .imageUrl(let imgUrl):
                HStack{
                    VStack(alignment: .leading){
                        Text(brandingInfo.key)
                        Text("\(imgUrl)")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    Spacer()
                    AvatarView(url: imgUrl)
                }
            case .strValue(let string):
                HStack{
                    VStack(alignment: .leading){
                        Text(brandingInfo.key)
                        Text(string)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                }
        }
    }
    
}
