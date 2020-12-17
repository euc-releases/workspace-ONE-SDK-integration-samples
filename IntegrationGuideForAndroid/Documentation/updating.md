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

1.  **Install the new SDK for Android under the Download/ directory and select
    it.**
    
    See the [Workspace ONE for Android Integration Preparation guide](../Guides/02Preparation/WorkspaceONE_Android_IntegrationPreparation.md) for how to obtain the SDK
    for Android.

    See the [Download](../Download) readme file for details of how to
    **install** and **select** the version.

    The build configuration of this repository is set up to work with the SDK
    you install there.

2.  **Build and run the base applications.**

    The base applications don't integrate the SDK so updating to a new version
    shouldn't make any difference. However, this is a good time to update to the
    latest versions of native code such as new Kotlin language plugins and
    AndroidX libraries.
    
    There are two base applications, `baseJava` and `baseKotlin`. Check for
    changes highlighted in the build.gradle file of each. Run both on a
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

3.  **Update, build and run the client applications.**

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
    
    Build and run both of the client apps as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

4.  **Update, build and run the framework applications.**

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
    
    Build and run each of the framework apps as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

5.  **Update, build and run the branding applications.**

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

    Build and run each of the branding apps as a test before proceeding.

    See the [applications.md](applications.md) file for a description of the app
    user interface.

The update is now ready for commit.

# License
Copyright 2020 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause