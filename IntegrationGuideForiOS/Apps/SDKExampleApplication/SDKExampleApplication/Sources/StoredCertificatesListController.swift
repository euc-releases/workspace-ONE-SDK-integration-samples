//
//  StoredCertificatesListController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import AWSDK

/// Class fetches the latest set of supported certificate types which are stored and shows  in tableview .
class StoredCertificatesListController: UITableViewController {

    private var labelMap = [
        CertificateUsageKey.integratedAuthIdentity : "Integrated Authentication",
        CertificateUsageKey.magSigning : "Proxy Signing",
        CertificateUsageKey.tunnelSigning : "Tunnel Signing",
        CertificateUsageKey.selfSignedSSLCerts : "Self Signed SSL Certificates",
        CertificateUsageKey.customTrustedAnchorCerts: "Custom Trusted Anchors",
        CertificateUsageKey.uncategorizedIdentity: "Uncategorized Identity Certs",
        CertificateUsageKey.others : "Others"
    ]

    private var certificatesMap: [String: [PublicCertificate]]?
    /// stores the available certificate usage keys .
    private var storedCertificateUsages: [String] {
        return certificatesMap?.keys.sorted() ?? [String]()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.updateCertificateMap()
    }

    /// retrieves all the stored certificates.
    private func updateCertificateMap() {
        AWSDKHelper.shared.retrieveStoredPublicCertificates { [weak self] (certificateMap, error) in
            guard let map = certificateMap,
                  let weakSelf = self else {
                return
            }
            weakSelf.certificatesMap = map
            DispatchQueue.main.async { [weak self] in
                guard let weakMainSelf = self else {
                    return
                }
                weakMainSelf.tableView.reloadData()
            }
        }
    }
}

extension StoredCertificatesListController {

    // MARK: - Table view data source
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.storedCertificateUsages.count
    }


    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "basicTextCell", for: indexPath)

        let usageKey = self.storedCertificateUsages[indexPath.row]
        let usageLabel = self.labelMap[usageKey] ?? "Unknown for app"
        cell.textLabel?.text = usageLabel
        cell.accessoryType = .disclosureIndicator
        return cell
    }

    // MARK: - Table view delegate
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)

        if let detailsViewController = storyboard.instantiateViewController(withIdentifier: "certificateDetailsView") as? CertificateInfoTableViewController {

            let usageKey = self.storedCertificateUsages[indexPath.row]
            if let certificates = self.certificatesMap?[usageKey] {
                detailsViewController.setup(with: certificates)

                let usageLabel = self.labelMap[usageKey] ?? "Unknown for app"
                detailsViewController.title = usageLabel
                self.navigationController?.pushViewController(detailsViewController, animated: true)
            }

        }
    }
}
