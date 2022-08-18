//
//  AWSDKHelper.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import AWSDK

@objcMembers
internal class AWSDKHelper: NSObject {

    static let shared: AWSDKHelper = .init()
    lazy private var controller = AWController.clientInstance()
    /// customPayload : variable which holds custom settings.
    internal var customPayload : AWSDK.CustomPayload? {
        let customPayload = controller.sdkProfile()?.customPayload
        return customPayload
    }
    /// variable which hold boolean value which indicates whether Data loss prevention is enabled.
    internal var dlpEnabled: Bool? {
        return AWController.clientInstance().sdkProfile()?.restrictionsPayload?.enableDataLossPrevention
    }

    ///brandingPayload : variable which hold enterprise branding configuration.
    internal var brandingPayload: AWSDK.BrandingPayload? {
        let brandingPayload = controller.sdkProfile()?.brandingPayload
        return brandingPayload
    }

    private override init() { }

    /// Intializer for AWSDK
    internal func initializeSDK() {
        // Get the singleton instance for the Workspace ONE SDK's Controller.

        // Set callback scheme(URL Scheme) that Workspace ONE SDK should use to communication with Workspace ONE anchor applications.
        // This callback scheme should be one of the supported URL Schemes by this application.
        // This string should match with the entry in the info.plist.
        // Replace this with one of your application's supported URL Schemes.
        controller.callbackScheme = "iosswiftsample"

        // Set the delegate for Workspace ONE SDK's Controller. This delegate will receive events from Workspace ONE SDK as callbacks.
        controller.delegate = self

        // Finally. Start the Workspace ONE SDK.
        // Note: You need to start Workspace ONE SDK's Controller at most once per application launch.
        // Workspace ONE SDK's Controller will monitor for the application lifecycle and refreshes access, authorization.
        // So starting Workspace ONE SDK every-time just duplicates this and may not look nice from UI perspective as well.
        controller.start()
        AWLogVerbose("Starting Workspace ONE SDK")
    }

    /// AWController.handleOpenURL` method will reconnect the Workspace ONE SDK back to its previous state to continue.
    /// - Parameters:
    ///   - openurl: URL to open
    ///   - fromApplication: Bundle ID of the app that sent the URL
    /// - Returns: boolean Value
    internal  func handleOpenURL(_ openurl: URL, fromApplication: String?) -> Bool{
        // If you are handling application specific URL schemes. Please make sure that the URL is not intended for Workspace ONE SDK's Controller.
        // An example way to perform this.
        let handledBySDKController = AWController.clientInstance().handleOpenURL(openurl, fromApplication: fromApplication)
        if handledBySDKController  {
            AWLogInfo("Handed over open URL to AWController")
            // Workspace ONE SDK's Controller will continue with the result from Open URL.
            return true
        }
        return false
    }

    /// Device token is set in this function which is received from APNs. Setting a new value of APNs initiate a call to Console, so to avoid unnecessary calls to console, assign token value to AWController only when token has really changed. Setting token to nil clears token value in console and won't be used for push notifications anymore.
    /// - Parameters:
    ///   - token: device token string.
    internal func setDeviceToken(_ token : String) {
        if controller.APNSToken != token {
            self.sendBeacon(updatedAPNSToken: token) { success, error in
                guard let err = error else {
                    AWLogInfo("Device token sent successfully")
                    return
                }
                AWLogError("Update APNS token failed with error: \(err)")
            }
        }
    }
}

/// UserInformation Info
extension AWSDKHelper  {

    /// API to fetch userInformation of type UserInformation from UEM console
    /// - Parameter completion:  Handler that gets invoked when fetch succeeds or fails. Use NSError object to get the reason for failure when UserInformation is nil
    internal  func retrieveUserInfo(completion: @escaping (UserInformation?, NSError?) -> Void){
        UserInformationController.sharedInstance.retrieveUserInfo(completionHandler: completion)
    }


    /// API to update the userCredentials with current values after authentication is success full
    /// - Parameter completion: handler that gets invoked when updating the user credential is succeeds or fails. Use NSError object to get the reason for failure
    internal func updateUserCredentials(completion: @escaping (Bool, NSError?) -> Void){
        controller.updateUserCredentials(completion: completion)
    }
}

extension AWSDKHelper {

    /// API to get cached account information from SDK.
    /// - Returns: Account object of type EnrollmentAccount or nil
    internal func fetchEnrollmentAccountInfo() -> EnrollmentAccount? {
        guard let account = AWController.clientInstance().account else {
            AWLogError("Error account is nill")
            return nil
        }
        return account
    }

    /// API to send existing SDK logs to UEM console
    /// - Parameter completion: handler that gets invoked when sending finishes successfully or fails. Use Bool value to check the status & NSError to get the reason for failure
    internal func sendLogs(_ completion: @escaping (Bool, NSError?) -> Void) {
        controller.sendLogDataWithCompletion(completion)
    }
}

extension AWSDKHelper {

    /// API to fetch application status and shared device Information from UEM console.
    /// - Parameter completion: handler that gets invoked when fetch succeeds or fails. Use Error object to get the reason for failure
    internal  func fetchApplicationAssignmentStatus(completion: @escaping (AWSDK.ApplicationStatusInformation?, AWSDK.SharedDeviceInformation?, Error?) -> Void){
        DeviceInformationController.sharedController.fetchApplicationAssignmentStatus(completion: completion)
    }
}

// Encryption - Decryption
extension AWSDKHelper {

    /// API to encrypt the data that is passed
    /// - Parameter data: data that needs to be encrypted
    /// - Throws:  an error if any during encryption.
    /// - Returns: encrypted data of type Data
    internal func encrypt(_ data: Data) throws -> Data {
        return try controller.encrypt(data)
    }


    /// API to decrypt the passed data
    /// - Parameter data: data that needs to be decrypted
    /// - Throws: an error if any during decryption
    /// - Returns: decrypted data of type Data
    internal func decrypt(_ data: Data) throws -> Data{
        return try controller.decrypt(data)
    }
}

extension AWSDKHelper{

    /// API to fetch all  the latest SDK certificates
    /// - Parameter completion: handler that gets invoked when certificates are retrieved successfully.
    internal func retrieveStoredPublicCertificates(completion : @escaping AWController.RetrieveStoredCertficateCompletionHandler){
        AWController.clientInstance().retrieveStoredPublicCertificates(completion: completion)
    }
}

extension AWSDKHelper {

    /// API  to fetch current SDK's log data
    /// - Throws: error of type NSError
    /// - Returns: current SDK's log data of type Data
    internal  func fetchSDKLogData() throws -> Data {
        let logProvider = WS1SDKDataProvider()
        let logData = try logProvider.fetchSDKLogData()
        return logData
    }
}

extension AWSDKHelper {

    /// API to delete the SDK data from the this app along with the apps that share iOS keychain with this app. This API should  be invoked only after the discussion with support team. you cannot recover the data deleted by this API.
    internal  func cleanSDKContainer() {
        let cleaner = WS1SDKContainerCleaner()
        cleaner.destroyContainerData()
    }
}

extension AWSDKHelper {

    /// API to refresh compromised status of  device.
    /// - Parameter completion:handler that gets invoked with (isJailbroken: Bool, evaluationToken: String, evaluationIdentifier: String)
    /// NOTE:- identifier represents the rule with which compromised detection is evaluated & evaluationToken can be discarded by the developers
    internal func checkCompromisedStatus(completion: @escaping (_ isJailbroken: Bool, _ evaluationToken: String, _ identifier: String?) -> Void){
        DeviceInformationController.sharedController.refreshDeviceCompromisedStatus(completion: completion)
    }
}

extension AWSDKHelper {
    typealias SendBeaconCompletion = (Bool, Error?) -> ()

    /// call this API to send beacon data to UEM console manually.
    /// - Parameter completion: handler that gets invoked when beacon is sent successfully. Use error object to get the reason for failure.
    internal func sendBeaconData(completion: @escaping SendBeaconCompletion) {
        let beaconTransmitter = SDKBeaconTransmitter.sharedTransmitter
        // To send immediately
        beaconTransmitter.sendDeviceStatusBeacon { (success, error) in
            completion(success, error)
        }
    }


    /// call this API to send beacon data with APNS token  to UEM console manually.
    /// - Parameters:
    ///   - updatedAPNSToken: APNS token to send to console.
    ///   - completion:  handler that gets invoked when beacon is sent successfully. Use error object to get the reason for failure.
    internal func sendBeacon(updatedAPNSToken: String, completion: SendBeaconCompletion?) {
        let beaconTransmitter = SDKBeaconTransmitter.sharedTransmitter
        // To send immediately
        beaconTransmitter.sendBeacon(updatedAPNSToken: updatedAPNSToken, completion: completion)
    }
}

extension AWSDKHelper {

    /// API allows to send custom analytics event to console.
    func sendAnalytics() {
        let analytics = AnalyticsHandler.sharedInstance
        analytics.recordEvent(
            AWSDK.AnalyticsEvent.customEvent,
            eventName: "EVENT_NAME",
            eventValue: "EVENT_VALUE",
            valueType: AWSDK.AnalyticsEventValueType.string
        )
    }
}

// MARK: - Integrated Authentication.
extension AWSDKHelper {
    func handle(challenge: URLAuthenticationChallenge?, completionHandler: ((URLSession.AuthChallengeDisposition, URLCredential?) -> Void)?) -> Bool {

        do {
            try AWController.clientInstance().canHandle(protectionSpace: challenge?.protectionSpace)
            return AWController.clientInstance().handleChallengeForURLSession(challenge: challenge, completionHandler: completionHandler)
        }
        catch _ {
            return false
        }
    }

    func validate(serverTrust: SecTrust) -> Bool {
        return AWController.clientInstance().validate(serverTrust: serverTrust, trustStore: .deviceAndCustom, strictness: .ignore)
    }
}
