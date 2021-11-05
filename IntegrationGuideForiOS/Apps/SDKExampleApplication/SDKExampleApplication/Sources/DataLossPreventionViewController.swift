//
//  DataLossPreventionViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

class DataLossPreventionViewController: UIViewController {

    @IBOutlet weak var tabBar: UITabBar!
    @IBOutlet weak var headingTextView: UITextView!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var learnMoreButton: UIButton!

    @IBOutlet weak var firstButton: UIButton!
    @IBOutlet weak var secondButton: UIButton!

    private let alertMessageDLP: String = "Please make sure to define correct bundle settings in your project and enable DLP in the SDK profile on the Workspace ONE console"
    private var currentSelection: Int = 0
    private var dlpEnabled = false

    private let pageAppDLPTitles = [
        "Setup bundle in project",
        "Install Workspace ONE apps","Edit SDK Profile",
        "Enable settings in SDK Profile",
        "Set the DLP",
        "Assign the profile"
    ]
    private let pageAppDLPImages = [
        "EditDLPBundle",
        "InstallWorkspaceONEApps",
        "EditSDKProfile",
        "EditDLPInProfile",
        "EnableDLPOptions",
        "AssignSDKProfile"
    ]
    private  let pageOverlayDLPTitles = [
        "Setup bundle in project",
        "Blocker View Enabled",
        "Blocker View Disabled"
    ]
    private let pageOverlayDLPImages = [
        "EditBlockerBundle",
        "WithBlockerScreen",
        "WithoutBlockerScreen"
    ]
    let pageEditingDLPTitles = [
        "Setup bundle in project",
        "Edit SDK Profile",
        "Edit Settings in SDK Profile",
        "Assign the profile"
    ]
    private let pageEditingDLPImages = [
        "EditCopyPasteInBundle",
        "EditSDKProfile",
        "EditCopyPasteInProfile",
        "AssignSDKProfile"
    ]
    private  let pageMoreDLPTitles = ["Additional DLP flags in SDK Profile"]
    private  let pageMoreDLPImages = ["MoreDLPSettings"]

    override func viewDidLoad() {
        super.viewDidLoad()
        self.addSwipeRecognizer()
        self.tabBar.delegate = self
        self.tabBar.selectedItem = tabBar.items?.first
        self.setAppDLPViews()
        self.dlpEnabled = checkDLPStatus()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if self.currentSelection == 3 {
            self.setAppDLPViews()
        }
    }

    /// 
    /// - Returns: returns boolean value indicating whether data loss prevention is enabled or disbaled.
    private func checkDLPStatus() -> Bool {
        return  AWSDKHelper.shared.dlpEnabled ?? false
    }

    @IBAction func didTapLearnMore(_ sender: UIButton) {
        guard let destinationVC = UIStoryboard(name: "Main", bundle: Bundle(for: type(of: self))).instantiateViewController(withIdentifier: "customSWT") as? WalkThroughViewController else {
            return
        }

        switch self.currentSelection {
            case 0:
                destinationVC.pageDescription = self.pageAppDLPTitles
                destinationVC.pageMedia       = self.pageAppDLPImages

            case 1:
                destinationVC.pageDescription = self.pageOverlayDLPTitles
                destinationVC.pageMedia       = self.pageOverlayDLPImages

            case 2:
                destinationVC.pageDescription = self.pageEditingDLPTitles
                destinationVC.pageMedia       = self.pageEditingDLPImages

            case 3:
                destinationVC.pageDescription = self.pageMoreDLPTitles
                destinationVC.pageMedia       = self.pageMoreDLPImages

            default:
                break
        }
        self.navigationController?.pushViewController(destinationVC, animated: true)
    }

    // MARK: Methods to control the Application DLP Settings
    @IBAction func didTapRedirectMailto(_ sender: AnyObject) {

        guard self.dlpEnabled, let url = URL(string: "mailto:exampleuser@example.com?cc=exampleuser2@example.com,exampleuser3@example.com&bcc=exampleuser4@example.com&subject=Some%20Important%20Mail&body=Hi%2C%0A%0A%0A") else {
            self.alertUser(withMessage: self.alertMessageDLP)
            return
        }
        ensureOnMainThread {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }

    @IBAction func didTapRedirectHttpLink(_ sender: AnyObject) {
        guard let url = URL(string: "https://www.vmware.com") else {
            self.alertUser(withMessage: self.alertMessageDLP)
            return
        }
        ensureOnMainThread {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }

    private func addSwipeRecognizer() {
        [UISwipeGestureRecognizer.Direction.right, UISwipeGestureRecognizer.Direction.left].forEach { direction in
            let gesture = UISwipeGestureRecognizer(target:self,action: #selector(self.handleSwipe(sender:)))
            gesture.direction = direction
            self.view.addGestureRecognizer(gesture)
        }
    }

    @objc func handleSwipe(sender: UISwipeGestureRecognizer) {
        switch (self.currentSelection, sender.direction.rawValue) {
            case (0, 1), (1, 2):
                self.setEditingDLPViews()

            case (0, 2), (2, 1):
                self.setBlockerDLPViews()

            case (1, 1), (2, 2):
                self.setAppDLPViews()

            default:
                break
        }
    }

    // MARK: NON Workspace ONE SDK Util Methods
    private  func setAppDLPViews() {
        self.currentSelection = 0
        self.tabBar.selectedItem = tabBar.items?.first
        self.firstButton.isHidden = false
        self.secondButton.isHidden = false
        self.headingTextView.text = "Restrict data to be opened in Workspace ONE applications"
        self.descriptionTextView.text = "Workspace ONE SDK Applications can be configured so that HTTP/HTTPS and MAILTO links can be automatically sent to Workspace ONE apps. Click Learn more to explore!"
    }

    private func setBlockerDLPViews() {
        self.currentSelection = 1
        self.tabBar.selectedItem = tabBar.items?[1]
        self.firstButton.isHidden = true
        self.secondButton.isHidden = true
        self.headingTextView.text = "Configuring Workspace ONE Blocker Screen"
        self.descriptionTextView.text = "Workspace ONE SDK provides the flexibility to configure the Workspace ONE blocker overlay screen which is presented on the double tap of home screen or while the app is started and is stopped. Click Learn more to explore!"
    }

    private  func setEditingDLPViews() {
        self.currentSelection = 2
        self.tabBar.selectedItem = tabBar.items?[2]
        self.firstButton.isHidden = true
        self.secondButton.isHidden = true
        self.headingTextView.text = "Restrict Cut/Copy/Paste operation"
        self.descriptionTextView.text = "User should't be able to copy or edit this text if this feature is enabled. Workspace ONE SDK can prevent the cut/copy/paste capabilities automatically inside your app. Click Learn more to Explore!"
    }

    private func setMoreDLPViews()  {
        self.currentSelection = 3
        self.didTapLearnMore(self.learnMoreButton)
    }

    private func alertUser(withMessage customMessage: String) {
        let action = UIAlertAction(title: "Dismiss", style: UIAlertAction.Style.default, handler: nil)
        AlertHelper.showAlert(title: "Data Loss Prevention", message: customMessage, actions: [action], presenter: self)
    }

}

// MARK: Tab Bar Switching Controller
extension DataLossPreventionViewController : UITabBarDelegate {

    func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {

        switch item.tag {
            case 0:
                self.setAppDLPViews()

            case 1:
                self.setBlockerDLPViews()

            case 2:
                self.setEditingDLPViews()

            case 3:
                self.setMoreDLPViews()

            default:
                break
        }
    }

}
