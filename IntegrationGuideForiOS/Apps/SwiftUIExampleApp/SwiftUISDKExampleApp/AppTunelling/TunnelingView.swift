//
//  TunnelingView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import WebKit
import Combine

enum NetworkType: String {
    case webview = "UIWebView"
    case urlSession = "NSURLSession"
    case wkwebview = "WKWebView"
}

struct TunnelingView: View {
    @State private var selectedNetworkType: NetworkType = .webview
    @State private var enteredUrlString: String = ""
    @StateObject var tunnelingViewModel = TunnelingViewModel()
    @State var wkwebView: WKWebView = WKWebView()
    @State var uiWebView: UIWebView = UIWebView()
    @State private var cancellable = Set<AnyCancellable>()
    
    var body: some View {
        VStack(alignment: .center) {
            Text(String(localized: "selectNetworkType"))
            TextField(
                String(localized: "tunnelingURLText"),
                text: $enteredUrlString
            )
            .disableAutocorrection(true)
            .keyboardType(.URL)
            Text(tunnelingViewModel.statusText)
            Picker("Pick?", selection: $selectedNetworkType) {
                Text(NetworkType.webview.rawValue)
                    .tag(NetworkType.webview)
                Text(NetworkType.urlSession.rawValue)
                    .tag(NetworkType.urlSession)
                Text(NetworkType.wkwebview.rawValue)
                    .tag(NetworkType.wkwebview)
            }
            .onChange(of: selectedNetworkType) { newValue in
                tunnelingViewModel.networkTypeSelectionChanged(networkType: selectedNetworkType)
            }
            .pickerStyle(.segmented)
            .navigationBarTitle(String(localized: "tunnelingNavigationTitle"))
            .navigationBarItems(trailing:
                                    Button(action: {
                tunnelingViewModel.goButtonTapped(urlString: enteredUrlString)
            }) {
                Text("GO")
            }
            )
            if tunnelingViewModel.shouldHideUIWebView {
                AWWKWebView(webWKView: wkwebView)
            }
            else {
                AWUIWebView(webUIView: uiWebView)
            }
        }
        .onAppear() {
            configureWKWebView()
            loadWebViews()
        }
    }

    func configureWKWebView() {
        wkwebView = WKWebView(frame: .zero, configuration: tunnelingViewModel.getWkWebViewConfig())
        #if swift(>=5.9)
        if #available(iOS 17.0,*) {
            do {
                // Enabling Tunnel on an instance of WKWebView.
                // WorkSpace One SDK needs to be intialised before enabling Tunnel.
                try self.wkwebView.enableTunnel()
            } catch let error {
                print("Failed to enable Tunnel - \(error)")
            }
        }
        #endif
    }

    func loadWebViews() {
        tunnelingViewModel.loadRequestUIWebView
            .receive(on: DispatchQueue.main)
            .sink { request in
                uiWebView.loadRequest(request)
            }
            .store(in: &cancellable)
        tunnelingViewModel.loadRequestWkWebView
            .receive(on: DispatchQueue.main)
            .sink { request in
                wkwebView.load(request)
            }
            .store(in: &cancellable)
        tunnelingViewModel.loadHtmlString
            .receive(on: DispatchQueue.main)
            .sink { (dataString, baseUrl) in
                uiWebView.loadHTMLString(dataString, baseURL: baseUrl)
            }
            .store(in: &cancellable)
        tunnelingViewModel.loadData
            .receive(on: DispatchQueue.main)
            .sink { (data, mimeType, encodingName, baseURL) in
                uiWebView.load(data, mimeType: mimeType, textEncodingName: encodingName, baseURL: baseURL)
            }
            .store(in: &cancellable)
    }
}

struct TunnelingView_Previews: PreviewProvider {
    static var previews: some View {
        TunnelingView()
    }
}

