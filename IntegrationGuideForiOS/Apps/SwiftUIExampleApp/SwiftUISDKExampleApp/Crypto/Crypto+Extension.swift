//
//  Crypto+Extension.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation


extension Array where Element == UInt8 {
    var data: Data {
        return Data.init(bytes: self, count: self.count)
    }
}
extension String {
    var data: Data {
        return self.utf8.map { UInt8(bitPattern: Int8($0)) }.data
    }

    var hexData: Data {
        return zip(
            self.enumerated().filter { $0.offset % 2 == 0 }.map { $0.element },
            self.enumerated().filter { $0.offset % 2 == 1 }.map { $0.element }
        ).map { UInt8(String([$0.0, $0.1]), radix: 16) }.compactMap { $0 }.data
    }
}

extension Data {
    var string: String {
        return String(data: self, encoding: .utf8) ?? ""
    }

    var hexString: String {
        return Array(self)
            .map { String(format: "%02x", $0) }
            .reduce("", +)
    }
}
