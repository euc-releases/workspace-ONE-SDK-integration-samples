//
//  DLPView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI


struct DLPView: View {

    let dlpFeatureList = [DLPFeature(name: String(localized: "copyPaste"), destination: AnyView(CopyPasteView())),
                          DLPFeature(name: String(localized: "Screenshot"), destination: AnyView(ScreenshotView())),
                          DLPFeature(name: String(localized: "email"), destination: AnyView(ComposeEmailView())),
                          DLPFeature(name: String(localized: "openURL"), destination: AnyView(OpenURLView())),
                          DLPFeature(name: String(localized: "customKeyboard"), destination: AnyView(KeyboardRestrictionView())),
                          DLPFeature(name: String(localized: "documentController"), destination: AnyView(DocumentInteractionController()))
                        ]

    var body: some View {
            List(dlpFeatureList) { dlpFeature in
                DLPFeatureRow(title: dlpFeature.name, destination: dlpFeature.destination)
            }
            .navigationBarTitle(Text(String(localized: "DLPNavigationTitle")), displayMode: .inline)
    }
}

struct DLPFeature: Identifiable {
    var id: UUID = UUID()
    var name: String
    var destination: AnyView
}

struct DLPFeatureRow: View {
    var titleString: String
    var destinationView: AnyView
    var body: some View {
        NavigationLink(destination: destinationView) {
            Text(titleString)
        }
    }
    init(title: String, destination: AnyView) {
        self.titleString = title
        self.destinationView = destination
    }
}
