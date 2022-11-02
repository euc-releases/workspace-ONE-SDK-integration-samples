//
//  IntegratedAuthenticationView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import WebKit
import Combine

struct IntegratedAuthenticationView: View {
    @StateObject var model = IntegratedAuthViewModel()
    @State private var enteredUrlString: String = ""
    private let wkWebView = WKWebView(frame: .zero)
    @State private var cancellable = Set<AnyCancellable>()

    var body: some View {
        VStack {
            HStack {
                TextField(
                    String(localized: "urlPlaceholder"),
                    text: $enteredUrlString
                )
                .disableAutocorrection(true)
                .keyboardType(.URL)
                Button {
                    model.goButtonTapped(urlString: enteredUrlString)
                } label: {
                    Text(String(localized: "goButtonTitle"))
                }
            }
            Text(model.statusText)
            AWWKWebView(webWKView: wkWebView)
        }
        .alert(item: $model.displayAlert) { alert in
            Alert(title: Text(alert.title), message: Text(alert.message), dismissButton: .default(Text(String(localized: "Ok"))))
        }
        .onAppear {
            model.checkSDKAccount()
            loadWebView()
        }
    }

    func loadWebView() {
        model.loadData
            .receive(on: DispatchQueue.main)
            .sink { (data, mimeType, encodingName, baseURL) in
                wkWebView.load(data, mimeType: mimeType, characterEncodingName: encodingName, baseURL: baseURL)
            }
            .store(in: &cancellable)
        model.loadHtmlString
            .receive(on: DispatchQueue.main)
            .sink { (dataString, baseUrl) in
                wkWebView.loadHTMLString(dataString, baseURL: baseUrl)
            }
            .store(in: &cancellable)
    }
}

struct IntegratedAuthenticationView_Previews: PreviewProvider {
    static var previews: some View {
        IntegratedAuthenticationView()
    }
}

struct CustomAlert: Identifiable {
    var id: String { title }
    var title: String = ""
    var message:  String = ""
    init(title: String = "", message: String = "") {
        self.title = title
        self.message = message
    }
}
