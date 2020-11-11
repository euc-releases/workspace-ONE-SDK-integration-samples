# Base Integration Guide
## Workspace ONE for Android
Android applications can be integrated with the VMware Workspace ONE® platform,
by using its mobile software development kit. Complete the tasks below as a base
for feature integration.

This document is part of the Workspace ONE Integration Guide for Android set.

# Table of Contents
{{TOC}}

# Introduction
The tasks detailed below represent the basic steps in integrating your Android
application with the Workspace ONE platform. The tasks you will complete depend
on the required integration level of your application.

Integration at the Framework level is necessary if the application will make use
of platform features such as authentication, single sign-on, data encryption, or
networking.

To integrate at the **Client level**, do the following tasks:

1.  [Add the Client SDK].
2.  [Initialize the Client SDK].

To integrate at the **Framework level**, do the following tasks:

1.  [Add the Client SDK].
2.  [Add the Framework].
3.  [Initialize the Framework].

Note that you don't add Client SDK initialization if you are integrating at the
Framework level.

## Integration Guides
This document is part of the Workspace ONE Integration Guide for Android set.

See other guides in the set for

-   an overview of integration levels and the benefits of each.
-   details of the integration preparation tasks, which must be done before the
    tasks in this document.

An overview that includes links to all the guides is available

-   in Markdown format, in the repository that also holds the sample code:  
    [https://github.com/vmware-samples/...IntegrationOverview.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/01Overview/WorkspaceONE_Android_IntegrationOverview.md)

-   in Portable Document Format (PDF), on the VMware website:  
    [https://code.vmware.com/...IntegrationOverview.pdf](https://code.vmware.com/docs/12354/WorkspaceONE_Android_IntegrationOverview.pdf)

## Compatibility
Instructions in this document have been tested with the following software
versions.

Software                                         | Version
-------------------------------------------------|--------
Workspace ONE SDK for Android                    | 20.10
Workspace ONE management console                 | 20.4
Android Studio integrated development environment| 4.1
Gradle plugin for Android                        | 4.1

# Integration Paths Diagram
The following diagram shows the tasks involved in base integration and the order
in which they can be completed. Integration Preparation is a prerequisite to
base integration. Framework integration is a prerequisite to integrating any of
the framework features, which are covered by other guides.

![**Diagram 1:** Base Integration paths](IntegrationPathsClientFramework.svg)

# Task: Add Client SDK [Add the Client SDK]
Adding the Client SDK is a Workspace ONE platform integration task for Android
application developers. It applies to all levels of platform integration.

**If you haven't installed your application via Workspace ONE** at least once,
then do so now. If you don't, the application under development won't work when
installed via the Android Debug Bridge (adb). Instructions for installing via
Workspace ONE can be found in the [Integration Guides] document set, in the
Integration Preparation guide.

The first step will be to set up the build configuration and files. These
instructions assume that your application has a typical project structure, as
follows:

-   *Project* files in the root directory.
-   *Application* module in a sub-directory.
-   Separate `build.gradle` files for the project and application.

## Project Structure Diagram
The following diagram illustrates the expected project directory structure, and
the locations of changes to be made.

![**Diagram 2:** Project structure and locations of changes](ProjectStructure.svg)

**Tip**: It might be easier to see the structure, and identify which Gradle file
is which, in the Android Studio project navigator if you select the Project
view, instead of the Android view.

## Software Development Kit Download Structure Diagram
The following diagram illustrates the directory structure of the SDK download. 

![**Diagram 3:** Download structure of the SDK for Android](../02Preparation/DownloadStructure.svg)

Files from within the above structure are copied under your application project
in the following instructions.

## Instructions
Proceed as follows.

### Build Configuration and Files [BuildConfigurationAndFilesClientSDKIntegration]
First, update the build configuration and add the required library files.

1.  Update the Gradle Android plugin version, if necessary.

    In the project build.gradle file, check the Android plugin version. This is
    typically near the top of the file, inside the `buildscript` block, in the
    `dependencies` sub-block. The top of the file might look like this:

        buildscript {
            ...
            repositories {
                ...
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:4.0.2'
                ...
            }
        }
    
    In this example, the Gradle Android plugin version is 4.0.2

    Ensure that the plugin version is at least 3.4.2

    The location of this change is shown in the [Project Structure Diagram].

2.  Add the required libraries to the build.

    In the application build.gradle file, in the `dependencies` block, add
    references to the required libraries. (The library files will be copied in
    the next step.) For example:

        dependencies {
            implementation fileTree(dir: 'libs', include: ['*.jar'])
            implementation ...
            implementation ...

            // Following lines are added to integrate Workspace ONE at the Client level ...

            // Workspace ONE libraries that are part of the SDK.
            implementation (name:'AirWatchSDK-20.10', ext:'aar')
            implementation(name:"ws1-android-logger-1.1.0", ext:'aar')

            // Third party libraries that are distributed with the SDK.
            implementation 'com.google.code.gson:gson:2.4'

            // Third party libraries that are hosted remotely.
            implementation "androidx.multidex:multidex:2.0.0"
            implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3'
            implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3'
            implementation "androidx.lifecycle:lifecycle-runtime:2.2.0"
            implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
            annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.2.0") {
                exclude group:'com.google.guava', module:'guava'
            }

            ...
        }

    The location of this change is shown in the [Project Structure Diagram].

3.  Copy the required library files.

     The code snippet in the previous step indicates which libraries are:

    -   Workspace ONE libraries that are part of the SDK.
    -   Third party libraries that are distributed with the SDK.
    -   Third party libraries that are hosted remotely.

    The files that must be copied are those that are part of the SDK, or are
    distributed with it.

    All these files can be found in the SDK distribution, under one or other of
    the `Libs` sub-directories or their `dependencies` sub-directories. See the
    [Software Development Kit Download Structure Diagram].
    
    Copy the files into your project, under the application module
    sub-directory, in the `libs` sub-directory. The location is shown in the
    [Project Structure Diagram]. If the sub-directory doesn't exist, create it
    now.

    The Client SDK library, `AirWatchSDK`, can be added as an AAR or a JAR file.
    Both are included in the download.

    If you prefer, you can instead take the gson library from its default online
    location. The version distributed with the SDK isn't customized. It is
    included for convenience and completeness only.

4.  Add the library files' location to the application build configuration.

    In the application build.gradle file, add a `repositories` block that
    specifies the location of the library file copies. For example:

        repositories {
            flatDir {
                dirs 'libs'
            }
        }
    
    The location of this change is shown in the [Project Structure Diagram].

This completes the required changes to the build configuration. Build the
application to confirm that no mistakes have been made. After that, continue
with the next step, which is [Service Implementation].

**If you haven't installed your application via Workspace ONE** at least once,
then the application under development won't work when installed via the Android
Debug Bridge (adb). Instructions for installing via Workspace ONE can be found
in the [Integration Guides] document set, in the Integration Preparation guide.

### Service Implementation
The Workspace ONE Client SDK runtime receives various essential notifications
from the management console. An implementation of a specific Android broadcast
receiver and service must be added to your application to support this.

Proceed as follows.

1.  Implement an AirWatch SDK Service class.

    -   Add a new class to your application.
    -   Declare the new class as a subclass of the
        `AirWatchSDKBaseIntentService` class.
    -   Add dummy implementations of the required methods.
    
    In Java, the class could look like this:

        public class AirWatchSDKIntentService extends AirWatchSDKBaseIntentService {
            @Override
            protected void onApplicationConfigurationChange(
                Bundle applicationConfiguration) { }

            @Override
            protected void onApplicationProfileReceived(
                Context context,
                String profileId,
                ApplicationProfile awAppProfile) { }

            @Override
            protected void onClearAppDataCommandReceived(
                Context context,
                ClearReasonCode reasonCode) { }

            @Override
            protected void onAnchorAppStatusReceived(
                Context context,
                AnchorAppStatus appStatus) { }

            @Override
            protected void onAnchorAppUpgrade(Context context, boolean isUpgrade) { }
        }

    In Kotlin, the class could look like this:

        class AirWatchSDKIntentService: AirWatchSDKBaseIntentService() {
            override fun onApplicationConfigurationChange(applicationConfiguration: Bundle) { }

            override fun onApplicationProfileReceived(
                context: Context,
                profileId: String,
                appProfile: ApplicationProfile){ }

            override fun onClearAppDataCommandReceived(context: Context, reasonCode: ClearReasonCode)
            { }

            override fun onAnchorAppStatusReceived(context: Context, appStatus: AnchorAppStatus) { }

            override fun onAnchorAppUpgrade(context: Context, isUpgrade: Boolean) { }
        }

2.  Declare the permission.

    In the Android manifest file, inside the `manifest` block but outside the
    `application` block, add a declaration like the following.

        <?xml version="1.0" encoding="utf-8"?>
        <manifest ...>

        <!-- Next line is added -->
        <uses-permission android:name="com.airwatch.sdk.BROADCAST" />

        <application ...>
        ...

3.  Declare the receiver and service.

    In the Android manifest file, inside the `application` block, add
    `receiver` and `service` declarations like the following.

        <application>

            ...

            <receiver
                android:name="com.airwatch.sdk.AirWatchSDKBroadcastReceiver"
                android:permission="com.airwatch.sdk.BROADCAST" >
                <intent-filter>
                    <!-- 
                    Next line uses the initial dot notation as a shorthand for the package name.
                    -->
                    <action android:name=".airwatchsdk.BROADCAST" />
                </intent-filter>
                <intent-filter>
                    <action
                        android:name="com.airwatch.intent.action.APPLICATION_CONFIGURATION_CHANGED"
                        />

                    <!--
                    In the host attribute, replace com.your.package with the package name of your
                    application.
                    -->
                    <data android:scheme="app" android:host="com.your.package" />

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

            <service android:name=".AirWatchSDKIntentService"/>

        </application>

This completes the required service implementation. Build the application to
confirm that no mistakes have been made.

**If you haven't installed your application via Workspace ONE** at least once,
then the application under development won't work when installed via the Android
Debug Bridge (adb). Instructions for installing via Workspace ONE can be found
in the [Integration Guides] document set, in the Integration Preparation guide.

### Next Steps
After completing the above, continue with the next task, which could be either
of the following.

-   [Initialize the Client SDK], if your application will use only Client-level
    integration.
-   [Add the Framework], otherwise.

# Task: Initialize Client SDK [Initialize the Client SDK]
Client SDK initialization is a Workspace ONE platform integration task for
Android application developers. It applies only to Client-level integration, not
to Framework integration.

The Client SDK initialization task is dependent on the [Add the Client SDK]
task. The following instructions assume that the dependent task is complete
already.

## SDKManager
The main class of the Client SDK is SDKManager. It must be initialized before
use. Initialize it by calling the `init` class method. The call must be on a
background thread. An Android Context object is required, which could be an
Activity instance for example.

In Java, code for an Activity that initializes the SDKManager could look like
this:

    public class MainActivity extends Activity {
        SDKManager sdkManager = null;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ...
            startSDK();
        }

        private void startSDK() { new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SDKManager initSDKManager = SDKManager.init(MainActivity.this);
                    sdkManager = initSDKManager;
                    toastHere(
                        "Workspace ONE console version:" + initSDKManager.getConsoleVersion());
                }
                catch (Exception exception) {
                    sdkManager = null;
                    toastHere(
                        "Workspace ONE failed " + exception + ".");
                }
            }
        }).start(); }

        private void toastHere(final String message) {runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });}

        ...
    }

In Kotlin, code for an Activity that initializes the SDKManager could look like
this:

    class MainActivity : Activity() {

        private var sdkManager: SDKManager? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            ...
            startSDK()
        }

        private fun startSDK() { thread {
            try {
                val initSDKManager = SDKManager.init(this)
                sdkManager = initSDKManager
                toastHere("Workspace ONE console version:${initSDKManager.consoleVersion}")
            }
            catch (exception: Exception) {
                sdkManager = null
                toastHere("Workspace ONE failed $exception.")
            }
        } }

        private fun toastHere(message: String) { runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } }

        ...
    }

Calling the `init` method completes SDK Manager initialization. Build and run
the application to verify that no mistakes have been made.

## Next Steps [NextStepsClientSDKInitialization]
After the SDKManager instance has been received from the init call, its other
methods can be called. Check the reference documentation for details of the
programming interface.

This completes Client-level integration.

# Task: Add Framework [Add the Framework]
Adding the Framework is a Workspace ONE platform integration task for Android
application developers. Adding the Framework is necessary if the application
will make use of platform features such as authentication, single sign-on, data
encryption, or networking.

This task is dependent on the [Add the Client SDK] task. The following
instructions assume that the dependent task is complete already.

## Build Configuration and Files [BuildConfigurationAndFilesFrameworkIntegration]
This task involves changing your application project's build configuration and
files. These instructions assume that your application has a typical project
structure, same as the Add Client SDK task, as shown in the
[Project Structure Diagram].

A number of libraries will be added to the project. These can be divided into
the following categories.

-   Workspace ONE libraries that are part of the SDK.
-   Third party libraries that are distributed with the SDK.
-   Third party libraries that are hosted remotely, for example in a Maven
    repository, and included via Gradle.

Proceed as follows.
<p class="allow-page-break" />
1.  Add the required libraries to the build.

    In the application build.gradle file, in the `dependencies` block, add
    references to the required libraries. For example:
    <p class="allow-page-break" />

        dependencies {
            def room_version = "2.2.4"

            implementation fileTree(dir: 'libs', include: ['*.jar'])
            implementation ...

            // Following lines are added to integrate Workspace ONE at the Framework level ...

            // Workspace ONE libraries that are part of the SDK.
            implementation(name:'ws1-sdk-oauth-api-lib-1.1.0', ext:'aar')
            implementation(name:'SCEPClient-1.0.13', ext: 'aar')
            implementation(name:'AWComplianceLibrary-2.3.5', ext: 'aar')
            implementation(name:'AWFramework-20.10', ext: 'aar')
            implementation(name:"AirWatchSDK-20.10", ext: "aar") 
            implementation(name:'VisionUx-1.5.0.a', ext: 'aar')
            implementation(name:'CredentialsExt-102.1.0', ext: 'aar')
            implementation(name:"chameleon-android-1.0.16-ndk-r21c", ext:'aar')
            implementation(name:"settings-1.0.17-ndk-r21c", ext:'aar')
            implementation(name:"opdata-android-1.3.2", ext:'aar')
            implementation(name:"attributesprovider-1.0.17-ndk-r21c", ext:'aar')
            implementation(name:"ws1-android-logger-1.1.0", ext:'aar')
            implementation(name:"encryptedpreferencesprovider-1.0.12", ext:'aar')
            implementation(name:"httpprovider-1.0.11", ext:'aar')
            implementation(name:"memoryprovider-1.0.11", ext:'aar')
            implementation(name:"supercollider-1.0.7-ndk-r21c", ext:'aar')
            // The following JAR file is included in the SDK but needn't be added
            // as a specific dependency because it is covered by the
            // `implementation fileTree( ... include: ['*.jar'])`, above, after
            // copying.
            // awannotations-1.0.jar

            // Third party libraries that are distributed with the SDK.
            implementation 'com.squareup.moshi:moshi:1.8.0'
            implementation 'com.squareup.moshi:moshi-adapters:1.8.0'
            implementation 'com.squareup.okio:okio:1.17.2'
            implementation 'com.google.zxing:core:3.3.3'
            implementation 'com.google.code.gson:gson:2.4'

            // Third party libraries that are hosted remotely.
            implementation 'androidx.security:security-crypto:1.0.0-rc02'
            implementation "androidx.lifecycle:lifecycle-runtime:2.2.0"
            implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
            kapt "androidx.lifecycle:lifecycle-compiler:2.2.0"
            implementation 'com.google.android.gms:play-services-safetynet:17.0.0'
            implementation 'androidx.legacy:legacy-support-v13:1.0.0'
            implementation 'androidx.appcompat:appcompat:1.1.0'
            implementation 'androidx.cardview:cardview:1.0.0'
            implementation 'androidx.recyclerview:recyclerview:1.1.0'
            implementation 'com.google.android.material:material:1.1.0'
            implementation 'androidx.appcompat:appcompat:1.1.0'
            implementation('androidx.legacy:legacy-preference-v14:1.0.0') {
                exclude group: 'androidx.legacy', module: 'legacy-support-v4'
                exclude group: 'androidx.appcompat', module: 'appcompat'
                exclude group: 'androidx.annotation', module: 'annotation'
                exclude group: 'androidx.recyclerview', module: 'recyclerview'
            }
            implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
            implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.2.71'
            implementation 'org.jetbrains.kotlin:kotlin-reflect:1.2.71'
            implementation 'org.koin:koin-core:2.1.0'
            implementation 'org.koin:koin-android:2.1.0'
            implementation 'net.zetetic:android-database-sqlcipher:4.4.0@aar'
            implementation 'androidx.work:work-runtime-ktx:2.3.3'
            implementation 'androidx.biometric:biometric:1.0.1'
            implementation "androidx.room:room-ktx:$room_version"
            kapt "androidx.room:room-compiler:$room_version"
        }
    
    Your application might already require different versions of some of the
    same libraries required by the SDK. Warning messages will be generated in
    the build output in that case, for example stating that there are
    incompatible JAR files in the classpath.
    
    You can resolve this by selecting one or other version, either the SDK
    requirement or your app's original requirement.  
    In principle, the SDK isn't supported with versions other than those given
    in the above. In practice however, problems are unlikely to be encountered
    with later versions.

2.  Copy the required library files.

    The code snippet in the previous step indicates which libraries are:

    -   Workspace ONE libraries that are part of the SDK.
    -   Third party libraries that are distributed with the SDK.
    -   Third party libraries that are hosted remotely.

    The files that must be copied are those that are part of the SDK, or are
    distributed with it.

    All these files can be found in the SDK distribution, under one or other of
    the `Libs` sub-directories or their `dependencies` sub-directories. See the
    [Software Development Kit Download Structure Diagram].
    
    Copy the files into your project. Put the copies under the application
    sub-directory, in the `libs` sub-directory. The location is shown in the
    [Project Structure Diagram].

    If you prefer, you can instead take some of the third party libraries from
    their default online locations. The versions distributed with the SDK aren't
    customized. They are included for convenience and completeness only.

3.  Add annotation processor support.

    In the application build.gradle file, add the `kotlin-kapt` plugin. The
    plugin can be added in the plugins block at the start of the file, for
    example as shown in the following snippet.

        plugins {
            id 'com.android.application'
            id 'kotlin-android'
            id 'kotlin-android-extensions'

            // Following line adds the required plugin.
            id 'kotlin-kapt'
        }
        ...

4.  Add the required packaging and compile options.

    Still in the application build.gradle file, in the `android` block, add the
    Java version compatibility declarations shown in the following snippet.

        ...
        android {
            compileSdkVersion 28

            // Following blocks are added.
            compileOptions {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
            }
            kotlinOptions {
                jvmTarget = "1.8"
            }
            packagingOptions {
                pickFirst '**/*.so'
            }
            // End of added blocks.

            defaultConfig {
                ...
            }
            buildTypes {
                ...
            }
        }
        ...

    The above assumes that support for earlier Android operating system versions
    and processor architectures isn't required in the application. If support is
    required, also follow the instructions under [Early Version Support].

This completes the required changes to the build configuration. Build the
application to confirm that no mistakes have been made. After that, continue
with the next task, which is to [Initialize the Framework].

## Early Version Support
The Workspace ONE SDK can be integrated with early versions of Android, by
following some additional steps. Early versions here means before 5.0 Android,
which is API level 21. If your application won't support devices running early
Android versions, skip the instructions in this section.

To support early versions, change the build configuration to:

-   Enable Multidex explicitly.
-   Use a support library for vector drawables.

To make the changes, proceed as follows.

1.  Configure the build.

    In the application build.gradle file, in the `android` block, within the
    `defaultConfig` block, add the required settings, for example:

        ...
        android {
            ...
            defaultConfig {
                ...
                multiDexEnabled true
                vectorDrawables.useSupportLibrary = true
                ...
            }
            ...
        }
        ...

2.  Add the required libraries, if not already present.

    A library is required for Multidex support. It can be included in the
    application build.gradle file, in the `dependencies` block, for example as
    follows:

        dependencies {
            ...
            implementation 'com.android.support:multidex:1.0.3'
            ...
        }

    **Note**: At time of writing, the Multidex library is a dependency of the
    Workspace ONE Framework anyway, regardless of the multiDexEnabled setting.
    This means that the above change is included in the Add Framework task,
    above, and won't need to be made in order to add early Android version
    support.

For background, see these pages on the Android developer website.

-   Multidex:  
    [https://developer.android.com/...multidex#mdex-on-l](https://developer.android.com/studio/build/multidex#mdex-on-l)
-   Vector Drawables:  
    [https://developer.android.com/...vector-drawable-resources#vector-drawables-backward-solution](https://developer.android.com/guide/topics/graphics/vector-drawable-resources#vector-drawables-backward-solution)

(Some PDF viewers incorrectly escape the hash anchor marker in the above links.
If that happens, edit the link in the browser address bar.)

This concludes the required changes to support early Android versions. Build the
application to confirm that no mistakes have been made. Then continue with the
next task, [Initialize the Framework].

# Task: Initialize Framework [Initialize the Framework]
Framework initialization is a Workspace ONE platform integration task for
Android application developers. It applies to Framework-level integration, not
to Client-level integration.

The Framework initialization task is dependent on the [Add the Framework] task.
The following instructions assume that the dependent task is complete already.

## Select initialization class
Framework initialization can start from either an Android Application subclass,
referred to as initialization by *delegation*, or from a Workspace ONE SDK
AWApplication subclass, referred to as initialization by *extension*.
Choose the better option for your application, as follows.

-   **If your application has an Android Application subclass**, then choose it
    as the Framework initialization class. Proceed to these instructions:  
    [Initialize by delegation from an Android application subclass].

-   **Otherwise, create a Workspace ONE SDK AWApplication subclass** and it will
    be the Framework initialization class. Proceed to these instructions:  
    [Create an initialization subclass by extension].

### Initialize by delegation from an Android Application subclass
Follow these instructions to initialize from an Android Application subclass.
This is an alternative to creating an AWApplication subclass. See
[Select initialization class] for a discussion of the alternatives.

Update your Android Application subclass as follows.

-   Declare that the class implements the AWSDKApplication interface.

-   Add an AWSDKApplicationDelegate instance as a property.

-   Move the code from the body of your onCreate method, if any, to an override
    of the AWSDKApplication onPostCreate method.

-   Override the AWSDKApplication getMainActivityIntent() method to return an
    Intent for the application's main Activity.

-   Override the following Android Application methods:

    -   onCreate
    -   getSystemService
    -   attachBaseContext

    The required overrides are shown in the code snippets below, in Kotlin and
    in Java.

-   Implement all the other AWSDKApplication methods by calling the same method
    in the AWSDKApplicationDelegate instance.

    Kotlin delegation-by can be used for the implementation. This is illustrated
    in the [Initialization by delegation in Kotlin] code snippet below.

### Initialization by delegation in Java
In Java, the class could look like this:

    public class Application extends android.app.Application implements AWSDKApplication {
        // SDK Delegate.
        private final AWSDKApplicationDelegate awDelegate = new AWSDKApplicationDelegate();
        @NotNull
        @Override
        public AWSDKApplication getDelegate() { return awDelegate; }

        // Android Application overrides for integration.
        @Override
        public void onCreate() {
            super.onCreate();
            this.onCreate(this);
        }

        @Override
        public Object getSystemService(String name) {
            return this.getAWSystemService(name, super.getSystemService(name));
        }

        @Override
        public void attachBaseContext(@NotNull Context base) {
            super.attachBaseContext(base);
            attachBaseContext(this);
        }

        // Application-specific overrides.
        @Override
        public void onPostCreate() {
            // Code from the application's original onCreate() would go here.
        }

        @NonNull
        @Override
        public Intent getMainActivityIntent() {
            // Replace MainActivity with application's original main activity.
            return new Intent(getApplicationContext(), MainActivity.class);
        }

        // Mechanistic AWSDKApplication abstract method overrides.

        // Methods that return a value could follow this as a template:
        @Nullable
        @Override
        public Object getAWSystemService(@NotNull String name, @Nullable Object systemService) {
            return awDelegate.getAWSystemService(name, systemService);
        }

        // Methods that return void could follow this as a template:
        @Override
        public void attachBaseContext(@NotNull android.app.Application application) {
            awDelegate.attachBaseContext(application);
        }

        // ... Many more overrides here.
    }

### Initialization by delegation in Kotlin
In Kotlin, the class could look like this:

    // This class uses Kotlin delegation to implement the AWSDKApplication
    // interface.  
    // A new AWSDKApplicationDelegate instance is allocated on the fly as the
    // delegate. For background on Kotlin delegation, see:
    // https://kotlinlang.org/docs/reference/delegation.html
    open class Application:
        android.app.Application(),
        AWSDKApplication by AWSDKApplicationDelegate()
    {
        // Android Application overrides for integration.
        override fun onCreate() {
            super.onCreate()
            onCreate(this)
        }

        override fun getSystemService(name: String): Any? {
            return getAWSystemService(name, super.getSystemService(name))
        }

        override fun attachBaseContext(base: Context?) {
            super.attachBaseContext(base)
            attachBaseContext(this)
        }

        // Application-specific overrides.
        override fun onPostCreate() {
            // Code from the application's original onCreate() would go here.
        }

        override fun getMainActivityIntent(): Intent {
            // Replace MainActivity with application's original main activity.
            return Intent(applicationContext, MainActivity::class.java)
        }
    }

### Next
This completes initialization from an Android Application subclass. Now continue
with the next step, which is to
[configure the initialization class in the manifest].

### Create an initialization subclass by extension
Follow these instructions to create a Framework initialization AWApplication
subclass. This is an alternative to initialising from an Android Application
subclass. See [Select initialization class] for a discussion of the
alternatives.

Add to your application code a new class that:

-   Is declared as an AWApplication subclass.
-   Overrides the getMainActivityIntent() method to return an Intent for the
    application's main Activity.
-   Implements the other required methods with dummies.

In Java, the class could look like this:

    // Note the fully qualified class name in the extends declaration.
    public class AWApplication extends com.airwatch.app.AWApplication {
        @NotNull
        @Override
        public Intent getMainActivityIntent() {
            return new Intent(getApplicationContext(), MainActivity.class);
        }

        @Override
        public void onSSLPinningRequestFailure(
                @NotNull String host, X509Certificate x509Certificate
        ) {
        }

        @Override
        public void onSSLPinningValidationFailure(
                @NotNull String host, X509Certificate x509Certificate
        ) {
        }
    }

In Kotlin, the class could look like this:

    // Note the fully qualified base class name.
    open class AWApplication: com.airwatch.app.AWApplication() {
        override fun getMainActivityIntent(): Intent {
            return Intent(applicationContext, MainActivity::class.java)
        }

        override fun onSSLPinningRequestFailure(
            host: String,
            serverCACert: X509Certificate?
        ) {
        }

        override fun onSSLPinningValidationFailure(
            host: String,
            serverCACert: X509Certificate?
        ) {
        }

    }

This completes the creation of an initialization subclass. Now continue with the
next step, which is to [configure the initialization class in the manifest].

## Configure the initialization class in the manifest [configure the initialization class in the manifest]
Follow these instructions to configure your selected initialization class in the
Android manifest. The initialization class will be either the existing Android
Application subclass, or a new AWApplication subclass that was just created. See
[Select initialization class] for a discussion of the alternatives.

Proceed as follows.

1.  Add the Android schema tools.

    The tools can be added at the top of the file, in the manifest tag, for
    example like this:

        <manifest
            xmlns:android="http://schemas.android.com/apk/res/android"
            package="com.example.integrationguide"
            xmlns:tools="http://schemas.android.com/tools"
            >

2.  Update the application declaration.

    The application declaration must be updated to:

    -   Declare an application class name, if it wasn't already declared.
    -   Replace the label.
    -   Override the allowBackup flag with the setting from the SDK manifest.

    These updates can be made in the application tag, for example like this:

        <application
            android:name=".YourApplicationOrAWApplicationSubClass"
            android:label="@string/app_name"
            ...
            tools:replace="android:label, android:allowBackup, android:networkSecurityConfig"
            >

3.  Set the launcher and main Activity to be from the Framework.

    If the application had a previous declaration for launcher and main
    Activity, remove it. Instead, declare the Framework SDKSplashActivity as
    launcher and main.

    New declarations could look like this, for example:

        <activity
            android:name=".MainActivity"
            >
            <!-- Original launcher and main declarations removed. -->
        </activity>
        <activity
            android:name="com.airwatch.login.ui.activity.SDKSplashActivity"
            android:label="@string/app_name"
            >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

4.  Update the AirWatch SDK service declaration.

    The AirWatch SDK service was declared and implemented in the 
    [Service Implementation] task. The declaration will be inside the
    application block, perhaps after the receiver block for the service. It must
    now be updated to have the bind-job permission, for example like this:

        <application>
            ...
            <receiver>
                ...
            </receiver>
            <service
                android:name=".AirWatchSDKIntentService"
                android:permission="android.permission.BIND_JOB_SERVICE"
                />
        </application>

This completes the initialization class configuration.

## Next Steps [NextStepsFrameworkInitialization]
Build and run the application to confirm that no mistakes have been made.

The Workspace ONE splash screen should be shown at launch, Other SDK screens
might also be shown depending on the configuration in the management console.
See the [User Interface Screen Capture Images] in the appendix to this document.

After completing the above, you can proceed to:

-   Networking integration.
-   Branding integration.
-   Integration of other framework features.

See the respective documents in the Workspace ONE Integration Guide for Android
set. An overview that includes links to all the guides in the set is available

-   in Markdown format, in the repository that also holds the sample code:  
    [https://github.com/vmware-samples/...IntegrationOverview.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/01Overview/WorkspaceONE_Android_IntegrationOverview.md)

-   in Portable Document Format (PDF), on the VMware website:  
    [https://code.vmware.com/...IntegrationOverview.pdf](https://code.vmware.com/docs/12354/WorkspaceONE_Android_IntegrationOverview.pdf)

# Appdendix: User Interface Screen Capture Images [User Interface Screen Capture Images]
The following images show screens that are part of the Workspace ONE SDK user
interface.

![**Screen capture 1:** Splash screen](ScreenCapture_SplashProgress.png)

![**Screen capture 2:** Login screen](ScreenCapture_LoginDefault.png)

The splash screen should be shown during every launch of an application that is
integrated to the Framework level. The login screen might be shown afterwards,
depending on the application state and the configuration in the management
console.

# Document Information
## Published Locations
This document is available

-   in Markdown format, in the repository that also holds the sample code:  
    [https://github.com/vmware-samples/...BaseIntegration.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/03BaseIntegration/WorkspaceONE_Android_BaseIntegration.md)

-   in Portable Document Format (PDF), on the VMware website:  
    [https://code.vmware.com/...BaseIntegration.pdf](https://code.vmware.com/docs/12356/WorkspaceONE_Android_BaseIntegration.pdf)

## Revision History
|Date     |Revision                                    |
|---------|--------------------------------------------|
|03jul2020|First publication, for 20.4 SDK for Android.|
|31jul2020|Update for 20.7 SDK for Android.            |
|30aug2020|Update for 20.8 SDK for Android.            |
|03sep2020|Post-release update.                        |
|01oct2020|Update for 20.9 SDK for Android.            |
|11oct2020|Post-release update.                        |
|03nov2020|Update for 20.10 SDK for Android.           |
|06nov2020|Post-release update.                        |

## Legal
-   **VMware, Inc.** 3401 Hillview Avenue Palo Alto CA 94304 USA
    Tel 877-486-9273 Fax 650-427-5001 www.vmware.com
-   Copyright © 2020 VMware, Inc. All rights reserved.
-   This content is protected by U.S. and international copyright and
    intellectual property laws. VMware products are covered by one
    or more patents listed at
    [https://www.vmware.com/go/patents](https://www.vmware.com/go/patents).
    VMware is a registered trademark or trademark of VMware, Inc. and its
    subsidiaries in the United States and other jurisdictions. All other marks
    and names mentioned herein may be trademarks of their respective companies.
-   The Workspace ONE Software Development Kit integration samples are
    licensed under a two-clause BSD license.  
    SPDX-License-Identifier: BSD-2-Clause