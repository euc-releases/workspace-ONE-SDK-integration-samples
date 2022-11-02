//
//  OpenURL.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import SwiftUI

struct OpenURLView: View {
    @State private var showingAlert = false
    let alertMessageDLP: String = String(localized: "checkDLPSettings")
    let url = URL(string: "https://www.vmware.com")!

    @Environment(\.openURL) private var openURL

    var body: some View {
        Button {
            guard canContinueOpenURL() else {
                showingAlert = true
                return
            }
            openURL(url)
        } label: {
            Text(String(localized: "openURL"))
        }
        .alert(alertMessageDLP, isPresented: $showingAlert) {
            Button(String(localized: "Ok"), role: .cancel) {
                showingAlert = false
            }
        }
    }

    func canContinueOpenURL() -> Bool {
        return AWSDKHelper.shared.dlpEnabled()
    }
}

struct OpenURLView_Previews: PreviewProvider {
    static var previews: some View {
        OpenURLView()
    }
}
