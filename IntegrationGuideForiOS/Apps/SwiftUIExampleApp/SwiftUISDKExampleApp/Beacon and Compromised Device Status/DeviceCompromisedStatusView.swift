//
//  DeviceCompromisedStatusView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct DeviceCompromisedStatusView: View {
    @State private var deviceCompromisedStatusString: String = ""
    @State private var deviceSendBeaconStatusString: String = ""
    
    var body: some View {
        VStack{
            btnCheckDeviceCompromisedStatus
            if !deviceCompromisedStatusString.isEmpty {
                deviceCompromisedStatusTextEditorView
            }
            
            btnSendDeviceStatusBeacon
            if !deviceSendBeaconStatusString.isEmpty {
                deviceSendBeaconStatusTextEditorView
            }
            Spacer()
        }
        .padding()
        .navigationBarTitle(String(localized: "CompromisedNavigationTitle"), displayMode: .inline)
    }
}

struct DeviceCompromisedStatusView_Previews: PreviewProvider {
    static var previews: some View {
        DeviceCompromisedStatusView()
    }
}

private extension DeviceCompromisedStatusView {
    var deviceCompromisedStatusTextEditorView: some View {
        TextEditor(text: $deviceCompromisedStatusString)
            .disabled(false)
            .frame( height: 100)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.2), lineWidth: 4)
            )
            .foregroundColor(.black)
    }
    var deviceSendBeaconStatusTextEditorView: some View {
        TextEditor(text: $deviceSendBeaconStatusString)
            .disabled(false)
            .frame( height: 300)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.2), lineWidth: 4)
            )
            .foregroundColor(.black)
    }
    var btnCheckDeviceCompromisedStatus: some View {
        Button{
            AWSDKHelper.shared.checkCompromisedStatus { isJailbroken, evaluationToken, identifier in
                DispatchQueue.main.async {
                    if isJailbroken {
                        deviceCompromisedStatusString = String(localized: "compromisedMessage")
                    }
                    else{
                        deviceCompromisedStatusString = String(localized: "NotCompromisedMessage")
                    }

                }
            }
        } label: {
            Text(String(localized: "checkCompromisedTitle"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
    }
    
    var btnSendDeviceStatusBeacon: some View {
        Button{
            AWSDKHelper.shared.sendBeaconData { success, error in
                DispatchQueue.main.async {
                    deviceSendBeaconStatusString = "\(success ? "Sent" : "Error, \(String(describing: error))")"
                }
            }
        } label: {
            Text(String(localized: "beaconTitle"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
    }
}
