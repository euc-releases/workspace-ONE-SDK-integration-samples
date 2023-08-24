# Workspace ONE SDK For Apache Corodva

Use this document to install the VMware Workspace One SDK Plugin for Apache Cordova. The plugin helps enterprise app developers add enterprise- grade security, conditional access, and compliance capabilities to mobile applications.

## Supported Components 
This plugin works with the listed component versions.
* Workspace ONE UEM Console 2203+ (may need to be higher depending on specific features)
* Android 8.0+ (for Android SDK component) / API level 23 OR above / Android Studio with the Gradle Android Build System (Gradle) 4.1.3 or later
* iOS 14.0+ (for iOS SDK component) / Xcode 14.x

## Build Example Application
Please refer following steps to run the example app.
1. Clone the repository.
2. Run this command `npm install`.
3. Build for iOS/android. Add the platforms that you want to target your app and ensure they get saved to config.xml and package.json.
  * For iOS use `cordova platform add ios`.
  * For Android use `cordova platform add android`.

## Initial Setup
Please find the [Prerequisites](GettingStarted.md) for using the Workspace One SDK Plugin for Apache Cordova.

## Installation
To install the plugin, type cordova plugin add airwatch-sdk-plugin at the command line. This should be added before any other plugin is added to the app. Note: The download from NPM, usually takes 2-3 minutes on average, over high speed internet connection.

## Package Installation
```
npm i airwatch-sdk-plugin
```

## Feature Description
Initialization of the SDK adds the listed features to your application, depending on the configurations set in the SDK profile in the Workspace One UEM Console.

* Application level passcode
* Application level tunneling of network traffic
* Integrated authentication / single sign on
* Data loss prevention
  * Disable Screenshot (Android only)
  * Restrict open-in for documents, web links, and email to approved applications only Restrict copy/paste (SDK provides flag value)
  * Restrict access to app when device is offline
  * Branding of VMware AirWatch splash screens when SDK application is launched on device.

## Feature Implementation
Please follow document at implementation [GettingStarted.md](GettingStarted.md).

## Release Notes
1. Latest versions of Workspace ONE SDKs (23.7.0 for iOS and 23.07 for Android).

## Workspace One SDK Documentation
For further details about the Workspace One SDK, navigate to https://my.workspaceone.com/products/Workspace-ONE-SDK and select the required platform, SDK version and Workspace ONE UEM console version.

## Questions and Feedback
For any questions/feedback or to report an issue, please reach out to VMware support teams at https://secure.workspaceone.com/login

## License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause