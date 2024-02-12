//
//  AWSDKHelper.swift
//  SwiftExampleApp
//
//  Copyright 2024 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import AWSDK
import Combine

@objcMembers
class AWSDKHelper: NSObject, AWControllerDelegate, ObservableObject {
    @Published var alert: CustomAlert?
    var checkDoneErrorCode: Int?
    static let shared: AWSDKHelper = .init()

    // This method gets called when Workspace ONE SDK finishes its setup.
    // At this point, Workspace ONE SDK has verified the enrollment, sets up Tunnel (if configured)
    // and applied Settings configured by your admin.

    // This method gets called once per launch of the application. Expect to recieve this
    // even if the application is launched from background (warm-start)

    // Note: This method is not guaranteed to be called on Main Thread. So executing any UI
    // related code in this callback may cause problems.
    func controllerDidFinishInitialCheck(error: NSError?) {
        guard let err = error else {
            // EVerything looks fine, Let's update the UI to present any secure content.
            checkDoneErrorCode = nil
            AWLogInfo("controllerDidFinishInitialCheck successfully")
            return
        }
        // At this point, we should take away access to all secure resources and should update the application
        // to display an error describing why user can not access the resource.
        checkDoneErrorCode = err.code
        alert = CustomAlert(title: String(localized: "initFail"), message: String(localized: "initFailMessage") + "\nError code: \(checkDoneErrorCode ?? -1)")
        AWLogInfo("controllerDidFinishInitialCheck failed with err : \(err)")
    }

    func startSDK() {
        checkDoneErrorCode = nil
        // Get the singleton instance for the Workspace ONE SDK's Controller.
        // Set callback scheme(URL Scheme) that Workspace ONE SDK should use to communication with Workspace ONE anchor applications.
        // This callback scheme should be one of the supported URL Schemes by this application.
        // This string should match with the entry in the info.plist.
        // Replace this with one of your application's supported URL Schemes.
        AWController.clientInstance().callbackScheme = "iosswiftUIsample"
        // The Apple app attestation service will be used to verify the bundle identifier of custom SDK apps.
        // `shouldAttestApp` setting the value true to enable app attestation
        // To disable app attestation set `shouldAttestApp` to `false`, Default value is true.
        AWController.clientInstance().shouldAttestApp = true
        // Every app must set the teamID property of the AWController instance before starting the SDK.
        // Ignored when `shouldAttestApp` is set to false.
        // Below `3E0YOUR0TEAM0ID0HERE0389` needs to be replaced with actual teamID
        AWController.clientInstance().teamID = "3E0YOUR0TEAM0ID0HERE0389"
        // Set the delegate for Workspace ONE SDK's Controller. This delegate will receive events from Workspace ONE SDK as callbacks.
        AWController.clientInstance().delegate = self
        // Finally. Start the Workspace ONE SDK.
        // Note: You need to start Workspace ONE SDK's Controller at most once per application launch.
        // Workspace ONE SDK's Controller will monitor for the application lifecycle and refreshes access, authorization.
        // So starting Workspace ONE SDK every-time just duplicates this and may not look nice from UI perspective as well.
        AWController.clientInstance().start()
    }

    /// AWController.handleOpenURL` method will reconnect the Workspace ONE SDK back to its previous state to continue.
    /// - Parameters:
    ///   - url: URL to open
    ///   - sourceApplication: Bundle ID of the app that sent the URL
    /// - Returns: boolean Value
    func handleOpenURL(url: URL, sourceApplication: String?) -> Bool {
        // If you are handling application specific URL schemes. Please make sure that the URL is not intended for Workspace ONE SDK's Controller.
        // An example way to perform this.
        let handledBySDKController = AWController.clientInstance().handleOpenURL(url, fromApplication: nil)
        if handledBySDKController  {
            AWLogInfo("Handed over open URL to AWController")
            // Workspace ONE SDK's Controller will continue with the result from Open URL.
            return true
        }
        return false
    }

    func loadApplicationInformation() -> Future<(ApplicationStatusInformation, SharedDeviceInformation), Error> {
        return Future { promise in
            DeviceInformationController.sharedController.fetchApplicationAssignmentStatus { statusInformation, sharedDeviceInfo, error in
                guard let statusInfo = statusInformation, let deviceInfo = sharedDeviceInfo else {
                    if  let error = error {
                        promise(.failure(error))
                    }
                    return
                }
                promise(.success((statusInfo, deviceInfo)))
            }
        }
    }

    func dlpEnabled() -> Bool {
        return  AWController.clientInstance().sdkProfile()?.restrictionsPayload?.enableDataLossPrevention ?? false
    }
}

// MARK: -  Encryption - Decryption
extension AWSDKHelper {

    /// API to encrypt the data that is passed
    /// - Parameter data: data that needs to be encrypted
    /// - Throws:  an error if any during encryption.
    /// - Returns: encrypted data of type Data
    internal func encrypt(_ data: Data) throws -> Data {
        return try AWController.clientInstance().encrypt(data)
    }


    /// API to decrypt the passed data
    /// - Parameter data: data that needs to be decrypted
    /// - Throws: an error if any during decryption
    /// - Returns: decrypted data of type Data
    internal func decrypt(_ data: Data) throws -> Data{
        return try AWController.clientInstance().decrypt(data)
    }
}

//MARK: - Remote Configuration: Custom settings
extension AWSDKHelper {
    /// customPayload : variable which holds custom settings.
    internal var customPayload : AWSDK.CustomPayload? {
        let customPayload = AWController.clientInstance().sdkProfile()?.customPayload
        return customPayload
    }
}
// MARK: - Device Compromised Status
extension AWSDKHelper {

    /// API to refresh compromised status of  device.
    /// - Parameter completion:handler that gets invoked with (isJailbroken: Bool, evaluationToken: String, evaluationIdentifier: String)
    /// NOTE:- identifier represents the rule with which compromised detection is evaluated & evaluationToken can be discarded by the developers
    internal func checkCompromisedStatus(completion: @escaping (_ isJailbroken: Bool, _ evaluationToken: String, _ identifier: String?) -> Void){
        DeviceInformationController.sharedController.refreshDeviceCompromisedStatus(completion: completion)
    }
}

// MARK: - Device Send Beacon Status
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

// MARK: - Enterprise Branding
extension AWSDKHelper {
    ///brandingPayload : variable which hold enterprise branding configuration.
    internal var brandingPayload: AWSDK.BrandingPayload? {
        let brandingPayload = AWController.clientInstance().sdkProfile()?.brandingPayload
        return brandingPayload
    }
}

// MARK: - Environment/User details
extension AWSDKHelper {
    /// API to update the userCredentials with current values after authentication is success full
    /// - Parameter completion: handler that gets invoked when updating the user credential is succeeds or fails. Use NSError object to get the reason for failure
    internal func updateUserCredentials(completion: @escaping (Bool, NSError?) -> Void){
        AWController.clientInstance().updateUserCredentials(completion: completion)
    }

    /// API to get cached account information from SDK.
    /// - Returns: Account object of type EnrollmentAccount or nil
    internal func fetchEnrollmentAccountInfo() -> EnrollmentAccount? {
        guard let account = AWController.clientInstance().account else {
            AWLogError("Error account is nill")
            return nil
        }
        return account
    }

    /// API to get ServerURL & Organisation information from SDK.
    /// - Returns: Environment Info object of type DeviceEnrollmentInfo or nil
    internal func fetchEnrollmentInfo() -> DeviceEnrollmentInfo? {
        guard let info = AWController.clientInstance().enrollmentInfo else {
            AWLogError("EnrollmentInfo is nill")
            return nil
        }
        return info
    }

    /// API to fetch userInformation of type UserInformation from UEM console
    /// - Parameter completion:  Handler that gets invoked when fetch succeeds or fails. Use NSError object to get the reason for failure when UserInformation is nil
    internal func retrieveUserInfo() -> Future<UserInformation, Error> {
        return Future { promise in
            UserInformationController.sharedInstance.retrieveUserInfo { userInfo, error in
                if let userInformation = userInfo {
                    promise(.success(userInformation))
                    return
                }
                if let errorReceived = error {
                    promise(.failure(errorReceived))
                }
            }
        }
    }
}

// MARK: - Session
extension AWSDKHelper {
    internal func handleChallengeForURLSession(challenge : URLAuthenticationChallenge,completionHandler: @escaping ((URLSession.AuthChallengeDisposition, URLCredential?) -> Swift.Void))  -> Bool {
        let awAuthHandleStatus = AWController.clientInstance().handleChallengeForURLSession(challenge: challenge, completionHandler: completionHandler)
        AWLogInfo("Did Workspace ONE SDK attempt to handle this challenge : \(awAuthHandleStatus)")
        return awAuthHandleStatus
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

// MARK: - Logs.
extension AWSDKHelper {

    public typealias logInfo = (data: Data, suggestedFileName: String)

    /// API  to fetch current SDK's log data
    /// - Throws: error of type NSError
    /// - Returns: current SDK's log data of type Data
    internal  func fetchSDKLogData() throws -> [logInfo] {
        let logProvider = WS1SDKDataProvider()
        var logInfos = [logInfo]()
        let logChunks = try logProvider.fetchSDKLogChunks()

        /// Use this logData and suggested name to create temporary file to
        /// either share or email.
        return try logProvider.fetchSDKLogChunks().map {
              return logInfo($0.data,$0.suggestedFileName)
            }
    }

    /// API to send existing SDK logs to UEM console
    /// - Parameter completion: handler that gets invoked when sending finishes successfully or fails. Use Bool value to check the status & NSError to get the reason for failure
    internal func sendLogData() -> Future<Bool, Error> {
        return Future { promise in
            AWController.clientInstance().sendLogDataWithCompletion { success, error in
                if let errorReceived = error  {
                    promise(.failure(errorReceived))
                } else {
                    promise(.success(success))
                }
            }
        }
    }

    internal func setUserPreferedLogLevel(logLevel: AWSDK.SettingLogLevel) {
         AWController.clientInstance().userPreferedLogLevel = logLevel
    }

    internal func getUserPreferedLogLevel() -> AWSDK.SettingLogLevel {
        return AWController.clientInstance().userPreferedLogLevel
    }
}

// MARK: - Stored certificates
extension AWSDKHelper {

    /// API to fetch all  the latest SDK certificates with their usage info
    /// - Parameter completion: handler that gets invoked when certificates are retrieved successfully.
    internal func retrieveStoredPublicCertificates(completion : @escaping AWController.RetrieveStoredCertficateCompletionHandler){
        AWController.clientInstance().retrieveStoredPublicCertificates(completion: completion)
    }

    /// API to fetch all  the latest SDK stored Identity certificate information.
    /// All valid PKCS12Certificate along with their passwords .
    /// - Parameter completion: handler that gets invoked when certificates are retrieved successfully.
    internal func exportIdentityCertificates(completion : @escaping AWController.IdentityCertificatesCompletionHandler){
        AWController.clientInstance().exportIdentityCertificates(completion: completion)
    }
}

// MARK: - Delegates
extension AWSDKHelper {
    func controllerDidReceive(profiles: [Profile]) {
        // This method to designed to provide a profile for application immediately after the controller has received
        //
        //
        // Usually, Applications receive Workspace ONE SDK Settings as a Profile.
        // Application will receive a profile updated by admin according to the following rules.
        //  1. When App is being launched for the first time.
        //  2. When Application is being killed and relaunched (cold-boot).
        //  3. After launched from the background and the last profile updated was more than 4 hours ago.
        //
        // In other cases, the cached profile will be returned through this method.
        //
        // Note: First time install and launch of the application requires a profile to be available.
        // Otherwise Workspace ONE SDK's Controller Start process will be halted and will be reported as an error.
        // Generally, this method will be called after `controllerDidFinishInitialCheck(error:)` except in
        // the instance of first time launch of the application.
        //
        AWLogVerbose("Workspace ONE SDK received \(profiles.count) profiles.")

        guard profiles.count > 0 else {
            AWLogError("No profile received")
            return
        }

        AWLogInfo("Now printing profiles received: \n")
        profiles.forEach { AWLogInfo("\(String(describing: $0))") }
    }

    func controllerDidWipeCurrentUserData() {
        // Please check for this method to handle cases when this device was unenrolled, or user tried to unlock with more than allowed attempts,
        // or other cases of compromised device detection etc. You may receive this callback at anytime during the app run.
        AWLogError("Application should wipe all secure data")
    }

    func controllerDidLockDataAccess() {
        // This optional method will give opportunity to prepare for showing lock screen and thus saving any sensitive data
        // before showing lock screen. This method requires your admin to set up authentication type as either passcode or
        // username/password.
        AWLogInfo("Controller did lock data access.")
    }

    func controllerWillPromptForPasscode() {
        // This optional method will be called right before showing unlock screen. It is intended to take care of any UI related changes
        // before Workspace ONE SDK's Controller will present its screens. This method requires your admin to set up authentication type as either passcode or
        // username/password.
        AWLogInfo("Controller did lock data access.")
    }

    func controllerDidUnlockDataAccess() {
        // This method will be called once user enters right passcode or username/password on lock screen.
        // This method requires your admin to set up authentication type as either passcode or
        // username/password.
        AWLogInfo("User successfully unlocked")
    }

    func applicationShouldStopNetworkActivity(reason: AWSDK.NetworkActivityStatus) {
        // This method gets called when your admin restricts offline access but detected that the network is offline.
        // This method will also be called when admin whitelists specific SSIDs that this device should be connected while using this
        // application and user is connected to different/non-whitelisted WiFi network.
        //
        // Application should look this callback and stop making any network calls until recovered. Look for `applicationCanResumeNetworkActivity`.
        AWLogError("Workspace ONE SDK Detected that device violated network access policy set by the admin. reason: \(String(describing: reason))")
    }

    func applicationCanResumeNetworkActivity() {
        // This method will be called when device recovered from restrictions set by admin regarding network access.
        AWLogInfo("Application can resume network activity.")
    }

}
