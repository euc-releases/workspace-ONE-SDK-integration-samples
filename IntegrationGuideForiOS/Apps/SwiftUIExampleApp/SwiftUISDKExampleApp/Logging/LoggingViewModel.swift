//
//  LoggingViewViewModel.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import AWSDK
import Combine


class LoggingViewModel: ObservableObject {
    private var cancellable = Set<AnyCancellable>()
    @Published var alert: CustomAlert?
    private var alertMessage: String? = nil {
        didSet {
            if let message = alertMessage {
                alert = CustomAlert(title: String(localized: "LoggingAlertTitle"), message: message)
            } else {
                alert = nil
            }
        }
    }

    let alertTitle = String(localized: "LoggingAlertTitle")

    /// Send application log up till this point to AW console
    func btnUploadLogsDidSelect(enteredLogMessage: String) {
        //Send/uploads collected logs to SDK server
        AWSDKHelper.shared.sendLogData()
            .receive(on: DispatchQueue.main)
            .sink { completion in
                switch completion {
                case .failure(let error) :
                    self.alertMessage = String(localized: "SendingLogErrorMessage") + ": [\(error.localizedDescription)]"
                case .finished: break
                }
            }
    receiveValue: { isSuccess in
        self.alertMessage = String(localized: "SendingLogSuccessMessage")
    }
    .store(in: &cancellable)
    }

    func btnCollectLogsDidSelect(enteredLogMessage: String, selectedLogLevel: LogLevel ) {
        //Collected logs
        guard !enteredLogMessage.isEmpty else {
            alertMessage = String(localized: "EmptyLogMessage")
            return
        }

        // The picker is being used to highlight the different log levels based on user interaction.
        // In a general implementation, these statements would be added to the codebase where appropriate.
        switch selectedLogLevel {
        case .verbose:
            AWLogVerbose(enteredLogMessage)
        case .info:
            AWLogInfo(enteredLogMessage)
        case .warning:
            AWLogWarning(enteredLogMessage)
        case .error:
            AWLogError(enteredLogMessage)
        }
        alertMessage = String(localized: "AppendLogSuccessMessage")
    }
}

