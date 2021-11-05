//
//  WorkspaceOneLogsViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit

class WorkspaceOneLogsViewController: UIViewController, UIDocumentInteractionControllerDelegate {

    private let textView: UITextView = UITextView(frame: .zero)
    private var logString = ""

    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.title = "SDK Logs"
        self.setupTextView()
        self.fetchLogData()
        self.setupNavigationButton()
    }

    /// fetch the logs and display in UITextView
    private func fetchLogData() {
        do {
          let content = try AWSDKHelper.shared.fetchSDKLogData()
          let logs = String(decoding: content, as: UTF8.self)
            logString = logs
            self.textView.text = logs
        }
        catch {
            self.textView.text = "Failed to get the logs"
        }
    }

    private func setupNavigationButton() {
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Copy Logs", style: .plain, target: self, action: #selector(logsButtonClicked))
    }

    @objc func logsButtonClicked() {
        //copying the logs to iOS clipboards.
        UIPasteboard.general.string = logString
    }

    private func setupTextView() {
        self.view.addSubview(self.textView)
        self.textView.frame = self.view.frame
        self.textView.translatesAutoresizingMaskIntoConstraints = false
        self.textView.topAnchor.constraint(equalTo: self.view.topAnchor).isActive = true
        self.textView.heightAnchor.constraint(equalTo: self.view.heightAnchor).isActive = true
        self.textView.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        self.textView.widthAnchor.constraint(equalTo: self.view.widthAnchor).isActive = true

    }
}

