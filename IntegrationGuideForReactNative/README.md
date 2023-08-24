# ws1-sdk-react-native
Use this document to install the VMware Workspace One SDK Plugin for React-Native. The plugin helps enterprise app developers add enterprise- grade security, conditional access, and compliance capabilities to mobile applications.

## Supported Components
This plugin works with the listed component versions.

* Workspace ONE UEM Console 2203+ (may need to be higher depending on specific features)
* Android 8.0+ (for Android SDK component) / API level 23 OR above / Android Studio with the Gradle Android Build System (Gradle) 4.1.3 or later
* iOS 14.0+ (for iOS SDK component) / Xcode 14.x


## Initial Setup
<medium>Please find the [Prerequisites](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForReactNative/GettingStarted.md) for using the React Native SDK </medium>

## Package installation

`$ npm install ws1-sdk-react-native --save`

### Mostly automatic installation

`$ react-native link ws1-sdk-react-native`

## Additional Setup
### iOS
Add following code in AppDelegate
```objective-c
-(BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options
{
  //Add following code for posting Notification for URL
  NSNotification *info = [[NSNotification alloc]initWithName:@"UIApplicationOpenURLOptionsSourceApplicationKey" object:url userInfo:options];
  [[NSNotificationCenter defaultCenter] postNotification:info];
  
  return YES;
}
```

### Android

1. Add the library files location to the application build configuration
```java
    repositories {
    //Old implementation
    // flatDir {
    //     dirs "$rootDir/../node_modules/ws1-sdk-react-native/android/libs"
    // }
    //Change to new Maven URL
    jcenter()
        maven {
            url 'https://vmwaresaas.jfrog.io/artifactory/Workspace-ONE-Android-SDK/'
        }
    }
```

2. Modify AndroidManifest.xml for Main Launcher
```java
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
```java
import com.workspaceonesdk.WorkspaceOneSdkActivity;
public class MainActivity extends WorkspaceOneSdkActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "example";
  }
}
```

3. Add SDKEventImplClass as below

```java
import com.workspaceonesdk.WorkspaceOneSdkApplication;
public class MainApplication extends WorkspaceOneSdkApplication implements ReactApplication {

    // Application-specific overrides : Comment onCreate() out and move the code to onPostCreate()

    //  @Override
    //  public void onCreate() {
    //    super.onCreate();
    //  }

    // Application-specific overrides : Copy all the code from onCreate() to onPostCreate()
    @Override
    public void onPostCreate() {
        super.onPostCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString("debug_http_host", "localhost:8088").apply();
        SoLoader.init(this, /* native exopackage */ false);
        initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
        
        // Code from the application's original onCreate() would go here
    }
    
    
    public void attachBaseContext(@NotNull Context base) {
        super.attachBaseContext(base);
        attachBaseContext(this);
    }

    
    @NotNull
    @Override
    public Intent getMainActivityIntent() {
        // Replace MainActivity with application's original main activity
        return new Intent(getApplicationContext(), MainActivity.class);
    }

    @NotNull
    @Override
    public WS1AnchorEvents getEventHandler() {
        return new SDKEventImpl();
    }
}
```
4. Update your Android Application subclass as follows
    -  Declare that the class implements the WorkspaceOneSDKApplication interface.
    -  Move the code from the body of your onCreate method, if any, to an override of the AWSDKApplication onPostCreate method.
    -  Override the AWSDKApplication getMainActivityIntent() method to return an Intent for the application’s main Activity.
    -  Override the following Android Application methods:
        - attachBaseContext

```java
import com.workspaceonesdk.WorkspaceOneSdkApplication;
public class MainApplication extends WorkspaceOneSdkApplication implements ReactApplication {

    // Application-specific overrides : Comment onCreate() out and move the code to onPostCreate()

    //  @Override
    //  public void onCreate() {
    //    super.onCreate();
    //  }

    // Application-specific overrides : Copy all the code from onCreate() to onPostCreate()
    @Override
    public void onPostCreate() {
        super.onPostCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString("debug_http_host", "localhost:8088").apply();
        SoLoader.init(this, /* native exopackage */ false);
        initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
        
        // Code from the application's original onCreate() would go here
    }
    
    
    public void attachBaseContext(@NotNull Context base) {
        super.attachBaseContext(base);
        attachBaseContext(this);
    }

    
    @NotNull
    @Override
    public Intent getMainActivityIntent() {
        // Replace MainActivity with application's original main activity
        return new Intent(getApplicationContext(), MainActivity.class);
    }

    @NotNull
    @Override
    public WS1AnchorEvents getEventHandler() {
        return new SDKEventImpl();
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
 Please follow document at [implementation](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForReactNative/GettingStarted.md).

## Release Notes
* Updated Version of WorkspaceOne SDKs(23.7 for iOS and Android)
    * **Change build.gradle for Maven URL as mentioned above in [Android Implementation](#Android)**

## Workspace One SDK Documentation
For further details about the Workspace One SDK, navigate to [Workspace-ONE-SDK](https://my.workspaceone.com/products/Workspace-ONE-SDK) and select the required platform, SDK version and Workspace ONE UEM console version.


## Questions and Feedback
For any questions/feedback or to report an issue, please reach out to [VMware support teams](https://secure.workspaceone.com/login)


# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause
