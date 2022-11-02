//
//  FeaturesView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import SwiftUI

struct Feature: Identifiable {
    var id: UUID = UUID()
    var name: String
    var destination: AnyView
}

struct FeatureRow: View {
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
