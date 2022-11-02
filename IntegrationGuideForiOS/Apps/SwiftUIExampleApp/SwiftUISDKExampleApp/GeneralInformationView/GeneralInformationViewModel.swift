//
//  GeneralInformationViewModel.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import Foundation
import Combine
import AWSDK

class GeneralInformationViewModel: ObservableObject {
    private var cancellable = Set<AnyCancellable>()
    @Published var userName: String = ""
    @Published var fullName: String = ""
    @Published var groupID: String = ""
    @Published var email: String = ""
    @Published var domain: String = ""
    @Published var userID: String = ""

    @Published var enrolled: String = ""
    @Published var isAppSupported: String = ""
    @Published var isManaged: String = ""
    @Published var consoleVersion: String = ""

    @Published var stagingEnabled: String = ""
    @Published var sharedDeviceStatus: String = ""
    @Published var userContextID: String = ""

    func fetchUserInformation() {
        AWSDKHelper.shared.retrieveUserInfo()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { _ in},
                  receiveValue: { [weak self] information in
                self?.userName = information.userName
                self?.fullName = information.firstName + information.lastName
                self?.email = information.email
                self?.domain = information.domain
                self?.userID = information.userIdentifier
                self?.groupID = information.groupID
            })
            .store(in: &cancellable)
    }

    func fetchApplicationInformation() {
        AWSDKHelper.shared.loadApplicationInformation()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: {_ in },
                  receiveValue: { [weak self] (applicationStatusInformation, sharedDeviceInformation) in
                self?.enrolled = String(describing: applicationStatusInformation.enrollmentStatus)
                self?.isAppSupported = String(describing: applicationStatusInformation.isAppSupported)
                self?.isManaged = String(describing: applicationStatusInformation.isDeviceManaged)
                self?.consoleVersion = String(describing: applicationStatusInformation.consoleVersion)
                self?.stagingEnabled = String(describing: sharedDeviceInformation.isDeviceStagingEnabled)
                self?.sharedDeviceStatus = String(describing: sharedDeviceInformation.sharedDeviceStatus.rawValue)
                self?.userContextID = String(describing: sharedDeviceInformation.userContextID)
            })
            .store(in: &cancellable)
    }

    func performFetch() {
        fetchUserInformation()
        fetchApplicationInformation()
    }
}
