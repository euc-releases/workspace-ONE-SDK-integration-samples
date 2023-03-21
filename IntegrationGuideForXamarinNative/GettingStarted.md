
# VMware Workspace ONE Software Development Kit (SDK)

## iOS And Android - Getting Started

This document explains how to integrate the Workspace ONE SDKs into your Xamarin-built apps.

 For detailed information about the Workspace ONE SDK and managing internal apps, See the **VMware Workspace ONE UEM Mobile Application Management Guide** and the **VMware Workspace ONE SDK Technical Implementation Guides** located on the Workspace ONE Resources Portal at <https://my.workspaceone.com/products/Workspace-ONE-SDK>

## iOS Overview

In order to inject Workspace ONE SDK functionality into your  Xamarin AWSDK App, integrate the two systems.

### Requirements

* iOS 14.0+ (for iOS SDK component) / Xcode 14.x
* Visual Studio 2022 (17.5 and above) for Windows / Visual Studio 2022 for Mac (17.5 and above)
* Workspace ONE-enrolled iOS test device
* The Workspace ONE Xamarin SDK (AWSDK) package from the Nuget Store.
* A Xamarin iOS app to integrate with the Workspace ONE SDK
** If you do not have a suitable application, you can create a new application in Visual Studio and integrate the SDK into that.

### Add App to the Workspace ONE UEM Console

Upload your internal app to the Workspace ONE UEM Console to register it with the system. This step enables UEM Console to identify the app and to add functionality to it. The **Workspace ONE UEM MAM Guide** details how to upload an internal app.

1. In Xamarin, export the app as a signed IPA.
2. Log into the Workspace ONE UEM Console as an administrator.
3. Navigate to **Apps & Books > Applications > List View > Internal** and then choose **Add Application**.
4. Select **Upload > Local File**, add the IPA file, and select **Continue**.
5. Select **More** and choose **SDK**.
6. Select the **iOS Default Settings** profile in the *SDK Profile* field.
7. Select **Save and Assign** to continue to the **Assignment** page.
8. Assign the app to a smart group and select a **Push Mode**.
9. Select **Add**, and **Save & Publish** the app to complete the upload process.

### Add Required SDK package to Project

Add the Workspace ONE SDK nuget package to your Xamarin project to enable the Xamarin IPA file in UEM Console to recognize and apply the Workspace ONE SDK functionality.

1. Open Visual Studio.
2. Right-click **Packages** and select **Add Packages**.
3. Search AWSDK on nuget.org and add it to the project.
4. Enable the *Assembly* check box if it isn't already and select **Ok**.

### Enable Communication Between the Intelligent Hub(formerly AirWatch Agent) and the Xamarin IPA File

Expose a custom scheme in the Info.plist file in the Xamarin project to enable the app to receive a call back from the Intelligent Hub. Your app receives communications from the Workspace ONE UEM Console through the Intelligent Hub. To expose the scheme, add a callback scheme registration and add a query scheme to your Xamarin project.

#### Add Callback Scheme Registration

1. Double-click the Info.plist file in your Xamarin project.
2. Select the **Advanced** tab.
3. In the **URL Types** section, choose the **Add URL Type** button.
4. Set the values of *Identifier* and *URL Schemes* to the desired callback scheme.
5. Set the *Role* to **Editor**.
6. Save the file.

#### Add SDK App Query Scheme

1. Double-click the Info.plist file in your Xamarin project.
2. Select the **Source** tab, and choose *Add new entry*.
3. Select the green "PLUS" in the selected row.
4. Double click **Custom Property**, and change it to **LSApplicationQueriesSchemes**.
5. Change the *Type* from *String* to *Array*.
6. Within the *Array*,  Add following 3 values for  anchor application.
   * Type String  and Value **airwatch**.
   * Type String  and Value  **awws1enroll**.
   * Type String  and Value   **wsonesdk**.
7. Save the file.

### Add Support for QR Scan and FaceId

#### QR Scan

Include NSCameraUsageDescription in the application info.plist file to enable the SDK to scan QR codes with the device camera.
Provide a description that devices prompt users to allow the application to enable this feature.

#### FaceID

Include NSFaceIDUsageDescription in the application info.plist file to enable the SDK to use FaceID.
Provide a description that devices prompt users to allow the application to enable this feature. Consider controlling the message users read. If you do not include a description, the iOS system prompts users with native messages that might not align with the capabilities of the application.

### Add an App Delegate to the Xamarin Project

To complete integration of Xamarin and Workspace ONE SDK within your app use a custom Application Delegate. Create a class to act as an `AWSDKDelegate`, define the callback scheme within the class, and set the class to recognize when initialization is complete.

1. Create a class to act as an `AWSDKDelegate` and to receive SDK callbacks.  

        using Foundation;
        using System.Diagnostics;
        using System;
        using AirWatchSDK;

        namespace sdkSampleApp
        {
            public class AirWatchSDKManager: AWSDKDelegate
            {            
                private static AirWatchSDKManager instance;
                private static string LogCategory = "AirWatchSDK";

                // private, use the Instance below
                private AirWatchSDKManager ()
                {
                }

                // singleton
                public static AirWatchSDKManager Instance {
                    get {
                        if (instance == null) {
                            instance = new AirWatchSDKManager ();
                        }
                        return instance;
                    }
                }

                // Below are all the protocol methods defined in AWSDKDelegate
                // Add customization here for SDK results

                override public void InitialCheckDoneWithError (NSError error)
                {
                    // Add any SDK Customization here
                    string message = String.Format ("InitialCheckDoneWithError received {0}, SDK initialized if no error", error);
                    Debug.WriteLine (message, LogCategory);
                }

                override public void ReceivedProfiles (AWProfile[] profiles)
                {
                    // Add any SDK Customization here
                    string message = String.Format ("ReceivedProfiles received {0}", profiles);
                    Debug.WriteLine (message, LogCategory);
                }

                override public void Wipe ()
                {
                    // Add any SDK Customization here
                    Debug.WriteLine ("Wipe command received", LogCategory);
                }

                override public void Lock ()
                {
                    // Add any SDK Customization here
                    Debug.WriteLine ("Lock command received", LogCategory);
                }

                override public void Unlock ()
                {
                    // Add any SDK Customization here
                    Debug.WriteLine ("Unlock command received", LogCategory);
                }

                public override void StopNetworkActivity(AWNetworkActivityStatus status)
                {
                    // Add any SDK Customization here
                    Debug.WriteLine("StopNetworkActivity received", LogCategory);
                }

                override public void ResumeNetworkActivity ()
                {
                    // Add any SDK Customization here
                    Debug.WriteLine ("ResumeNetworkActivity received", LogCategory);
                    }
                }
            }

2. Add the listed functionality to the `AppDelegate.cs`.

        using System;
        using ObjCRuntime;
        using System.Diagnostics;
        using AirWatchSDK;

       ...

        public override bool FinishedLaunching (UIApplication application, NSDictionary launchOptions)
        {
            if (Runtime.Arch == Arch.SIMULATOR) {
                Debug.WriteLine ("Running in Simulator, skipping initialization of the AirWatch SDK!");
                return true;
            } else {
                Debug.WriteLine ("Running on Device, beginning initialization of the AirWatch SDK.");

                // Configure the Controller by:
                var sdkController = AWController.ClientInstance ();
                // 1) defining the callback scheme so the app can get called back,
                sdkController.CallbackScheme = "mysamplescheme"; // defined in Info.plist
                // 2) set the delegate to know when the initialization has been completed.
                sdkController.Delegate = AirWatchSDKManager.Instance;
                AWController.ClientInstance ().Start ();

                return true;
            }
        }

        public override void OnActivated (UIApplication application)
        {
        }

        public override bool HandleOpenURL (UIApplication application, NSUrl url)
        {
            return AWController.ClientInstance ().HandleOpenURL (url, "");
        }

If you are using Xamarin Forms on iOS, you need to add this to your AppDelegate as well.  This will expose the `UIWindow` that is part of the iOS `AppDelegate` but is hidden in `Xamarin.Forms.Platform.iOS.FormsApplicationDelegate`:

    [Export("window")]
    public UIWindow GetWindow()
    {
        return UIApplication.SharedApplication.Windows[0];
    }

### Debug Your Application

Your application is now SDK-enlightened!  If you do not see an SSO passcode, ensure that the Organization Group has **Single Sign On** enabled and that an **Authentication Type** is configured. These configurations are explained in the **Workspace ONE UEM MAM Guide**.

## Android Overview

To integrate Workspace ONE Android SDK Xamarin components into an existing Xamarin Android app follow described steps.

### Requirements

* Visual Studio 2022 (17.5 and above) for Windows / Visual Studio 2022 for Mac (17.5 and above)
* Workspace ONE Xamarin Android SDK binaries from the Nuget Store.
* Android test device running Marshmallow and above.
* Android 8.0+ (for Android SDK component) / API level 23 OR above 
* Intelligent Hub(formerly AirWatch Agent v7.0+) for Android from Google Playstore.
* Whitelisted Release/Debug signing key as explained below should be used for signing the Xamarin android application.

**Note: Applications using the Workspace ONE SDK for Xamarin must make changes in their HTTP networking classes to support Android 10 (Q) devices. Applications must use version 1.4.0+ of AWSDK nuget package, and use the HTTP Client classes provided in the Android component. For more details, please refer to the [troubleshooting guide](#troubleshooting-guide).**

### Integrating Workspace ONE SDK

1. While integrating **Workspace ONE SDK**, application method count may exceed 64k due to library dependencies. Enable Multi-Dex option for the app in Visual Studio.
2. Add the VMware Workspace ONE SDK package from the NuGet Gallery.
3. Add 
    1. Xamarin.GooglePlayServices.Base (v71.1610.4) 
    2. Xamarin.GooglePlayServices.SafetyNet (v71.1600.4)
    3. Xamarin.AndroidX.Core.SplashScreen (v1.0.0) 
    4. Xamarin.AndroidX.Lifecycle.Process (v2.5.1.2)
    5. Xamarin.AndroidX.Startup.StartupRuntime (v1.0.0)
4. Initialize Workspace ONE SDK:
    a) Extend the application class of the Xamarin app from **AWApplication** class of Workspace ONE SDK. Override the **MainActivityIntent** to return application's main landing activity. Move app's `onCreate()` business logic to `onPostCreate()`.

    **Application class**

        namespace XamarinAndroidSampleApp.Landing
        {
            [Application(Label = "@string/app_name", Icon = "@drawable/AppLogo", Theme = "@style/AppTheme")]

            public class SampleApplication : AWApplication
            {
                public SampleApplication(IntPtr handle, JniHandleOwnership ownerShip)
                : base(handle, ownerShip)
                {
                }

                public override Intent MainActivityIntent
                {
                    get
                    {
                    var intent = new Intent(ApplicationContext, typeof(MainActivity)); // MainActivity is application's main landing activity
                    return intent;
                    }
                }

                public override Intent MainLauncherIntent
                {
                    get
                    {
                    var intent = new Intent(ApplicationContext, typeof(GatewaySplashActivity));
                    return intent;
                    }
                }

                public override bool MagCertificateEnable
                {
                    get
                    {
                    return true; // to fetch mag certificate during initial setup
                    }
                }

                public override bool IsInputLogoBrandable
                {
                    get
                    {
                    return true; // to brand application logo
                    }
                }

                public override void OnPostCreate()
                {
                base.OnPostCreate();
                // App specific code here
                }


               public override void OnSSLPinningValidationFailure(string host1, X509Certificate cert)
               {
               }

               public override void OnSSLPinningRequestFailure(string host1, X509Certificate cert)
               {
               }
            }
        }

    b) Add **AirWatchSDKIntentService** by setting the name as `<AppPackageName>.AirWatchSDKIntentService`.
    c) Set `GatewaySplashActivity` as your main launching activity and and `AirWatchSDKBroadcastReceiver` broadcast receiver declarations in manifest file. With these changes, the manifest file should have entries as shown below:

    **Manifest File**

        <application android:name="SampleApplication" android:theme="@style/AppTheme" tools:replace="android:label">
        <activity android:name="com.airwatch.gateway.ui.GatewaySplashActivity" android:label="@string/app_name" android:exported="true">
        <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
        </activity>
        <activity android:name=".MainActivity" android:label="@string/app_name" android:exported="true"/>
        <provider android:name="androidx.startup.InitializationProvider" android:authorities="${applicationId}.androidx-startup" tools:node="remove" />
        <receiver android:name="com.airwatch.sdk.AirWatchSDKBroadcastReceiver" android:permission="com.airwatch.sdk.BROADCAST" android:exported="true">
        <intent-filter>
        <action android:name="com.airwatch.xamarinsampleapp.airwatchsdk.BROADCAST" />
        </intent-filter>
        <intent-filter>
        <action android:name="android.intent.action.PACKAGE_ADDED" />
        <action android:name="android.intent.action.PACKAGE_REMOVED" />
        <action android:name="android.intent.action.PACKAGE_REPLACED" />
        <action android:name="android.intent.action.PACKAGE_CHANGED" />
        <action android:name="android.intent.action.PACKAGE_RESTARTED" />
        <data android:scheme="package" />
        </intent-filter>
        </receiver>
        </application>

    d) For authentication, timeout and data-loss prevention features, all the application activities should extend from `Com.Airwatch.Gateway.UI.GatewayBaseActivity`. It allows the application to handle the lifecycle correctly and to manage the state of **Workspace ONE SDK**.

#### Note

    If the app cannot extend  AWApplication class, it can also use the delegate approach, more details at <https://docs.vmware.com/en/VMware-Workspace-ONE-UEM/services/SDK_Android/GUID-B59ECD0A-92CF-4EC8-8A39-34B80F1D8788.html>

### Features

1. Application level branding, authentication, timeout behavior and offline access are enforced by following by above integration process and extending AirWatch base activity.
2. For application level tunneling, you can use normal HTTP clients or android webview. It follows the Tunnel URLs setting on the console. Please refer to sample app code.
3. For Integrated authentication, application should use AirWatch wrapped HTTP clients or AWWebView for Basic, NTLM or client certificate based authentication. Please refer to sample app code.
4. Data Loss Prevention:
    a) Restricting screenshot as per policy is enforced when your activity extends GatewayBaseActivity.
    b) For restricting copy-paste, application should use AirWatch wrapped textviews like AWEditText, AWTextView and AWWebView.
    c) In order to restrict OpenIn for documents, weblinks and email, use `UriOpenerFactory.Instance.OpenUri(context, uri)` and `UriOpenerFactory.Instance.OpenUri(context, filepath)` as shown in the sample app.
5. Custom Settings: Inorder to get custom settings set on the console, SDKManager API needs to be used. In general any SDKManager API can be used in below manner:

        Using SDKManager APIs

        try
        {
            // Below call will return SDKManager instance readily
            // as it is already initialised as part of Login flow.
            var sdkmanager = SDKManager.Init(this);
            textView.Text = sdkmanager.CustomSettings;
        } catch (AirWatchSDKException e)
        {
            // exception while using SDKManager API
        }

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

### Troubleshooting Guide

**Q. On Android 10 (Q), HTTP requests fail with a "407 Proxy Authentication Required" error when the "VMware Tunnel Proxy" option is enabled.**

A. To resolve this error, applications need to use the AW wrapper classes provided for the WebView and HTTP clients in the `Com.Airwatch.Gateway.Clients` package. This is required because, on Android 10 (Q), the Android platform removed access to the [/proc/net file system](https://developer.android.com/about/versions/10/privacy/changes#proc-net-filesystem). This change caused the SDK's internal authentication process to fail. The AW wrapper classes present in the `Com.Airwatch.Gateway.Clients` package perform the authentication. 

This solution is available from AWSDK nuget package version 1.4.0. Please refer to the Xamarin-AWSDK Sample application's [`TunnelActivity`](https://github.com/vmwareairwatchsdk/Xamarin-AWSDK/blob/master/samples/XamarinAndroidSampleApp/XamarinAndroidSampleApp/Tunneling/TunnelingActivity.cs) to see the usage of wrapper classes.


# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause
