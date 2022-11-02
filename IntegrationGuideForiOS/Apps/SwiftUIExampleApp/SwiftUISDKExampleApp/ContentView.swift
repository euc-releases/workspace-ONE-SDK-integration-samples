//
//  ContentView.swift
//  SwiftExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AWSDK

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

struct ContentView: View {
    @StateObject var awsdkHelper = AWSDKHelper.shared
    let featureList = [ Feature(name: String(localized: "GeneralInformation"), destination: AnyView(InformationView())),
                        Feature(name: String(localized: "DLP"), destination: AnyView(DLPView())),
                        Feature(name: String(localized: "Encryption"), destination: AnyView(CryptoView())),
                        Feature(name: String(localized: "RemoteConfiguration"), destination: AnyView(CustomSettings())),
                        Feature(name: String(localized: "Beacon"), destination: AnyView(DeviceCompromisedStatusView())),
                        Feature(name: String(localized: "Branding"), destination: AnyView(EnterpriseBrandingView())),
                        Feature(name: String(localized: "IAuthentication"), destination: AnyView(IntegratedAuthenticationView())),
                        Feature(name: String(localized: "AppTunneling"), destination: AnyView(TunnelingView())),
                        Feature(name: String(localized: "Logging"), destination: AnyView(LoggingView())),
                        Feature(name: String(localized: "Certificates"), destination: AnyView(CertificatesEntryView()))
                      ]

    var body: some View {
        NavigationView{
            List(featureList) { feature in
                FeatureRow(title: feature.name, destination: feature.destination)
            }
            .navigationBarTitle(String(localized: "homeNavigationTitle"))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItemGroup(placement: .bottomBar) {
                    Button(String(localized: "clearKeychainTitle")) {
                        AWLogInfo("Clearing keychain")
                        ClearKeychainHelper.clearKeychain()
                    }
                }
            }
        }
        .navigationViewStyle(.stack)
        .alert(item: $awsdkHelper.alert) { alert in
            Alert(title: Text(alert.title), message: Text(alert.message), dismissButton: .default(Text(String(localized: "Dismiss"))))
        }
    }
}


