# ws1-sdk-flutter
Use this document to install the VMware Workspace One SDK Plugin for Flutter. The plugin helps enterprise app developers add enterprise-grade security, conditional access, and compliance capabilities to mobile applications.

## Package installation
Add plugin as dependency to the application pubspec.yaml

```script
dependencies:
  flutter:
    sdk: flutter

  workspaceone_sdk_flutter:^23.10.0

```

`$  dart pub get`

## Supported Components
This plugin works with the listed component versions.

* Workspace ONE UEM Console 2109+ (may need to be higher depending on specific features)
* Android 5.0+ (for Android SDK component) / API level 21 OR above / Android Studio with the Gradle Android Build System (Gradle) 3.3.0 or later / Workspace ONE Intelligent Hub for Android version 21.9 or later
* iOS 14.0+ (for iOS SDK component) / Xcode 14.0.1 or later



## Initial Setup
<medium>Please find the [Prerequisites](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForFlutter/GettingStarted.md) for using the Flutter SDK </medium>


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
3. Add WS1EventImpl
```kotlin
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.airwatch.sdk.profile.AnchorAppStatus
import com.airwatch.sdk.profile.ApplicationProfile
import com.airwatch.sdk.shareddevice.ClearReasonCode
import com.airwatch.event.WS1AnchorEvents
import org.koin.core.component.KoinComponent

class WS1EventImpl : WS1AnchorEvents,KoinComponent {
override fun onApplicationConfigurationChange(bundle: Bundle?, context: Context) {}
override fun onApplicationProfileReceived(context: Context, s: String, applicationProfile: ApplicationProfile) {
Log.d("SDK Init", "onApplicationProfileReceived")
}

    override fun onClearAppDataCommandReceived(context: Context, clearReasonCode: ClearReasonCode) {
        Log.d("SDK Init", "onClearAppDataCommandReceived")
    }

    override fun onAnchorAppStatusReceived(context: Context, anchorAppStatus: AnchorAppStatus) {}
    override fun onAnchorAppUpgrade(context: Context, b: Boolean) {}
}

```

4. Update your Android Application subclass as follows
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

        override fun getEventHandler(): WS1AnchorEvents {
            return WS1EventImpl()
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
Please follow document at [implementation](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForFlutter/GettingStarted.md).

## Release Notes
* First release of Workspace One SDK for Flutter support.
* Latest versions of Workspace One SDKs (23.4.0 for iOS and 23.04 for Android).

## Workspace One SDK Documentation
For further details about the Workspace One SDK, navigate to https://my.workspaceone.com/products/Workspace-ONE-SDK and select the required platform, SDK version and Workspace ONE UEM console version.

## Questions and Feedback
For any questions/feedback or to report an issue, please reach out to VMware support teams at https://secure.workspaceone.com/login