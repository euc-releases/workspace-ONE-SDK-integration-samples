//
//  CertificateDetailsView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AWSDK

// Shows all information about certificate by querying to supported certificate attributes.
struct CertificateDetailsView: View {
    static let infoKeys = [
        CertificateInfoKey.subjectName,
        CertificateInfoKey.subjectUserID,
        CertificateInfoKey.subjectIdentifier,
        CertificateInfoKey.emailAddress,
        CertificateInfoKey.commonName,
        CertificateInfoKey.issuer,
        CertificateInfoKey.algorithm,
        CertificateInfoKey.subjectAlternativeName,
        CertificateInfoKey.keyUsage,
        CertificateInfoKey.extendedKeyUsage,
        CertificateInfoKey.universalPrincipalName,
        CertificateInfoKey.rawCertificate,
        CertificateInfoKey.serialNumber,
        CertificateInfoKey.startDate,
        CertificateInfoKey.endDate
    ]

    var certificate: PublicCertificate

    var body: some View {
        List(Self.infoKeys.indices, id: \.self) { index in
            Section {
                VStack(alignment: .leading) {
                    Text(Self.infoKeys[index])
                        .font(.headline)

                    Divider()
                    
                    Text(getInfoValueForKey(keyName: Self.infoKeys[index]))
                    .font(.body)
                    .multilineTextAlignment(.leading)
                }
            }
        }
    }
}

extension CertificateDetailsView {
    func getInfoValueForKey(keyName: String) -> String {
        if let str = self.certificate.value(forCertificateAttribute: keyName) as? String {
           return str
        } else if let date = self.certificate.value(forCertificateAttribute: keyName) as? Date {
            return String(describing: date)
        } else if let data = self.certificate.value(forCertificateAttribute: keyName) as? Data {
            return String(describing: data)
        } else {
            return String(localized: "MissingStringValue")
        }
    }
}
