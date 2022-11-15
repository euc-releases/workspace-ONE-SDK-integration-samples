# Build Instructions
The code project in the Workspace ONE Integration Guide for Android repository
can be built by following these instructions. For an introduction to the
repository, see the [parent directory](..) readme file.

# Directories
The top-level directories in the repository are as follows.

-   [Apps/](../Apps) has the demonstration applications' code, in an Android
    Studio project. Sub-directories hold complete applications written either in
    Java or in Kotlin. For a complete list of applications with links, see the
    [applications](applications.md) file.

-   [Secret/](../Secret) is set aside for your signing keystore and
    configuration.

-   `Download` was set aside for manual installation of the software development
    kit (SDK) but is no longer used.

# Instructions
Follow these instructions to build the applications in this project.

1.  **Create an Android application signing keystore and configuration.**

    See the
    [Workspace ONE for Android Integration Preparation guide appendices](../Guides/02Preparation/WorkspaceONE_Android_IntegrationPreparation.md#appendix-how-to-generate-a-signed-android-package-file-once-how-to-generate-a-signed-android-package-file-once)
    in case you need instructions. Use the following locations:

    -   Keystore file:  
        `/wherever/you/pulled/this/repository/IntegrationGuideForAndroid/Secret/keystore`
    
    -   Signing configuration:  
        `/wherever/you/pulled/this/repository/IntegrationGuideForAndroid/Secret/keystore.gradle`
    
    See the [Secret](../Secret) readme file for a template and notes.

    The build configuration of this repository is set up to use the files you
    create at the above locations.

2.  **Ensure you have access to the SDK Maven repository.**

    The repository server is here.  
    `https://vmwaresaas.jfrog.io/artifactory/Workspace-ONE-Android-SDK/`

3.  **Load the project into Android Studio.**

    Proceed as follows.

    1.  Open Android Studio.
    2.  Close any open projects, to avoid accidents.
    3.  Open as an existing project this location:

            `/wherever/you/pulled/this/repository/IntegrationGuideForAndroid/Apps/`
        
        Note: Don't select any file under that directory.
    
    4.  Android Studio prompts you to Gradle Sync because it is unable to get
        Gradle wrapper properties. Select OK and the project will synchronise
        and configure its build. It might take a minute or two first time.

    The applications in the repository should now appear in the Android Studio
    project navigator. There are 26 at time of writing. The naming convention is
    as follows, in alphabetic order.

    -   `base*`  
        Applications with no SDK integration, used as a base for other
        applications.
    -   `brand*`  
        Demonstration applications for the Branding Integration Guide.
    -   `client*`  
        Applications for the Base Integration Guide that demonstrate Client
        level integration.
    -   `dlpExtendKotlin`  
        Kotlin Application that demonstrates Workspace ONE Data Loss Prevention features.
    -   `framework*`  
        Applications for the Base Integration Guide that demonstrate Framework
        level integration, either by delegation or by extension.
    -   `identificationDemo`  
        Application that demonstrates device identification and custom settings
        features of Workspace ONE.
    -   `privacy*`  
        Demonstration application for the Privacy Agreement Module Integration
        Guide.

     The [applications](applications.md) file has a complete list of the
     applications.

4.  **Test that the applications can be built.**

    After the synchronisation has finished, execute the Gradle task:  
    Integration Guide, Tasks, build, assemble

    Gradle tasks can be accessed from the Gradle assistant tab sidebar, which by
    default is at the top right of the Android Studio window.

    If the build doesn't run OK then check the first two steps, above, have been
    completed.

You are now ready to install or debug any of the demonstration applications on
your developer device.

# Unused Directory
The [Download/](../Download) directory is now unused but was originally set
aside for manual download and installation of the Workspace ONE mobile SDK. The
code project in the Workspace ONE Integration Guide for Android repository was
configured to use the SDK from that directory but is now configured to build
with the Maven distribution of the SDK instead.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause