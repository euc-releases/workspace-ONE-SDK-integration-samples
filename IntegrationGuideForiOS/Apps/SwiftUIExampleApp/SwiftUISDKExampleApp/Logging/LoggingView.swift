//
//  LoggingView.swift
//  SwiftUISDKExampleApp
//
//
//  Copyright 2023 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AWSDK

enum LogLevel: String, CaseIterable, Identifiable {
    case verbose, info, warning, error
    var id: Self { self }
}

enum UserPreferedLogLevel: String, CaseIterable, Identifiable {
    case verbose, info, warning, error, off
    var id: Self { self }
}

struct LoggingView: View {

    @ObservedObject private var viewModel = LoggingViewModel()
    @State private var selectedLogLevel: LogLevel = .info
    @State private var enteredLogMessage: String = ""
    @State private var selectedUserPreferedLogLevelForSDKLogs: UserPreferedLogLevel = .off

    var body: some View {
        VStack{
            form
            btnCollectLog
            btnSendLogs
            Spacer()
            formSDKLogsLevel
            btnUserPreferedLogLevel
            Spacer()
        }
        .onAppear{
            selectedUserPreferedLogLevelForSDKLogs = viewModel.getUserPreferedLogLevelForSDK()
            selectedLogLevel = .info
            enteredLogMessage = "Test Message"
        }
        .padding()
        .navigationBarTitle(String(localized: "LoggingNavigationTitle"))
        .alert(item: $viewModel.alert) { alert in
            Alert(title: Text(alert.title), message: Text(alert.message), dismissButton: .default(Text(String(localized: "Ok"))))
        }
        .navigationBarItems(trailing:
            NavigationLink(destination: LogView()) {
                Text((String(localized: "FetchLogsButtonTitle")))
            }
        )
    }
}

struct LoggingView_Previews: PreviewProvider {
    static var previews: some View {
        LoggingView()
    }
}

private extension LoggingView {

    var form: some View {
        Form {
            Section(String(localized: "LoggingSectionTitle")) {
                TextField(
                    String(localized: "LogTextFieldPlaceholder"),
                    text: $enteredLogMessage
                )
                .disableAutocorrection(true)
                Picker(String(localized: "PickerViewFieldTitle"), selection: $selectedLogLevel) {
                    ForEach(LogLevel.allCases) { loglevel in
                        Text(loglevel.rawValue.capitalized)
                            .tag(loglevel)
                    }
                }
            }
        }
        .frame( height: 180)
    }

    var btnSendLogs: some View {
        Button{
            viewModel.btnUploadLogsDidSelect(enteredLogMessage: enteredLogMessage)
        } label: {
            Text(String(localized: "UploadLogBtnTitle"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
    }

    var btnCollectLog: some View {
        Button{
            viewModel.btnCollectLogsDidSelect(enteredLogMessage: enteredLogMessage, selectedLogLevel: selectedLogLevel)
            enteredLogMessage = ""
        } label: {
            Text(String(localized: "AppendLogBtnTitle"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
    }
    
    var formSDKLogsLevel: some View {
        Form {
            Section(String(localized: "SDKLogLevelSectionTitle")) {
                Picker(String(localized: "UserPreferredLoglevelPickerViewFieldTitle"), selection: $selectedUserPreferedLogLevelForSDKLogs) {
                    ForEach(UserPreferedLogLevel.allCases) { loglevel in
                        Text(loglevel.rawValue.capitalized)
                            .tag(loglevel)
                    }
                }
            }
        }
        .frame( height: 120)
    }
    var btnUserPreferedLogLevel: some View {
        Button{
            viewModel.setUserPreferedLogLevelForSDK(logLevel: selectedUserPreferedLogLevelForSDKLogs)
            viewModel.btnCollectAllLogLevelToTest()
        } label: {
            Text(String(localized: "SetUserPreferedLogLevelBtnTitle"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
    }
}
