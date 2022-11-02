//
//  CertificatesListView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import AWSDK
import SwiftUI
    
struct CertificatesListView: View {
    var certificates: [PublicCertificate]

    var body: some View {
        List(self.certificates.indices, id: \.self) { index in
            NavigationLink {
                CertificateDetailsView(certificate: self.certificates[index])
            } label: {
                HStack {
                    Image(systemName: "doc.text.image")
                    Text(
                        (self.certificates[index].value(forCertificateAttribute: CertificateInfoKey.commonName) as? String) ?? (String(localized: "UnknownName"))
                    )
                }
            }
        }
    }
}
