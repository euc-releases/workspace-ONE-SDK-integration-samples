//
//  TunnelingViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import UIKit
import Foundation
import AWSDK
import WebKit

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


class TunnelingViewController: UIViewController, UIWebViewDelegate, URLSessionDelegate {

    @IBOutlet weak var urlTextField: UITextField!
    @IBOutlet weak var segmentedControl: UISegmentedControl!
    @IBOutlet weak var responseLabel: UILabel!
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet weak var containerView: UIView!
    var wkWebView: WKWebView? = nil
    let customHandler = CustomURLSchemeHandler()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.isToolbarHidden = true
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard)))
        self.responseLabel.text = ""
        self.loadingIndicator.hidesWhenStopped = true
        self.webView.layer.borderColor = UIColor.black.cgColor
        self.webView.layer.borderWidth = 2.0
        self.webView.delegate = self
        
        self.containerView.layer.borderColor = UIColor.black.cgColor
        self.containerView.layer.borderWidth = 2.0
        let config = WKWebViewConfiguration()
        config.setURLSchemeHandler(self.customHandler, forURLScheme: "ptth")
        config.setURLSchemeHandler(self.customHandler, forURLScheme: "ptths")
        let wv = WKWebView(frame: .zero, configuration: config)
        self.containerView.addSubview(wv)
        wv.translatesAutoresizingMaskIntoConstraints = false
        wv.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        wv.trailingAnchor.constraint(equalTo: self.containerView.trailingAnchor).isActive = true
        wv.topAnchor.constraint(equalTo: self.containerView.topAnchor).isActive = true
        wv.bottomAnchor.constraint(equalTo: self.containerView.bottomAnchor).isActive = true
        self.wkWebView = wv
        self.containerView.isHidden = true
        self.segmentedControl.selectedSegmentIndex = 0
    }

    // MARK: UISegmentedControl

    @IBAction func segmentedControlChanged(_ sender: AnyObject) {
        switch self.segmentedControl.selectedSegmentIndex {
        case 0:
            self.webView.isHidden = false
            self.view.bringSubviewToFront(self.webView)
            self.containerView.isHidden = true
            self.view.sendSubviewToBack(self.containerView)

            AWLogInfo("Loading Tunneled traffic into WebView")
            self.webView.loadRequest(URLRequest.init(url:URL.init(string: "about:blank")! ))
            self.responseLabel.text = ""

        case 1:
            self.webView.isHidden = false
            self.view.bringSubviewToFront(self.webView)
            self.containerView.isHidden = true
            self.view.sendSubviewToBack(self.containerView)
            AWLogInfo("Fetching Tunneled traffic using URL Session")
            self.webView.loadRequest(URLRequest.init(url:URL.init(string: "about:blank")! ))
            responseLabel.text = "HTTP Status"

        case 2:
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
            self.containerView.isHidden = false
            self.view.bringSubviewToFront(self.containerView)
            AWLogInfo("WKWebView")
            self.wkWebView?.load(URLRequest.init(url:URL.init(string: "about:blank")!))
            self.responseLabel.text = "HTTP Status"

        default:
            break
        }
    }

    // MARK: Networking

    // There is no additional Workspace ONE SDK calls required to proxy the traffic form iOS Networking classes via AirWatch Tunnel
    // However there are some limitations to this. Please refer Workspace ONE SDK guide to read more about these limitations
    @IBAction func handleRequest(_ sender: AnyObject) {
        guard let url = self.urlTextField.url else {
            Alert.invalidURL.alertController.default.present(onViewController: self) {
                self.responseLabel.text = Alert.invalidURL.title
            }
            return
        }

        let request = URLRequest(url: url)
        switch segmentedControl.selectedSegmentIndex {
            case 0:
                self.webView.isHidden = false
                self.view.bringSubviewToFront(self.webView)
                self.containerView.isHidden = true
                self.view.sendSubviewToBack(self.containerView)
                self.webView.loadRequest(request)
                
            case 1:
                self.webView.isHidden = false
                self.view.bringSubviewToFront(self.webView)
                self.containerView.isHidden = true
                self.view.sendSubviewToBack(self.containerView)
                self.makeSessionRequest(request, networkType: 1)
                
            case 2:
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
                self.containerView.isHidden = false
                self.view.bringSubviewToFront(self.containerView)
                self.wkWebView?.load(request)
            default: break
        }
    }

    // Making the session request and using session task to do the networking.
    func makeSessionRequest(_ request: URLRequest, networkType: Int) {
        let session = URLSession(configuration: URLSession.shared.configuration, delegate: self, delegateQueue: nil)

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
                    self.responseLabel.text = "HTTP: \(response.statusCode)"
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
                        self.webView.loadHTMLString(dataString, baseURL: url)
                    }
                    session.invalidateAndCancel()
                    session.finishTasksAndInvalidate()
                    return
            }

            // Updating the UI on the Main thread.
            DispatchQueue.main.async {
                self.webView.load(data, mimeType: mimeType, textEncodingName: textEncodingName, baseURL: url)
            }
            session.invalidateAndCancel()
            session.finishTasksAndInvalidate()
            return
        }

        task.resume()
    }

    // MARK: - WebView Delegates

    func webViewDidStartLoad(_ webView: UIWebView) {
        ensureOnMainThread {
            UIApplication.shared.isNetworkActivityIndicatorVisible = true
        }
        self.segmentedControl.isHidden = true
    }

    func webViewDidFinishLoad(_ webView: UIWebView) {
        if webView.isLoading {
            return
        }

        // Hide networking indicator
        ensureOnMainThread {
            UIApplication.shared.isNetworkActivityIndicatorVisible = false
        }
        self.segmentedControl.isHidden = false
    }

    func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        AWLogInfo("Webview Failed to load.")
    }
}

extension UITextField {
    var url: URL? {
        guard let text = self.text?.trimmingCharacters(in: .whitespacesAndNewlines) else { return nil }
        if let url = URL(string: text), let scheme = url.scheme, scheme.isEmpty == false {
            return url
        }
        
        return URL(string: "https://" + text)
    }
}

extension URLResponse {
    var encoding: String.Encoding {
        let usedEncoding = String.Encoding.utf8

        guard let encodingName = self.textEncodingName as CFString? else {
            return usedEncoding
        }

        let encoding = CFStringConvertEncodingToNSStringEncoding(CFStringConvertIANACharSetNameToEncoding(encodingName))

        guard encoding != UInt(kCFStringEncodingInvalidId) else {
            return usedEncoding
        }

        return String.Encoding(rawValue: encoding)
    }
}

extension UIViewController {
    @objc func dismissKeyboard() {
        ensureOnMainThread {
            UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to:nil, from:nil, for:nil)
        }
    }
}
extension UIView {

    /// Returns a collection of constraints to anchor the bounds of the current view to the given view.
    ///
    /// - Parameter view: The view to anchor to.
    /// - Returns: The layout constraints needed for this constraint.
    func constraintsForAnchoringTo(boundsOf view: UIView) -> [NSLayoutConstraint] {
        return [
            topAnchor.constraint(equalTo: view.topAnchor),
            leadingAnchor.constraint(equalTo: view.leadingAnchor),
            view.bottomAnchor.constraint(equalTo: bottomAnchor),
            view.trailingAnchor.constraint(equalTo: trailingAnchor)
        ]
    }
}
@propertyWrapper
public struct UsesAutoLayout<T: UIView> {
    public var wrappedValue: T {
        didSet {
            wrappedValue.translatesAutoresizingMaskIntoConstraints = false
        }
    }

    public init(wrappedValue: T) {
        self.wrappedValue = wrappedValue
        wrappedValue.translatesAutoresizingMaskIntoConstraints = false
    }
}

final class MyViewController {
    @UsesAutoLayout
    var label = UILabel()
}
