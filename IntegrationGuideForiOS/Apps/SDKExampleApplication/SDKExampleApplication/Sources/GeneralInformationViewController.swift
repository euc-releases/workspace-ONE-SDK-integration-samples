//
//  GeneralInformationViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import AWSDK

class GeneralInformationViewController: UITableViewController {

    private var userInformation = [
        (key: InfoEntry.username,         value: InfoEntry.loading.string),
        (key: InfoEntry.userOrganization, value: InfoEntry.loading.string),
        (key: InfoEntry.username,         value: InfoEntry.loading.string),
        (key: InfoEntry.fullName,         value: InfoEntry.loading.string),
        (key: InfoEntry.domain,           value: InfoEntry.loading.string),
        (key: InfoEntry.userID,           value: InfoEntry.loading.string)
    ]

    private var applicationInformation = [
        (key: InfoEntry.enrolled,       value: InfoEntry.loading.string),
        (key: InfoEntry.isAppSupported,  value: InfoEntry.loading.string),
        (key: InfoEntry.isManaged,       value: InfoEntry.loading.string),
        (key: InfoEntry.consoleVersion,  value: InfoEntry.loading.string)
    ]
    private var sharedDeviceInformation = [
        (key: InfoEntry.stagingEnabled,       value: InfoEntry.loading.string),
        (key: InfoEntry.sharedDeviceStatus,  value: InfoEntry.loading.string),
        (key: InfoEntry.userContextID,       value: InfoEntry.loading.string),
    ]

    override func viewDidLoad() {
        super.viewDidLoad()
        // Workspace ONE SDK API calls separated for readability
        self.fetchUserInfo()
        self.fetchDeviceInfo()
    }


    /// Fetch Device Information and show in UI
    private func fetchDeviceInfo() {

        AWSDKHelper.shared.fetchApplicationAssignmentStatus { [weak self]
            (applicationStatusInformation: ApplicationStatusInformation?, sharedDeviceInformation: SharedDeviceInformation?, error: Error?) in


            print("Error fetching information: \(error.debugDescription)")
            guard let weakSelf = self else {
                AWLogError("Controller got deallocated to display result")
                return
            }

            guard let appStatusInfo = applicationStatusInformation, error == nil else {
                print("Error fetching information: \(error.debugDescription)")
                OperationQueue.main.addOperation {
                    weakSelf.displayFetchUserInfoError()
                }
                return
            }

            // Fetching properties from applicationInformation instance
            // Enrollment Status - See below extension for possible values / enums
            weakSelf.applicationInformation[0].value = String(describing: appStatusInfo.enrollmentStatus)

            // Is App Supported
            weakSelf.applicationInformation[1].value = String(describing: appStatusInfo.isAppSupported)

            // Bool check for App Management
            weakSelf.applicationInformation[2].value = appStatusInfo.isDeviceManaged.description

            weakSelf.applicationInformation[3].value = appStatusInfo.consoleVersion
            if let sharedInfo = sharedDeviceInformation {
                weakSelf.sharedDeviceInformation[0].value = String(describing: sharedInfo.isDeviceStagingEnabled)
                weakSelf.sharedDeviceInformation[1].value = String(describing: sharedInfo.sharedDeviceStatus.rawValue)
                weakSelf.sharedDeviceInformation[2].value = sharedInfo.userContextID ?? "N/A"
            } else {
                weakSelf.sharedDeviceInformation[0].value = "N/A"
                weakSelf.sharedDeviceInformation[1].value = "N/A"
                weakSelf.sharedDeviceInformation[2].value = "N/A"
            }
            DispatchQueue.main.async {
                weakSelf.tableView.reloadData()
            }
        }

    }

    /// Fetch UserInformation asynchronously and shows in UI
    private func fetchUserInfo() {
        AWSDKHelper.shared.retrieveUserInfo { [weak self] (userInformation, error) in
            guard let weakself = self else {
                AWLogError("Controller got deallocated to display result")
                return
            }

            guard let userInformation = userInformation, error == nil else {
                AWLogError("Error fetching information: \(error.debugDescription)")
                weakself.displayFetchUserInfoError()
                return
            }

            // Username
            weakself.userInformation[0].value = userInformation.userName

            // Group ID
            weakself.userInformation[1].value = userInformation.groupID

            // Email Address
            weakself.userInformation[2].value = userInformation.email

            // Full Name
            weakself.userInformation[3].value = userInformation.firstName + " " + userInformation.lastName

            // Domain
            weakself.userInformation[4].value = userInformation.domain

            // User ID
            weakself.userInformation[5].value = userInformation.userIdentifier
            DispatchQueue.main.async {
                weakself.tableView.reloadData()
            }
        }
    }

    private func displayFetchUserInfoError() {
        AWLogError("Log In error")
        let okAction = UIAlertAction(title: "Dismiss", style: .default) { _ in
            AWLogInfo("Dismiss")
        }
        AlertHelper.showAlert(title: "SDKError", message: "An Error Occured while Workspace ONE SDK was trying to fetch user info from Workspace ONE backend. Please make sure your device is enrolled", actions: [okAction], presenter: self)
    }

}

private enum InfoEntry: String {
    case loading          = "Loading..."
    case enrolled         = "Enrolled: "
    case isManaged        = "Is Managed: "
    case username         = "Username: "
    case userOrganization = "User Organization Group: "
    case email            = "Email Address: "
    case fullName         = "Full Name: "
    case domain           = "Domain: "
    case userID           = "User ID: "
    case isAppSupported   = "Is App Supported: "
    case consoleVersion   = "Console Version: "
    case stagingEnabled       = "Staging Enabled"
    case sharedDeviceStatus   = "CICO Status"
    case userContextID        = "Checked Out User"

    var string: String { return self.rawValue }
}


class SDKInfoTableViewCell: UITableViewCell {

    @IBOutlet weak var keyLabel: UILabel!
    @IBOutlet weak var valueLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

}

extension GeneralInformationViewController {

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
            case 0:
                return self.userInformation.count
            case 1:
                return self.applicationInformation.count
            case 2:
                return self.sharedDeviceInformation.count
            default:
                return 0
        }
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! SDKInfoTableViewCell
        cell.separatorInset = UIEdgeInsets.zero

        switch indexPath.section {
            case 0:
                cell.keyLabel.text = self.userInformation[indexPath.row].key.string
                cell.valueLabel.text = self.userInformation[indexPath.row].value

            case 1:
                cell.keyLabel.text = self.applicationInformation[indexPath.row].key.string
                cell.valueLabel.text = self.applicationInformation[indexPath.row].value

            case 2:
                cell.keyLabel.text = self.sharedDeviceInformation[indexPath.row].key.string
                cell.valueLabel.text = self.sharedDeviceInformation[indexPath.row].value

            default:
                break
        }
        return cell
    }

    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
            case 0:
                return "User Information"

            case 1:
                return "Application Status Information"

            case 2:
                return "Shared Device Information"

            default:
                return ""
        }
    }
}
