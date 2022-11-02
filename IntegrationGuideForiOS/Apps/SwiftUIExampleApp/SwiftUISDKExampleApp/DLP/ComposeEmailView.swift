//
//  ComposeEmailView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import SwiftUI

struct ComposeEmailView: View {
    @State private var showingAlert = false
    @State private var alertMessage = ""
    let alertMessageDLP: String = String(localized: "checkDLPSettings")
    let url = URL(string: "mailto:exampleuser@example.com?cc=exampleuser2@example.com,exampleuser3@example.com&bcc=exampleuser4@example.com&subject=Some%20Important%20Mail&body=Hi%2C%0A%0A%0A")!

    @Environment(\.openURL) private var openURL

    var body: some View {
        Button {
            guard canContinueComposeEmail() else {
                showingAlert = true
                alertMessage = alertMessageDLP
                return
            }
            openURL(url)
        } label: {
            Text(String(localized: "sendMail"))
        }
        .alert(alertMessage, isPresented: $showingAlert) {
            Button(String(localized: "Ok"), role: .cancel) {
                showingAlert = false
            }
        }
    }

    func canContinueComposeEmail() -> Bool {
        return AWSDKHelper.shared.dlpEnabled()
    }
}

struct ComposeEmailView_Previews: PreviewProvider {
    static var previews: some View {
        ComposeEmailView()
    }
}
