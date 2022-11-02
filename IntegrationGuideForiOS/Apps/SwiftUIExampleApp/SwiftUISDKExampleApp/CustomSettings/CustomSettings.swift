//
//  CustomSettings.swift
//  WS1SDKBridgeTestApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct CustomSettings: View {
    @State private var shouldShowAlert = false
    @State private var customSettingsString: String = ""
    
    var body: some View {
        VStack{
            btnFetchCustomSettings
            if !customSettingsString.isEmpty {
                txtEditorView
            }
            Spacer()
        }
        .padding()
        .navigationBarTitle(String(localized: "customNavigationTitle"))
    }
}

struct CustomSettings_Previews: PreviewProvider {
    static var previews: some View {
        CustomSettings()
    }
}

private extension CustomSettings {
    var txtEditorView: some View {
        TextEditor(text: $customSettingsString)
            .disabled(false)
            .frame( height: 250)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.2), lineWidth: 4)
            )
            .foregroundColor(.black)
    }
    var btnFetchCustomSettings: some View {
        Button{
            guard
                let customPayload = AWSDKHelper.shared.customPayload,
                let customSettings = customPayload.settings,
                customSettings != ""
            else {
                shouldShowAlert = true
                return
            }
            customSettingsString = customSettings
        } label: {
            Text(String(localized: "fetchButtonTitle"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
        .alert(isPresented: $shouldShowAlert) {
            Alert(title: Text(String(localized: "CustomSettingsalertTitle")), message: Text(String(localized: "CustomSettingsalertMessage")), dismissButton: .default(Text(String(localized: "Ok"))))
        }
    }
}
