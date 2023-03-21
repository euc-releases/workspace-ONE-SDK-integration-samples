# VMware Workspace ONE Software Development Kit (SDK)

## iOS And Android - Getting Started
This document explains how to integrate the Workspace ONE SDKs into your Apache Cordova apps.

For detailed information about the Workspace ONE SDK and managing internal apps, See the VMware Workspace ONE UEM Mobile Application Management Guide and the VMware Workspace ONE SDK Technical Implementation Guides located on the Workspace ONE Resources Portal at https://my.workspaceone.com/products/Workspace-ONE-SDK

## iOS Overview
In order to inject Workspace ONE SDK functionality into your Apache Cordova App, integrate the two systems.

### Requirements
* iOS 14.0+ (for iOS SDK component) / Xcode 14.x
* Visual Studio Code 
* Workspace ONE-enrolled iOS test device
* VMware Workspace One SDK Plugin for Apache Cordova from npm.

### Add App to the Workspace ONE UEM Console
Upload your internal app to the Workspace ONE UEM Console to register it with the system. This step enables UEM Console to identify the app and to add functionality to it. The Workspace ONE UEM MAM Guide details how to upload an internal app.

1. Export the app as a signed IPA.
2. Log into the Workspace ONE UEM Console as an administrator.
3. Navigate to Apps & Books > Applications > List View > Internal and then choose Add Application.
4. Select Upload > Local File, add the IPA file, and select Continue.
5. Select More and choose SDK.
6. Select the iOS Default Settings profile in the SDK Profile field.
7. Select Save and Assign to continue to the Assignment page.
8. Assign the app to a smart group and select a Push Mode.
9. Select Add, and Save & Publish the app to complete the upload process.

### Enable Communication Between the Intelligent Hub(formerly AirWatch Agent) and the app's IPA File
Expose a custom scheme in the Info.plist file in the Apache cordova project to enable the app to receive a call back from the Intelligent Hub. Your app receives communications from the Workspace ONE UEM Console through the Intelligent Hub. To expose the scheme, add a callback scheme registration and add a query scheme to your project.

### Add Callback Scheme Registration
1. In Xcode, navigate to Supporting Files.
2. Select the file -Info.plist.
3. Navigate to the URL Types section.
4. If it does not exist, add it at the Information Property List root node of the PLIST.
5. In the URL Types section, choose the Add URL Type button.
6. Set the values of Identifier and URL Schemes to the desired callback scheme.
7. Set the Role to Editor.
8. Configure trust for all Workspace ONE UEM anchor application schemes under the LSApplicationQueriesSchemes entry in the Information Property List.
9. Within the Array, Add following 3 values for anchor application.
  * Type String and Value **airwatch**.
  * Type String and Value **awws1enroll**.
  * Type String and Value **wsonesdk**.

### Add Support for QR Scan and FaceId
#### QR Scan
Include NSCameraUsageDescription in the application info.plist file to enable the SDK to scan QR codes with the device camera. Provide a description that devices prompt users to allow the application to enable this feature.
#### FaceID
Include NSFaceIDUsageDescription in the application info.plist file to enable the SDK to use FaceID. Provide a description that devices prompt users to allow the application to enable this feature. Consider controlling the message users read. If you do not include a description, the iOS system prompts users with native messages that might not align with the capabilities of the application.

## Android Overview
To integrate Workspace ONE SDKs into an existing Apache cordova app follow described steps.

### Requirements
* Android 8.0+ (for Android SDK component) / API level 23 OR above / Android Studio with the Gradle Android Build System (Gradle) 4.1.3 or later 
* Visual Studio Code 
* VMware Workspace One SDK Plugin for Apache Cordova from npm.
* Android test device running Lollipop and above.
* Intelligent Hub for Android from Google Playstore.
* Whitelisted Release/Debug signing key as explained below should be used for signing the Apache cordova application.

### Whitelist Signing Key
Before you can begin using the Workspace ONE SDK, you must ensure your application signing key is whitelisted with your Workspace ONE UEM Console. When your SDK-integrated application starts up, the SDK checks the signing public key with which it is signed. It compared againt the list of whitelisted apps to determine whether your application is trusted.

Workspace ONE allows whitelisting for both apps deployed internally or deployed through a public app store.

#### Internally Deployed Applications
1. After building the application apk, sign it using your own specific app signing key.
2. Upload the signed apk file to the Workspace ONE UEM Console as described below. Workspace ONE UEM Console extracts the application's public signing key and adds it to the whitelisted apps list  
  a) Log into the Workspace ONE UEM Console as an administrator. 
  b) Navigate to Apps & Books > Applications > List View > Internal and then choose Add Application. 
  c) Select Upload > Local File, add the APK file, and select Continue. 
  d) Select More and choose SDK. 
  e) Select the Android Default Settings profile in the SDK Profile field. 
  f) Select Save and Assign to continue to the Assignment page. 
  g) Assign the app to a smart group and select a Push Mode. 
  h) Select Add, and Save & Publish the app to complete the upload process.

#### Publicly Deployed Applications
For applications that are deployed publicly through the Play Store, send the public signing key of the application to AirWatch for whitelisting.

### Push App to Dev Device using App Catalog
In order for the Intelligent Hub to manage an app, it needs to be sent to the device. This can be done via an installation policy of Automatic or by pushing the app down once using the Hub's APP CATALOG. Once the app is listed in the Managed Apps section of the Hub, it is ready for local management.

## Usage
Adding following code in JS file will call setSDKEventListener API to set event handler to receive events from the SDK. And will be notified if SDK initialization is a success/failure by events 'initSuccess'/'initFailure'.
```js
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
  console.log('Running cordova' + cordova.platformId + '@' + cordova.version);
  initialiseSDK()
}

function initialiseSDK() {
    window.plugins.airwatch.setSDKEventListener(function(event, error) {
        if (event === "initSuccess") {
            console.log('Init Success');
        }
        else if(event === "initFailure") {
            alert("Init Failure");
        }
    });
}
```

## Functions 

**Functions available for Android and iOS**

```javascript
setSDKEventListener(listener)
```
Sets an event-handler function to receive events from the SDK. See the "Events" section. This should be called once Cordova fires 'deviceready' event. The listener callback should have two parameters, event and info.

```javascript
username(successCallback, errorCallback)
```
Gets the enrolled user's username. The username is returned as a string parameter to the successCallback function. On iOS using this API may show a screen to enter username and password if the app gets registered to WSOne UEM console via managed settings.

```javascript
groupId(successCallback, errorCallback)
```
Gets the enrolled user's group ID. The group ID will be returned as a string parameter to the successCallback function.

```javascript
serverName(successCallback, errorCallback)
```
Get the name of the server to which the device is enrolled. The server name will be returned as a string parameter to the successCallback function.

```javascript
allowCopyPaste(successCallback, errorCallback)
```
Gets the "allow offline use" setting for the profile. If true, then the user can use managed apps when not connected to the network. If false, the user cannot use managed apps when not connected to the network. The value is returned as a boolean parameter to the successCallback function.

```javascript
openFile (absolutepath, successCallback, errorCallback)
```
Opens the file specified by the absolute path in accordance with the data loss prevention settings as configured on the Workspace One UEM Console. The plugin restricts the files only to those whitelisted applications. Applications can have a custom implementation using restrictDocumentToApps and allowedApplications APIs. A success callback is invoked when the plugin is successfully able to open the file. In all other cases, an error callback is invoked with the corresponding error code.
To obtain the absolute path of the file (whether the file available bundled in the app or downloaded to the documents folder by the app), refer to the Cordova File plugin documentation, at https://cordova.apache.org/docs/en/latest/reference/cordova-plugin-file/.

Error code values for openFile
  * 2 - File not found at the path
  * 1 - Absolute path not specified
  * 0 - No error

```javascript
registerPushNotificationToken (token, successCallback, errorCallback)
```
API to set the Push Notification token. The token will be sent to Workspace One UEM Console by the plugin. Applications has to get the Push Notification token before calling this and pass the token. A success callback is invoked when the plugin is successfully able to set the token. In all other cases, an error callback is invoked with the corresponding error code.

Error code values for Push Notification token registration
  * 1 - Token not specified
  * 0 - No error

## Events
The Workspace One SDK sends event notifications to applications that use it when certain conditions arise. To receive these notifications in a Cordova app, call setSDKEventListener(listener). The listener is a function that accepts two parameters. The first parameter will be a string containing the name of the event, as listed below. The second is an object that contains additional data if relevant to that type of event.

### Events available for Android and iOS

`initSuccess`

Sent when the Workspace One SDK is successfully initialized. All the functions of the plugin, other than setSDKEventListener(listener), are available after this event is fired. See 'Functions' section above.


`initFailure`

Sent when the Workspace One SDK cannot be successfully initialized. Any future calls to the plugin fails.

### Events available for iOS only
`wipe`

Sent when the device receives a "wipe" instruction from the console.

`lock`

Sent when the device receives a "lock" instruction from the console.

`unlock`

Sent when the device receives an "unlock" instruction from the console.

`stopNetworkActivity`

Sent when the device receives a "stopNetworkActivity" instruction from the console. The event data parameter contains a property named status with a numeric value as specified in the table below.

`resumeNetworkActivity`

Sent when the device receives a "resumeNetworkActivity" instruction from the console.

### Network status values for stopNetworkActivity:

|   Status Code  |  Network State          | 
| -------------- | ----------------------- |
|       -2       |  initializing           |  
|       -1       |  normal                 |  
|        1       |  cellular data disabled | 
|        2       |  roaming                | 
|        3       |  proxy failed           | 
|        4       |  network not reachable  | 

Best Practices
--------------

### Add Business Logic After the initSuccess Event Fires

For applications using the Workspace One SDK, have all business logic of the application added after the SDK fires initSuccess event. This means the SDK successfully initialized and the user is authenticated successfully, if applicable.

The application waits until the initSuccess or initFailure event fires. Until the Workspace One SDK completely loads and the initSuccess event fires, the application shows a waiting screen or a loading screen to give feedback to the user that the applicaiton is in the process of loading and starting.

### Do Not Add Business Logic to the deviceready Event

Unless there is a specific business requirement to perform operations before the Workspace One SDK is initialized, do not add logic to the deviceready event. The SDK shows an authentication screen above Cordova WebView which can block the application's UI until the SDK is initialized. The initFailure event fires when if SDK initialization somehow fails. The application listens to this event and shows the corresponding error in the application.

## Feature Implementation
### Branding
### iOS
Once the plugin is added to the app, there will be a new bundle (AWSDKDefaults) that is also added to the app. This bundle has AWSDKDefaultSettings.plist that already has few entries in it. To enable branding , new entries has to be added to it. For details on the entries to be added to enable branding in your app, navigate to https://code.vmware.com/web/sdk/Native/airwatch-ios and search for Branding.

### Android
Please follow the steps mentioned in https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/04Branding/WorkspaceONE_Android_Branding.md to brand the splash screen, the app logo gets branded as per the images from the WS1 UEM console branding payload.

### SSO
### iOS
To enable multiple apps built with the plugin share common authentication session and other SDK info, please follow the steps mentioned in the document at https://code.vmware.com/web/sdk/Native/airwatch-ios under section "Keychain Access Group Entitlements".

### Android 
Nothing specific coding /configuration to be done on Android.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause