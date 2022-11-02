//
//  TunnelingViewModel.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import WebKit
import AWSDK
import Combine

class SessionDelegate: NSObject, URLSessionDelegate {
    func urlSession(_ session: URLSession, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        if challenge.protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust {
            let isTrusted = AWController.clientInstance().validate(serverTrust: challenge.protectionSpace.serverTrust!,
                                                                                       trustStore: .deviceAndCustom, strictness: .ignore)
            if isTrusted {
                let creds = URLCredential(trust: challenge.protectionSpace.serverTrust!)
                completionHandler(.useCredential, creds);
            } else {
                completionHandler(.performDefaultHandling, nil);
            }
        }

    }
}

class TunnelingViewModel: ObservableObject {
    @Published var statusText: String = ""
    @Published var shouldHideUIWebView: Bool = false
    @Published var shouldHideWKWebView: Bool = true
    var loadRequestUIWebView = PassthroughSubject<URLRequest, Never>()
    var loadRequestWkWebView = PassthroughSubject<URLRequest, Never>()
    var loadHtmlString = PassthroughSubject<(dataString: String, baseURL: URL), Never>()
    var loadData = PassthroughSubject<(data: Data, mimeType: String, encodingName: String, baseURL: URL), Never>()

    private var selectedNetworkType: NetworkType = .webview
    let sessionDelegate = SessionDelegate()

    func getWkWebViewConfig() -> WKWebViewConfiguration {
        let config = WKWebViewConfiguration()
        let customHandler = CustomURLSchemeHandler()
        config.setURLSchemeHandler(customHandler, forURLScheme: "ptth")
        config.setURLSchemeHandler(customHandler, forURLScheme: "ptths")
        return config
    }
}

extension TunnelingViewModel {
    func networkTypeSelectionChanged(networkType: NetworkType) {
        selectedNetworkType = networkType

        switch networkType {
        case .webview:
            self.shouldHideUIWebView = false
            self.shouldHideWKWebView = true
            AWLogInfo("Loading Tunneled traffic into WebView")
            self.loadRequestUIWebView.send(URLRequest.init(url:URL.init(string: "about:blank")!))
            statusText = ""

        case .urlSession:
            self.shouldHideUIWebView = false
            self.shouldHideWKWebView = true
            AWLogInfo("Fetching Tunneled traffic using URL Session")
            self.loadRequestUIWebView.send(URLRequest.init(url:URL.init(string: "about:blank")!))
            statusText = "HTTP Status"

        case .wkwebview:
            let websiteDataTypes = NSSet(array: [WKWebsiteDataTypeFetchCache,
                                                 WKWebsiteDataTypeDiskCache,
                                                 WKWebsiteDataTypeMemoryCache,
                                                 WKWebsiteDataTypeOfflineWebApplicationCache,
                                                 WKWebsiteDataTypeCookies,
                                                 WKWebsiteDataTypeSessionStorage,
                                                 WKWebsiteDataTypeLocalStorage,
                                                 WKWebsiteDataTypeWebSQLDatabases,
                                                 WKWebsiteDataTypeIndexedDBDatabases,
                                                 WKWebsiteDataTypeServiceWorkerRegistrations])
            WKWebsiteDataStore.default().removeData(ofTypes: websiteDataTypes as! Set<String>, modifiedSince: Date.distantPast, completionHandler:{ } )
            self.shouldHideUIWebView = true
            self.shouldHideWKWebView = false
            AWLogInfo("WKWebView")
            self.loadRequestWkWebView.send(URLRequest.init(url:URL.init(string: "about:blank")!))
            statusText = "HTTP Status"
        }
    }

    func goButtonTapped(urlString: String) {
        statusText = ""
        guard let url = urlString.url else {
            statusText = Alerts.invalidURL.title
            return
        }

        let request = URLRequest(url: url)
        switch selectedNetworkType {
        case .webview:
            self.shouldHideUIWebView = false
            self.shouldHideWKWebView = true
            self.loadRequestUIWebView.send(request)

        case .urlSession:
            self.shouldHideUIWebView = false
            self.shouldHideWKWebView = true
            self.makeSessionRequest(request, networkType: 1)

        case .wkwebview:
            let websiteDataTypes = NSSet(array: [WKWebsiteDataTypeFetchCache,
                                                 WKWebsiteDataTypeDiskCache,
                                                 WKWebsiteDataTypeMemoryCache,
                                                 WKWebsiteDataTypeOfflineWebApplicationCache,
                                                 WKWebsiteDataTypeCookies,
                                                 WKWebsiteDataTypeSessionStorage,
                                                 WKWebsiteDataTypeLocalStorage,
                                                 WKWebsiteDataTypeWebSQLDatabases,
                                                 WKWebsiteDataTypeIndexedDBDatabases,
                                                 WKWebsiteDataTypeServiceWorkerRegistrations])
            WKWebsiteDataStore.default().removeData(ofTypes: websiteDataTypes as! Set<String>, modifiedSince: Date.distantPast, completionHandler:{ } )
            self.shouldHideUIWebView = true
            self.shouldHideWKWebView = false
            self.loadRequestWkWebView.send(request)
        }
    }

    // Making the session request and using session task to do the networking.
    func makeSessionRequest(_ request: URLRequest, networkType: Int) {
        let session = URLSession(configuration: URLSession.shared.configuration, delegate: sessionDelegate, delegateQueue: nil)

        let task = session.dataTask(with: request) { taskData, taskResponse, error in
            guard
                let data = taskData,
                let response = taskResponse as? HTTPURLResponse,
                let dataString = String(data: data, encoding: response.encoding),
                let url = response.url
            else {
                session.invalidateAndCancel()
                session.finishTasksAndInvalidate()
                return
            }

            // Loading the task data into the webview if webview (0) is selected
            guard networkType == 0 else {
                DispatchQueue.main.async {
                    // Set the labels based on the data/response values
                    self.statusText = "HTTP: \(response.statusCode)"
                }
                // Invalidating the session once UI has been updated with the task data and response
                session.invalidateAndCancel()
                session.finishTasksAndInvalidate()
                return
            }

            guard
                let mimeType = response.mimeType,
                let textEncodingName = response.textEncodingName
            else {
                DispatchQueue.main.async {
                    self.loadHtmlString.send((dataString: dataString, baseURL: url))
                }
                session.invalidateAndCancel()
                session.finishTasksAndInvalidate()
                return
            }

            // Updating the UI on the Main thread.
            DispatchQueue.main.async {
                self.loadData.send((data: data, mimeType: mimeType, encodingName: textEncodingName, baseURL: url))
            }
            session.invalidateAndCancel()
            session.finishTasksAndInvalidate()
            return
        }
        task.resume()
    }
    
}

class CustomURLSchemeHandler: NSObject, WKURLSchemeHandler {
    var taskMap: [URL: URLSessionDataTask] = [:]
    func webView(_ webView: WKWebView, start urlSchemeTask: WKURLSchemeTask) {
        guard let url = urlSchemeTask.request.url,
              let properURL = URL(string: url.absoluteString.replacingOccurrences(of: "ptth", with: "http")) else {
            urlSchemeTask.didFailWithError(URLError(URLError.Code.badURL))
            return
        }

        let request = URLRequest(url: properURL)
        let task = URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            defer {
                self?.webView(webView, stop: urlSchemeTask)
            }
            if let error = error {
                urlSchemeTask.didFailWithError(error)
                return
            }
            if let response = response {
                urlSchemeTask.didReceive(response)
            }
            if let data = data {
                urlSchemeTask.didReceive(data)
            }
            urlSchemeTask.didFinish()
        }
        self.taskMap[url] = task
        task.resume()
    }

    func webView(_ webView: WKWebView, stop urlSchemeTask: WKURLSchemeTask) {
        guard let url = urlSchemeTask.request.url else {
            urlSchemeTask.didFailWithError(URLError(URLError.Code.badURL))
            return
        }
        if let task = self.taskMap[url] {
            self.taskMap[url] = nil
            task.cancel()
        }
    }
}

extension String {
    var url: URL? {
        guard !self.isEmpty else {
            return nil
        }
        let text = self.trimmingCharacters(in: .whitespacesAndNewlines)
        if let url = URL(string: text), let scheme = url.scheme, scheme.isEmpty == false {
            return url
        }
        return URL(string: "https://" + text)
    }
}


