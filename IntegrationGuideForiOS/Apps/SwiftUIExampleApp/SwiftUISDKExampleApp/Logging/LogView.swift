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
    @State private var isSharePresented: Bool = false
    @StateObject private var viewModel = LoggingViewModel()

    var body: some View {
        VStack{
        }
        .padding()
        .onAppear(perform: viewModel.fetchSDKLogData)
        .navigationBarTitle(String(localized: "FetchLogsNavigationTitle"))
        .navigationBarItems(trailing:
                                Button(action: {
            isSharePresented = viewModel.logInfo.count > 0 ? true : false
        }){
            Text(String(localized: "CopyLogsBtnTitle"))
        })
        .sheet(isPresented: $isSharePresented) {
            shareFiles()
        }
    }
}

extension LogView {

    private func shareFiles() -> ShareLogsActivityController {
        let logFileURLs = self.storeLogsToTemporaryFile(logInfos: viewModel.logInfo)
        let sheetView = ShareLogsActivityController(activityItems: logFileURLs, callback: { _,_,_,_ in
            self.deleteLogFiles(fileURLs: logFileURLs)
        })

        return sheetView
    }

    /// Create log files in temp directory from the log data available
    private func storeLogsToTemporaryFile(logInfos: [AWSDKHelper.logInfo]) -> [URL] {
        var fileURLs = [URL]()
        logInfos.forEach {
            do {
                let fileURL = tempFileURL(with: $0.suggestedFileName)
                fileURLs.append(fileURL)
                try $0.data.write(to: fileURL)
            } catch {

            }
        }
        return fileURLs
    }

    /// Delete the log files
    /// - Parameter fileURLs: fileurl
    internal func deleteLogFiles(fileURLs : [URL]) {
        fileURLs.forEach {
            if FileManager.default.fileExists(atPath: $0.path) {
                do {
                    try FileManager.default.removeItem(at: $0)
                } catch {
                }
            }
        }
    }

    /// API to construct the temp file url path with the suggested file name
    private func tempFileURL(with fileName: String) -> URL {
        return FileManager.default.temporaryDirectory.appendingPathComponent(fileName)
    }
}

struct LogView_Previews: PreviewProvider {
    static var previews: some View {
        LogView()
    }
}

struct ShareLogsActivityController: UIViewControllerRepresentable {
    typealias Callback = (_ activityType: UIActivity.ActivityType?, _ completed: Bool, _ returnedItems: [Any]?, _ error: Error?) -> Void
    let activityItems: [Any]
    let applicationActivities: [UIActivity]? = nil
    let excludedActivityTypes: [UIActivity.ActivityType]? = nil
    var callback: Callback
    func makeUIViewController(context: Context) -> UIActivityViewController {
        let controller = UIActivityViewController(
            activityItems: activityItems,
            applicationActivities: applicationActivities)
        controller.excludedActivityTypes = excludedActivityTypes
        controller.completionWithItemsHandler = callback
        return controller
    }
    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {
        // nothing to do here
    }
}
