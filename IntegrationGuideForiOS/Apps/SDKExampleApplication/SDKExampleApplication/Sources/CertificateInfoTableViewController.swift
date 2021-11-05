//
//  CertificateInfoTableViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import AWSDK


/// Class shows all information about certificate by querying to supported certificate attributes.
/// to know more about supported certificate attributes check below code.
class CertificateInfoTableViewController: UITableViewController {

    private enum Labels: String {
        case emptyData = "** Empty **"
    }

    private struct CellInfo {
        let title: String
        let subtitle: String
    }

    private var cellInfo = [[CellInfo]]()

    override func viewDidLoad() {
        super.viewDidLoad()
    }

    ///  setting up the data to be loaded in tableview about certificates
    /// - Parameter certificates: array of objects that holds information about certificates.
    internal func setup(with certificates: [PublicCertificate]) {
        var cellInfos = [[CellInfo]]()
        for certificate in certificates {
            var cells = [CellInfo]()

            // CertificateInfoKey are the strings that allows you to retrieve attributes and raw data of certificate.
            let dataKey = CertificateInfoKey.rawCertificate
            if let data = certificate.value(forCertificateAttribute: dataKey) as? Data  {
                cells.append(CellInfo(title: dataKey, subtitle: String(describing: data)))
            } else {
                cells.append(CellInfo(title: dataKey, subtitle: Labels.emptyData.rawValue))
            }

            let stringValueKeys = [
                CertificateInfoKey.subjectName,
                CertificateInfoKey.subjectUserID,
                CertificateInfoKey.subjectIdentifier,
                CertificateInfoKey.emailAddress ,
                CertificateInfoKey.commonName ,
                CertificateInfoKey.issuer ,
                CertificateInfoKey.algorithm ,
                CertificateInfoKey.subjectAlternativeName ,
                CertificateInfoKey.keyUsage ,
                CertificateInfoKey.extendedKeyUsage ,
                CertificateInfoKey.universalPrincipalName
            ]

            for key in stringValueKeys {
                if let value = certificate.value(forCertificateAttribute: key) as? String  {
                    cells.append(CellInfo(title: key, subtitle: value))
                } else {
                    cells.append(CellInfo(title: key, subtitle: Labels.emptyData.rawValue))
                }
            }

            let serialNumberKey = CertificateInfoKey.serialNumber
            if let data = certificate.value(forCertificateAttribute: serialNumberKey) as? Data  {
                cells.append(CellInfo(title: serialNumberKey, subtitle: String(describing: data)))
            } else {
                cells.append(CellInfo(title: serialNumberKey, subtitle: Labels.emptyData.rawValue))
            }

            let dateValueKeys = [
                CertificateInfoKey.startDate,
                CertificateInfoKey.endDate
            ]

            for key in dateValueKeys {
                if let value = certificate.value(forCertificateAttribute: key) as? Date  {
                    cells.append(CellInfo(title: key, subtitle: String(describing: value)))
                } else {
                    cells.append(CellInfo(title: key, subtitle: Labels.emptyData.rawValue))
                }
            }
            cellInfos.append(cells)
        }
        self.cellInfo = cellInfos
        self.tableView.reloadData()
    }

}

// MARK: - Table view data source
extension CertificateInfoTableViewController  {

    override func numberOfSections(in tableView: UITableView) -> Int {
        return self.cellInfo.count
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.cellInfo[section].count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "subtitleCell", for: indexPath)
        let currentCellInfo = self.cellInfo[indexPath.section][indexPath.row]
        cell.textLabel?.text = currentCellInfo.title
        cell.detailTextLabel?.text = currentCellInfo.subtitle

        return cell
    }
}
