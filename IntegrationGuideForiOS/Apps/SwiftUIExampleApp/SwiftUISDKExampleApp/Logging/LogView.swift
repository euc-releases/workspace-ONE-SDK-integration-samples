//
//  LogView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct LogView: View {
    @State private var logString = ""
    var body: some View {
        VStack{
            txtEditorView
        }
        .padding()
        .onAppear(perform: fetchLogData)
        .navigationBarTitle(String(localized: "FetchLogsNavigationTitle"))
        .navigationBarItems(trailing:
                                Button(action: {
            copyLogsDidSelect()
        }){
            Text(String(localized: "CopyLogsBtnTitle"))
        })
    }
}
extension LogView {
    var txtEditorView: some View {
        TextEditor(text: $logString)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.2), lineWidth: 4)
            )
            .foregroundColor(.black)
    }
}
extension LogView {
    /// Fetch the logs and update state variable string
    private func fetchLogData() {
        do {
            let content = try AWSDKHelper.shared.fetchSDKLogData()
            logString = String(decoding: content, as: UTF8.self)
        }
        catch {
            logString = String(localized: "FetchLogsErrorMessage")
        }
    }
    
    private func copyLogsDidSelect() {
        /// Copying the logs to iOS clipboards.
        UIPasteboard.general.string = logString
    }
}

struct LogView_Previews: PreviewProvider {
    static var previews: some View {
        LogView()
    }
}
