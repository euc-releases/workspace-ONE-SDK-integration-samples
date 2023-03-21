
# VMware Workspace ONE Software Development Kit (SDK)

## iOS And Android - Getting Started

This document explains how to integrate the Workspace ONE SDKs into your React-Native-built apps.

 For detailed information about the Workspace ONE SDK and managing internal apps, See the **VMware Workspace ONE UEM Mobile Application Management Guide** and the **VMware Workspace ONE SDK Technical Implementation Guides** located on the Workspace ONE Resources Portal at <https://my.workspaceone.com/products/Workspace-ONE-SDK>

## iOS Overview

In order to inject Workspace ONE SDK functionality into your  React-Native AWSDK App, integrate the two systems.

### Requirements

* iOS 14.0+ (for iOS SDK component) / Xcode 14.x
* Visual Studio Code 
* Workspace ONE-enrolled iOS test device
* The Workspace ONE React-Native SDK  package from npm.
* A React-Native iOS app to integrate with the Workspace ONE SDK
** If you do not have a suitable application, you can create a new application and integrate the SDK into that.

### Add App to the Workspace ONE UEM Console

Upload your internal app to the Workspace ONE UEM Console to register it with the system. This step enables UEM Console to identify the app and to add functionality to it. The **Workspace ONE UEM MAM Guide** details how to upload an internal app.

1. In React-Native, export the app as a signed IPA.
2. Log into the Workspace ONE UEM Console as an administrator.
3. Navigate to **Apps & Books > Applications > List View > Internal** and then choose **Add Application**.
4. Select **Upload > Local File**, add the IPA file, and select **Continue**.
5. Select **More** and choose **SDK**.
6. Select the **iOS Default Settings** profile in the *SDK Profile* field.
7. Select **Save and Assign** to continue to the **Assignment** page.
8. Assign the app to a smart group and select a **Push Mode**.
9. Select **Add**, and **Save & Publish** the app to complete the upload process.

### Enable Communication Between the Intelligent Hub(formerly AirWatch Agent) and the React-Native IPA File

Expose a custom scheme in the Info.plist file in the React-Native project to enable the app to receive a call back from the Intelligent Hub. Your app receives communications from the Workspace ONE UEM Console through the Intelligent Hub. To expose the scheme, add a callback scheme registration and add a query scheme to your React-Native project.

#### Add Callback Scheme Registration

1. In Xcode, navigate to Supporting Files.
2. Select the file <YourAppName> -Info.plist.
3. Navigate to the URL Types section.
4. If it does not exist, add it at the Information Property List root node of the PLIST.
5. In the **URL Types** section, choose the **Add URL Type** button.
6. Set the values of *Identifier* and *URL Schemes* to the desired callback scheme.
7. Set the *Role* to **Editor**.
8. Configure trust for all Workspace ONE UEM anchor application schemes under the LSApplicationQueriesSchemes entry in the Information Property List.
9. Within the *Array*,  Add following 3 values for  anchor application.
   * Type String  and Value **airwatch**.
   * Type String  and Value  **awws1enroll**.
   * Type String  and Value   **wsonesdk**.

### Add Support for QR Scan and FaceId

#### QR Scan

Include NSCameraUsageDescription in the application info.plist file to enable the SDK to scan QR codes with the device camera.
Provide a description that devices prompt users to allow the application to enable this feature.

#### FaceID

Include NSFaceIDUsageDescription in the application info.plist file to enable the SDK to use FaceID.
Provide a description that devices prompt users to allow the application to enable this feature. Consider controlling the message users read. If you do not include a description, the iOS system prompts users with native messages that might not align with the capabilities of the application.

## Android Overview

To integrate Workspace ONE Android SDK React-Native components into an existing React-Native Android app follow described steps.

### Requirements

* Android 8.0+ (for Android SDK component) / API level 23 OR above / Android Studio with the Gradle Android Build System (Gradle) 4.1.3 or later 
* Visual Studio Code
* The Workspace ONE React-Native SDK  package from npm
* Android test device running Lollipop and above.
* Intelligent Hub for Android from Google Playstore.
* Whitelisted Release/Debug signing key as explained below should be used for signing the React-Native android application.

### Whitelist Signing Key

Before you can begin using the **Workspace ONE SDK**, you must ensure your application signing key is whitelisted with your Workspace ONE UEM Console. When your SDK-integrated application starts up, the SDK checks the signing public key with which it is signed. It compared againt the list of whitelisted apps to determine whether your application is trusted.

Workspace ONE allows whitelisting for both apps deployed internally or deployed through a public app store.

#### Internally Deployed Applications

1. After building the application apk, sign it using your own specific app signing key.
2. Upload the signed apk file to the Workspace ONE UEM Console as described below. Workspace ONE UEM Console extracts the application's public signing key and adds it to the whitelisted apps list
    a) Log into the Workspace ONE UEM Console as an administrator.
    b) Navigate to **Apps & Books > Applications > List View > Internal** and then choose *Add Application*.
    c) Select *Upload > Local File*, add the APK file, and select Continue.
    d) Select **More** and choose *SDK*.
    e) Select the *Android Default Settings* profile in the SDK Profile field.
    f) Select *Save and Assign* to continue to the *Assignment* page.
    g) Assign the app to a smart group and select a *Push Mode*.
    h) Select *Add*, and *Save & Publish* the app to complete the upload process.

#### Publicly Deployed Applications

For applications that are deployed publicly through the Play Store, send the public signing key of the application to AirWatch for whitelisting.

**Note: Contact your professional services representative for the process of whitelisting the public signing key.**

### Push App to Dev Device using App Catalog

In order for the **Intelligent Hub** to manage an app, it needs to be sent to the device.  This can be done via an installation policy of **Automatic** or by pushing the app down once using the **Hub**'s *APP CATALOG*.  Once the app is listed in the *Managed Apps* section of the Hub, it is ready for local management.

## Usage

```javascript to initialize the SDK
import { NativeModules} from 'react-native';
const {WorkspaceOneSdk } = NativeModules;

export default class App extends Component {
componentDidMount() {

     // Start SDK.
      WorkspaceOneSdk.startSDK()
      const eventEmitter = new NativeEventEmitter(NativeModules.WorkspaceOneSdk);
      this.eventListner = eventEmitter.addListener('initSuccess',(event) => {
        console.log("SDK Init Success",event);
      });
      this.eventListner = eventEmitter.addListener('initFailure',(event) => {
        console.log("SDK Init Failed",event);
      });
      
  }
}
```

```javascript to access Environment info
import React, { Component } from 'react';
import { NativeModules,StyleSheet, View, Button,Platform, Text} from 'react-native';
const {WorkspaceOneSdk } = NativeModules;
export default class Information extends Component {

    constructor(){
 
        super();
   
        this.state = {
   
            UserName:'User Not Found',
            GroupId: 'GroupId Not Found',
            ServerName: 'Server Name Not Found'
        }
   
    }


    componentDidMount = async() => {

          try {
           const  useName = await WorkspaceOneSdk.userName();
            this.setState({
                      UserName: 'User Name : ' + useName
                  });
          } catch (error) {
            console.error(error);
          }

          try {
            var groupId = await WorkspaceOneSdk.groupId();
            this.setState({
                      GroupId:  'Group Id : ' + groupId 
                  });
          } catch (error) {
            console.error(error);
          }

          try {
            const serverName = await WorkspaceOneSdk.serverName();
            this.setState({
              ServerName: 'Server Name : ' + serverName 
                  });
          } catch (error) {
            console.error(error);
          }

    }

}
```
## Functions

Functions available for Android and iOS

```javascript
WorkspaceOneSdk.startSDK()
```
This will start the SDK initailization and will be notified if SDK initialization is success/failure by events 'initSuccess'/'initFailure'.

```javascript
WorkspaceOneSdk.userName()
```
Gets the enrolled user's username. The username is returned as a string. On iOS using this API may show a screen to enter username and password if the app gets registered to WSOne UEM console via managed settings.


```javascript
WorkspaceOneSdk.groupId()
```
Gets the enrolled user's group ID. The group ID will be returned as a string.


```javascript
WorkspaceOneSdk.serverName()
```
Get the name of the server to which the device is enrolled. The server name will be returned as a string.

```javascript
WorkspaceOneSdk.allowCopyPaste()
```
Gets the "allow copy/paste" setting for the profile. If true, then the user can copy and paste between managed apps. If false then the user cannot copy and paste between managed apps. The value is returned as a boolean.


```javascript
WorkspaceOneSdk.customSettings()
```
Gets any custom settings provided in the app's profile. The value will be returned as a string parameter.

## Feature Implementation
### Branding
### iOS
To enable brancding add AWSDKDefaultSettings.plist to app bundle and add new entries to the plist. For details on the entries to be added to enable branding in your app, navigate [here](https://code.vmware.com/web/sdk/Native/airwatch-ios) and search for Branding.

### Android
Please follow the steps mentioned in [GitHub](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/04Branding/WorkspaceONE_Android_Branding.md) to brand the splash screen, the app logo gets branded as per the images from the WS1 UEM console branding payload.

### SSO
### iOS
To enable multiple apps built with the plugin share common authentication session and other SDK info, please follow the steps mentioned in the document [here](https://code.vmware.com/web/sdk/Native/airwatch-ios) under section "Keychain Access Group Entitlements".

### Android
Nothing specific coding /configuration to be done on Android.

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause

