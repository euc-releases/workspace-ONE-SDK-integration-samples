//
//  GeneralInformationView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import SwiftUI

struct InformationView: View {

    @StateObject private var viewModel = GeneralInformationViewModel()
    var body: some View {
        List {
            Section(header: Text(String(localized: "userInfo"))) {
                Text("\(String(localized: "userName")): \(viewModel.userName)")
                Text("\(String(localized: "groupID")): \(viewModel.groupID)")
                Text("\(String(localized: "email")): \((viewModel.email))")
                Text("\(String(localized: "fullName")): \(viewModel.fullName)")
                Text("\(String(localized: "domain")): \(viewModel.domain)")
                Text("\(String(localized: "userID")): \(viewModel.userID)")
            }
            Section(header: Text(String(localized: "appStatusInfo"))) {
                Text("\(String(localized: "enrolled")): \(viewModel.enrolled)")
                Text("\(String(localized: "isAppSupported")): \(viewModel.isAppSupported)")
                Text("\(String(localized: "isManaged")): \(viewModel.isManaged)")
                Text("\(String(localized: "consoleVersion")): \(viewModel.consoleVersion)")
            }
            Section(header: Text(String(localized: "deviceInfo"))) {
                Text("\(String(localized: "stagingEnabled")): \(viewModel.stagingEnabled)")
                Text("\(String(localized: "sharedDeviceStatus")): \(viewModel.sharedDeviceStatus)")
                Text("\(String(localized: "userContextID")): \(viewModel.userContextID)")
            }
        } .onAppear(perform: viewModel.performFetch)
    }
}
