# ws1-sdk-flutter
Use this document to install the VMware Workspace One SDK Plugin for Flutter. The plugin helps enterprise app developers add enterprise-grade security, conditional access, and compliance capabilities to mobile applications.

## Package installation
Add plugin as dependency to the application pubspec.yaml 

```script
dependencies:
  flutter:
    sdk: flutter

  workspace_one_sdk_flutter:^22.3.0

```

`$  dart pub get`

## Supported Components
This plugin works with the listed component versions.

* Workspace ONE UEM Console 2005 or later
* Android v8.0+ / API Level 23+ 
* iOS 12.0+ / Xcode 13/13.1/13.2.1

## Initial Setup
<medium>Please find the [Prerequisites](https://github.com/vmwareairwatchsdk/vmware-wsone-sdk-flutter/blob/master/GettingStarted.md) for using the Flutter SDK </medium>


## Additional Setup
### iOS
1. Add the AWSDK through Swift Package Manager.
Click [here](https://github.com/vmwareairwatchsdk/iOS-WorkspaceONE-SDK) for integrating the AWSDK framework through Swift Package Manager

2. Add following code in AppDelegate
```objc
-(BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options
{
  //Add following code for posting Notification for URL
  NSNotification *info = [[NSNotification alloc]initWithName:@"UIApplicationOpenURLOptionsSourceApplicationKey" object:url userInfo:options];
  [[NSNotificationCenter defaultCenter] postNotification:info];
  
  return YES;
}
```
3. Add Post Install script in Podfile.

```ruby
post_install do |installer|
  installer.pods_project.targets.each do |target|
    flutter_additional_ios_build_settings(target)
  end
  # Add this line to get the AWSDK Swift Package from SPM
  $workspaceone_sdk_flutter.post_install(installer)
end
```


### Android

1. Modify AndroidManifest.xml for Main Launcher
```xml
     <activity
        android:name=".MainActivity"
        android:label="@string/app_name"
        android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
        android:launchMode="singleTask"
        android:windowSoftInputMode="adjustResize">
    </activity>
    <activity
        android:name="com.airwatch.login.ui.activity.SDKSplashActivity" android:label="@string/app_name">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" /> 
        </intent-filter>
    </activity>
```
2. Update your Main Activity 
```kotlin
    import com.vmware.workspaceone_sdk_flutter.WorkspaceOneSdkActivity
    class MainActivity: WorkspaceOneSdkActivity() {
    }
```
3. . Update your Android Application subclass as follows 
    -  Declare that the class implements the WorkspaceOneSDKApplication interface.
    -  Move the code from the body of your onCreate method, if any, to an override of the AWSDKApplication onPostCreate method.
    -  Override the AWSDKApplication getMainActivityIntent() method to return an Intent for the application’s main Activity.
    -  Override the following Android Application methods: 
        - attachBaseContext

```kotlin
    import com.vmware.workspaceone_sdk_flutter.WorkspaceOneSdkApplication
    class MainApplication : WorkspaceOneSdkApplication() {

        // Application-specific overrides : Comment onCreate() out and move the code to onPostCreate()

        //  @Override
        //  public void onCreate() {
        //    super.onCreate();
        //  }

        // Application-specific overrides : Copy all the code from onCreate() to onPostCreate()
        override fun onPostCreate() {
            super.onPostCreate()
        }

        override fun attachBaseContext(base: Context?) {
            super.attachBaseContext(base)
            attachBaseContext(this)
        }

        override fun getMainActivityIntent(): Intent {
            return Intent(this,MainActivity::class.java)
        }
    }
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
    * Branding of VMware AirWatch splash screens when SDK application is launched on device

 ## Feature Implementation
 Please follow document at [implementation](https://github.com/vmwareairwatchsdk/vmware-wsone-sdk-flutter/blob/master/GettingStarted.md).

## Release Notes
First release of Workspace One SDK for Flutter support.
Latest versions of Workspace One SDKs (22.2 for iOS and Android).

## Workspace One SDK Documentation
For further details about the Workspace One SDK, navigate to https://my.workspaceone.com/products/Workspace-ONE-SDK and select the required platform, SDK version and Workspace ONE UEM console version.

## Questions and Feedback
For any questions/feedback or to report an issue, please reach out to VMware support teams at https://secure.workspaceone.com/login

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause