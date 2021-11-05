//
//  DeviceCompromisedStatusViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//
import UIKit


class DeviceCompromisedStatusViewController: UIViewController {

    @IBOutlet weak var deviceCompromisedStatusLabel: UILabel!
    @IBOutlet weak var beaconStatusLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    @IBAction func checkDeviceCompromisedStatus(_ sender: Any) {
        // checks whether device is jail broken or not and updates UI accordingly.
        AWSDKHelper.shared.checkCompromisedStatus { isJailbroken, evaluationToken, identifier in
            DispatchQueue.main.async {
                if isJailbroken {
                    self.deviceCompromisedStatusLabel.text = "Device is compromised"
                }
                else{
                    self.deviceCompromisedStatusLabel.text = "Device is not compromised"
                }

            }
        }
    }
    
    @IBAction func sendDeviceStatusBeacon(_ sender: Any) {
        // send beacon data to console and updates UI accordingly.
        AWSDKHelper.shared.sendBeaconData { success, error in
            DispatchQueue.main.async {
                self.beaconStatusLabel.text = "\(success ? "Sent" : "Error, \(String(describing: error))")"
            }
        }
    }
}
