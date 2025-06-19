# Workspace ONE SDK For Apache Corodva

Use this document to install the Omnissa Workspace One SDK Plugin for Apache Cordova. The plugin helps enterprise app developers add enterprise- grade security, conditional access, and compliance capabilities to mobile applications.

## Supported Components 
This plugin works with the listed component versions.
* Workspace ONE UEM Console 2306+ (may need to be higher depending on specific features)
* Android 7.0+ (for Android SDK component) / API level 26 OR above / Android Studio with the Gradle Android Build System (Gradle) 8.2.2+ or later / Workspace ONE Intelligent Hub for Android version 25.02 or later
* iOS & iPadOS 16+ or later (for iOS SDK component) / Xcode 16+ or later

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
  * Branding of Omnissa AirWatch splash screens when SDK application is launched on device.

## Feature Implementation
Please follow document at implementation [GettingStarted.md](GettingStarted.md).

## Release Notes
1. Latest versions of Workspace ONE SDKs (25.2.1 for iOS and 25.02.1 for Android).

## Workspace One SDK Documentation
For further details about the Workspace One SDK, navigate to https://my.workspaceone.com/products/Workspace-ONE-SDK and select the required platform, SDK version and Workspace ONE UEM console version.

## Questions and Feedback
For any questions/feedback or to report an issue, please reach out to Omnissa support teams at https://secure.workspaceone.com/login

#