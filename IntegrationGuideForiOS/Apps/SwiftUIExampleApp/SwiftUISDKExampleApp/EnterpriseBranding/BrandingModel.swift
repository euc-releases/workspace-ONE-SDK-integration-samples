//
//  BrandingModel.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

enum BrandingType {
    case imageUrl(URL)
    case colorValue(Color)
    case strValue(String)
}

struct BrandingModel: Identifiable {
    var id: UUID = UUID()
    var key: String
    var value: Any
    var brandValue: BrandingType {
        switch value {
        case let childValue as Color:
            return .colorValue(childValue)
        case  let childValue as UIColor:
            return .colorValue(Color(childValue))
        case let childValue as URL:
            return  .imageUrl(childValue)
        case let childValue as String:
            if let url = NSURL(string: childValue),
                UIApplication.shared.canOpenURL(url as URL) {
                return .imageUrl(url as URL)
            } else if childValue.hasPrefix("#"){
                return .colorValue(Color.hexStringToColor(hex: childValue))
            } else {
                return .strValue(childValue)
            }
        default:
            return .strValue("\(value)")
        }
    }
}
