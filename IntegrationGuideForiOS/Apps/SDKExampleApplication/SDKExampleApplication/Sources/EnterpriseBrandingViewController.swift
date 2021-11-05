//
//  BrandingTableViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit


/// Apply branding colours  and images to SDK UI.
/// Two Sources of Branding Information : Enterprise Branding and Static Branding.
/// Here Enterprise branding is integrated for Static branding  refer to a workspace one SDK document.
class EnterpriseBrandingViewController: UITableViewController {

    private var brandingImageInformation = [(String, Any)]()
    private var brandingColorInformation = [(String, Any)]()


    override func viewDidLoad() {
        super.viewDidLoad()

        self.retrieveBrandingInfo()
        self.tableView.tableFooterView = UIView()

    }

    /// retrieving branding info and storing it in key-value pair and loading in UITableView.
    private func retrieveBrandingInfo() {

        guard let brandingPayload = AWSDKHelper.shared.brandingPayload else {
            return
        }

        if let organizationName = brandingPayload.organizationName {
            if(!organizationName.isEmpty) {
                brandingColorInformation.append(("Organization Name",organizationName))
            }
        }
        if let primaryHighlightColor = brandingPayload.primaryHighlightColor {
            brandingColorInformation.append(("Primary highlight",primaryHighlightColor))
        }

        if let primaryColor = brandingPayload.primaryColor {
            brandingColorInformation.append(("Primary",primaryColor))
        }
        if let secondaryHighlightColor = brandingPayload.secondaryHighlightColor {
            brandingColorInformation.append(("Secondary highlight",secondaryHighlightColor))
        }

        if let secondaryColor = brandingPayload.secondaryColor {
            brandingColorInformation.append(("Secondary",secondaryColor))
        }

        if let toolbarColor = brandingPayload.toolbarColor {
            brandingColorInformation.append(("Toolbar",toolbarColor))
        }

        if let primaryTextColor = brandingPayload.primaryTextColor {
            brandingColorInformation.append(("Primary text",primaryTextColor))
        }

        if let secondaryTextColor = brandingPayload.secondaryTextColor {
            brandingColorInformation.append(("Secondary text",secondaryTextColor))
        }

        if let loginTitleTextColor = brandingPayload.loginTitleTextColor {
            brandingColorInformation.append(("Login title text",loginTitleTextColor))
        }

        if let tertiaryTextColor = brandingPayload.tertiaryTextColor {
            brandingColorInformation.append(("Tertiary text",tertiaryTextColor))
        }

        if let toolbarTextColor = brandingPayload.toolbarTextColor {
            brandingColorInformation.append(("Toolbar text",toolbarTextColor))
        }
        
        if let iPhoneBackgroundImageURL = brandingPayload.iPhoneBackgroundImageURL {
            brandingImageInformation.append(("iPhone background",iPhoneBackgroundImageURL))
        }
        if let iPhone2xBackgroundImageURL = brandingPayload.iPhone2xBackgroundImageURL {
            brandingImageInformation.append((key: "iPhone2x background", value: iPhone2xBackgroundImageURL))
        }
        if let iPhone52xBackgroundImageURL = brandingPayload.iPhone52xBackgroundImageURL {
            brandingImageInformation.append((key: "iPhone52x background", value: iPhone52xBackgroundImageURL))
        }
        if let iPadBackgroundImageURL = brandingPayload.iPadBackgroundImageURL {
            brandingImageInformation.append((key: "iPad background", value: iPadBackgroundImageURL))
        }
        if let iPad2xBackgroundImageURL = brandingPayload.iPad2xBackgroundImageURL {
            brandingImageInformation.append((key: "iPad2x background", value: iPad2xBackgroundImageURL))
        }
        if let iPhoneCompanyLogoURL = brandingPayload.iPhoneCompanyLogoURL {
            brandingImageInformation.append((key: "iPhone company logo", value: iPhoneCompanyLogoURL))
        }
        if let iPhone2xCompanyLogoURL = brandingPayload.iPhone2xCompanyLogoURL {
            brandingImageInformation.append((key: "iPhone2x company logo", value: iPhone2xCompanyLogoURL))
        }
        if let iPadCompanyLogoURL = brandingPayload.iPadCompanyLogoURL {
            brandingImageInformation.append((key: "iPad company logo", value: iPadCompanyLogoURL))
        }
        if let iPad2xCompanyLogoURL = brandingPayload.iPad2xCompanyLogoURL {
            brandingImageInformation.append((key: "iPad2x company logo", value: iPad2xCompanyLogoURL))
        }

        self.tableView.reloadData()
    }

}

// MARK: - Table view data source
extension EnterpriseBrandingViewController {

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
            case 0:
                return brandingColorInformation.count
            case 1:
                return brandingImageInformation.count
            default:
                return 0
        }
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "brandingCell", for: indexPath) as? BrandingCell else {
            return UITableViewCell()
        }
        // Configure the cell...
        switch indexPath.section {
            case 0:
                let labelKeyString = self.brandingColorInformation[indexPath.row].0
                cell.brandingLabel.text = labelKeyString
                cell.brandingColorImage.isHidden = true
                cell.brandingColorLabel.isHidden = false
                cell.brandingColorLabel.backgroundColor = brandingColorInformation[indexPath.row].1 as? UIColor
            case 1:
                let labelKeyString = self.brandingImageInformation[indexPath.row].0
                cell.brandingLabel.text = labelKeyString
                cell.brandingColorImage.isHidden = false
                cell.brandingColorLabel.isHidden = true
                // loading image from URL
                if let imgURL = brandingImageInformation[indexPath.row].1 as? URL {
                    cell.brandingColorImage.load(url: imgURL)
                }
            default:
                break
        }
        return cell
    }
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
            case 0:
                return "Colour Information"

            case 1:
                return "Image Information"

            default:
                return ""
        }
    }
}

class BrandingCell : UITableViewCell {

    @IBOutlet weak var brandingLabel: UILabel!
    @IBOutlet weak var brandingColorLabel: UILabel!
    @IBOutlet weak var brandingColorImage: UIImageView!

}
