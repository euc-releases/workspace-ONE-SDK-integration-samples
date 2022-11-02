//
//  CopyPasteView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct CopyPasteView: View {
    @State private var enteredText: String = ""
    var body: some View {
        VStack {
            Text(String(localized: "tryCopy"))
                .textSelection(.enabled)
            TextField(
                String(localized: "tryPaste"),
                text: $enteredText
            )
            Spacer()
        }
        .navigationBarTitle(Text(String(localized: "DLPNavigationTitle")), displayMode: .inline)
    }
}

struct CopyPasteView_Previews: PreviewProvider {
    static var previews: some View {
        CopyPasteView()
    }
}
