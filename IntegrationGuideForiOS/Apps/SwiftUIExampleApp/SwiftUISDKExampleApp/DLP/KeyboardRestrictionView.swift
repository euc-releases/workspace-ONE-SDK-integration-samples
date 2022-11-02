//
//  KeyboardRestrictionView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct KeyboardRestrictionView: View {
    let instructionText = String(localized: "keyboardInstruction")
    @State private var enteredText: String = ""

    var body: some View {
        VStack {
            Text(instructionText)
                .textSelection(.enabled)
            TextField(
                String(localized: "keyboardTextPlaceholder"),
                text: $enteredText
            )
            Spacer()
        }
    }
}

struct KeyboardRestrictionView_Previews: PreviewProvider {
    static var previews: some View {
        KeyboardRestrictionView()
    }
}
