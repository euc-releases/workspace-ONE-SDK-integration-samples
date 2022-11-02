//
//  CertificatesViewModel.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import AWSDK
    
@MainActor
class CertificatesViewModel: ObservableObject {
    @Published var certificates: Swift.Result<[String: [PublicCertificate]], Error>?

    func refresh() {
        AWController.clientInstance().retrieveStoredPublicCertificates { certificates, error in
            Task { @MainActor in
                if let error = error {
                    self.certificates = .failure(error)
                } else {
                    self.certificates = .success(certificates ?? [:])
                }
            }
        }
    }
}
