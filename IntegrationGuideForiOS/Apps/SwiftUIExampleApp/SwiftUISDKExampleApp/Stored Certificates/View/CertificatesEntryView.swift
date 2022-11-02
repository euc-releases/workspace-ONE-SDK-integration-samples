//
//  CertificatesEntryView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AWSDK

struct CertificatesEntryView: View {

    static let keys = [
        CertificateUsageKey.integratedAuthIdentity : "Integrated Authentication",
        CertificateUsageKey.magSigning : "Proxy Signing",
        CertificateUsageKey.tunnelSigning : "Tunnel Signing",
        CertificateUsageKey.selfSignedSSLCerts : "Self Signed SSL Certificates",
        CertificateUsageKey.customTrustedAnchorCerts: "Custom Trusted Anchors",
        CertificateUsageKey.uncategorizedIdentity: "Uncategorized Identity Certs",
        CertificateUsageKey.others : "Others"
    ]

    @StateObject var model: CertificatesViewModel
    init(){
        self._model = StateObject(wrappedValue: CertificatesViewModel())
    }
    
    var body: some View {
        List {
            if let certificate = self.model.certificates {
                switch certificate {
                    case .failure(let error):
                        Section {
                            HStack {
                                Image(systemName: "x.circle.fill")
                                    .foregroundColor(.red)
                                Text(error.localizedDescription)
                            }
                        }

                    case .success(let certificates) where certificates.isEmpty:
                        Section {
                            HStack {
                                Image(systemName: "exclamationmark.triangle.fill")
                                    .foregroundColor(.yellow)
                                Text(String(localized: "EmptyCertificateList"))
                            }
                        }

                    case .success(let certificates):
                        ForEach(certificates.map { $0 }, id: \.key) { pair in
                            if let title = Self.keys[pair.key] {
                                Section {
                                    NavigationLink {
                                        CertificatesListView(certificates: pair.value)
                                    } label: {
                                        HStack {
                                            Image(systemName: "mail.stack.fill")
                                            Text(title)
                                        }
                                    }
                                }
                            }
                        }
                }
            } else {
                HStack {
                    Spacer()
                    ProgressView()
                    Spacer()
                }
            }
        }.onAppear {
            self.model.refresh()
        }
    }
}

struct CertificatesEntryView_Previews: PreviewProvider {
    static var previews: some View {
        CertificatesEntryView()
    }
}
