# Updating
The code project in the Workspace ONE Integration Guide for Android repository
can be updated for a new release of the software development kit by following
these instructions. For an introduction to the repository, see the
[parent directory](..) readme file.

# Prerequisites
These instructions assume you have already built the code project at least once.
If you haven't, follow the build instructions in the [build.md](build.md) file
first.

# Instructions
Follow these instructions to update the applications in this project to a new
version of the Workspace ONE Software Development Kit (SDK) for Android.

1.  **Build and run the base applications.**

    The base applications don't integrate the SDK so updating to a new version
    shouldn't make any difference. However, this is a good time to update to the
    latest versions of native code such as new Kotlin language plugins and
    AndroidX libraries.

    First, check for changes highlighted in the project build.gradle file.
    
    There are two base applications, `baseJava` and `baseKotlin`. Check for
    changes highlighted in the build.gradle file of each. Run at least one on a
    developer device as a test before proceeding.

    If there any native changes to the dependencies, update the
    `buildBase.gradle` files accordingly.
    
    -   There are many copies of the file in the project, all of which should be
        identical when the update is finished.
    
    -   Start by updating one of the `buildBase.gradle` files in either of the
        base app modules, then build and run it as a test.

    -   When the first copy has been updated, use the samers.py script to apply
        the updates to all the other copies. See the [samers.md](samers.md) file
        for instructions.
    
    See the [applications.md](applications.md) file for a description of the app
    user interface.

2.  **Update the Workspace ONE Intelligent Hub app.**

    Now is a good time to update to the latest version of Hub on your developer
    device. You can update using the Play Store, for example.

3.  **Install the new SDK for Android under the Download/ directory and select
    it.**
    
    See the [Workspace ONE for Android Integration Preparation guide](../Guides/02Preparation/WorkspaceONE_Android_IntegrationPreparation.md) for how to obtain the SDK
    for Android.

    See the [Download](../Download) readme file for details of how to
    **install** and **select** the version.

    The build configuration of this repository is set up to work with the SDK
    you install there.

4.  **Update, build and run the client applications.**

    There are two client applications: `clientJava` and `clientKotlin`. They
    integrate the SDK at the client level.

    If the new SDK introduces any changes to the dependencies for client level
    integration, update the `integrateClient.gradle` files accordingly.
    
    -   There are many copies of the file in the project, all of which should be
        identical when the update is finished.
    
    -   Start by updating one of the `integrateClient.gradle` files in either of
        the client app modules, then build and run it as a test.

    -   When the first copy has been updated, use the samers.py script to apply
        the updates to all the other copies. See the [samers.md](samers.md) file
        for instructions.
    
    If the new SDK doesn't introduce any changes to the dependencies for client
    level integration, there will be no need to update the
    `integrateClient.gradle` file.
    
    Code changes might also be needed for integration with the new SDK. For
    example, new methods might be needed in the AirWatchSDKBaseIntentService
    implementation. Use the same approach as for the build configuration change,
    make the change in one app module and then apply to the others using the
    samers.py script.
    
    Build and run at least one of the client apps as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

5.  **Update, build and run the framework applications.**

    There are several framework applications. Their names all start with
    `framework`. They integrate the SDK at the framework level, either by
    delegation or by extension.

    If the new SDK introduces any changes to the dependencies for framework
    level integration, update the `integrateFramework.gradle` files accordingly.

    -   There are many copies of the file in the project, all of which should be
        identical when the update is finished.
    
    -   Start by updating one of the `integrateFramework.gradle` files in one of
        the framework app modules, then build and run it as a test.

    -   When the first copy has been updated, use the samers.py script to apply
        the updates to all the other copies. See the [samers.md](samers.md) file
        for instructions.
    
    If the new SDK doesn't introduce any changes to the dependencies for
    framework level integration, there will be no need to update the
    `integrateFramework.gradle` file.

    Code changes might also be needed for integration with the new SDK. For
    example, new methods might be needed in the Application or AWApplication
    subclasses. Use the same approach as for the build configuration change,
    make the change in one app module and then apply to the others using the
    samers.py script.
    
    Build and run at least one of the framework apps as a test before
    proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

6.  **Update, build and run the branding applications.**

    There are a number of branding demonstration applications. Their names all
    start with `brand`. They integrate the SDK at the framework level, and
    demonstrate different branding features of Workspace ONE for Android.

    Branding integration has the same dependencies as framework integration, so
    there won't be a need to update anything like an integrateBrand.gradle file.

    Any updates required for framework integration of the new SDK will have been
    applied already in a previous step, for example by running the samers.py
    script.

    Code changes specifically for branding might be needed. Do any updates in
    the same way as updates to the client and framework application code, by
    making the change in one app and then applying it with the samers.py script.

    Build and run at least one of the branding apps as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

7.  **Update, build and run the privacy agreement applications.**

    There is one privacy agreement application: `privacyKotlin`. It integrates
    the Workspace ONE Privacy Agreement Module, not the other parts of the
    Workspace ONE SDK.

    If the new SDK introduces any changes to the dependencies for privacy
    agreement integration, update the `integratePrivacy.gradle` file
    accordingly.

    Build and run the app as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

8.  **Update, build and run the Data Loss Prevention application.**

    There is one data loss prevention application: `dlpExtendKotlin`.
    It integrates the SDK at the framework level, and demonstrates a few data loss 
    prevention features of Workspace ONE for Android.

    This application uses Maven to handle it's WS1 dependencies so there is 
    no need to update any integration build files in order to run.

    Build and run the app as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface and how to configure Data Loss Prevention features in the 
    management console.

9.  **Update, build and run the additional sample applications.**

    There is one additional sample application: `identificationDemo`. It
    integrates the SDK at the client level.

    This app has a standalone build configuration and doesn't have a copy of the
    `integrateClient.gradle` file. Make the same changes to this app's build
    configuration and code that you made to the `clientKotlin` app.

    Build and run the app as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface and how to configure the management console.

10.  **Run the automated tests.**

    The sample applications each have a copy of the same automated test suite.
    See for example the
    [BaseActivityTest.kt](../Apps/frameworkDelegateKotlin/src/androidTest/java/com/example/integrationguide/BaseActivityTest.kt)
    file. See also in the build Gradle files the `testNoAppUninstall` task.

    The suite is quite basic, testing only that the app launches and that its
    user interface is interactive. This is enough to verify that each app builds
    and doesn't crash during SDK initialization.

    You can run the test suite in all the apps one after another with the
    [gradleach.py](../Apps/gradleach.py) Python script. For example, as follows.

        cd /wherever/you/pulled/this/repository/IntegrationGuideForAndroid/Apps

        # Print the usage as a reminder first.
        python3 ./gradleach.py -h

        # Go for real.
        python3 ./gradleach.py

    If any test fails, the script will terminate early with an error message
    from Gradle. Fix any issues before running the tests again.

When the test passes in all the apps, the update is ready for commit.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause
