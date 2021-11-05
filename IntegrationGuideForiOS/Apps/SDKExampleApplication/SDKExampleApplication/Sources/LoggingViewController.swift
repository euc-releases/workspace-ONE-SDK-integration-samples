//
//  LoggingViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import UIKit
import AWSDK

class LoggingViewController: UIViewController {

    @IBOutlet weak var logLevelPicker: UIPickerView!
    @IBOutlet weak var logInputField: UITextField!

    let pickerData = ["Verbose", "Info", "Warning", "Error"]
    var logLevelChoice = 0

    override func viewDidLoad() {
        super.viewDidLoad()

        logInputField.delegate = self
        logLevelPicker.delegate = self
        logLevelPicker.dataSource = self

        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tap)
        self.setupNavigationButton()
    }

    private func setupNavigationButton() {
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Logs", style: .plain, target: self, action: #selector(logsButtonClicked))
    }

    @objc func logsButtonClicked() {
        let logsScreen = WorkspaceOneLogsViewController()
        self.navigationController?.pushViewController(logsScreen, animated: true)
    }

   /// Append user input to current application log.
    @IBAction func didTapAppendToLog(_ sender: AnyObject) {
        guard let text = logInputField.text else {
            return
        }

        // The picker is being used to highlight the different log levels based on user interaction.
        // In a general implementation, these statements would be added to the codebase where appropriate.
        switch logLevelChoice {
            case 0:
                //records all type of data
                AWLogVerbose(text)
                print("capturing verbose logs")

            case 1:
                //records data for information purpose
                AWLogInfo(text)
                print("capturing information logs")

            case 2:
                //records errors and warning.
                AWLogWarning(text)
                print("capturing warning logs")

            case 3:
                //records only error.
                AWLogError(text)
                print("capturing error logs")

            default:
                break
        }
    }

    /// Send application log up till this point to Workspace ONE console
    @IBAction func didTapSendLog(_ sender: AnyObject) {
        AWSDKHelper.shared.sendLogs { success, error in
            DispatchQueue.main.async {
                if success, error == nil {
                    self.showAlert(with: "Logs sent successfully.")
                } else {
                    self.showAlert(with: "Error sending logs: \(String(describing: error))")
                }
            }
        }
    }

    private func showAlert(with message: String) {
        let action = (UIAlertAction(title: "Dismiss", style: .default, handler: nil))
        AlertHelper.showAlert(title: "AWSDK Log Reporter", message: message, actions: [action], presenter: self)
    }
}

// MARK:- UITextFieldDelegate
extension LoggingViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: UITextField) {
        logInputField.becomeFirstResponder()
        logInputField.selectAll(nil)
    }
}

// MARK:- UIPicker Delegate and Datasource
extension LoggingViewController: UIPickerViewDelegate {
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        logLevelChoice = row
    }
}

extension LoggingViewController: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }

    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
    }

    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }
}
