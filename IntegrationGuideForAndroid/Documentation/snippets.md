# Code Snippets
Code snippets from the Workspace ONE Integration Guide for Android document set
can be viewed here. For an introduction to the repository, see the
[parent directory](..) readme file.

The following code snippets are in the integration guide for the software
development kit (SDK).

# Base Integration guide.
-   Gradle code to add the required libraries for Client level integration:
    [integrateClient.gradle](../Apps/clientKotlin/integrateClient.gradle)

-   AirWatch SDK Service implementation.

    -   In Java:
        [AirWatchSDKIntentService.java](../Apps/clientJava/src/main/java/com/example/integrationguide/AirWatchSDKIntentService.java)

    -   In Kotlin:
        [AirWatchSDKIntentService.kt](../Apps/clientKotlin/src/main/java/com/example/integrationguide/AirWatchSDKIntentService.kt)

-   Android Manifest including AirWatch SDK Service declaration:
    [AndroidManifest.xml](../Apps/clientKotlin/src/main/AndroidManifest.xml)

-   Client level initialisation code.

    -   In Java:
        [MainActivity.java](../Apps/clientJava/src/main/java/com/example/integrationguide/MainActivity.java)

    -   In Kotlin:
        [MainActivity.kt](../Apps/clientKotlin/src/main/java/com/example/integrationguide/MainActivity.kt)

-   Gradle code to add the required libraries and settings for Framework level
    integration:
    [integrateFramework.gradle](../Apps/frameworkDelegateJava/integrateFramework.gradle)

-   Android Application subclass for Framework initialisation by delegation.

    -   in Java:
        [Application.java](../Apps/frameworkDelegateJava/src/main/java/com/example/integrationguide/Application.java)

    -   In Kotlin:
        [Application.kt](../Apps/frameworkDelegateKotlin/src/main/java/com/example/integrationguide/Application.kt)

-   AirWatch AWApplication subclass for Framework initialisation by extension.

    -   In Java:
        [AWApplication.java](../Apps/frameworkExtendJava/src/main/java/com/example/integrationguide/AWApplication.java)

    -   In Kotlin:
        [AWApplication.kt](../Apps/frameworkExtendKotlin/src/main/java/com/example/integrationguide/AWApplication.kt)

-   Android Manifest with modifications for Framework level integration:
    [AndroidManifest.xml](../Apps/frameworkExtendKotlin/src/main/AndroidManifest.xml)

# Branding Integration guide
-   Static application branding:
    [styles.xml](../Apps/brandStaticDelegateJava/src/main/res/values/styles.xml)

-   Enterprise branding:

    -   Android Application subclass that supports enterprise branding. This class
        is a subclass of the Application subclass for Framework initialisation by
        delegation.

        -   In Java:
            [EnterpriseBrandApplication.java](../Apps/brandEnterpriseOnlyDelegateJava/src/main/java/com/example/integrationguide/EnterpriseBrandApplication.java)

        -   In Kotlin:
            [EnterpriseBrandApplication.kt](../Apps/brandEnterpriseOnlyDelegateKotlin/src/main/java/com/example/integrationguide/EnterpriseBrandApplication.kt)

    -   AirWatch AWApplication subclass that supports enterprise branding. This
        class is a subclass of the AWApplication subclass for Framework
        initialisation by extension.

        -   In Java:
            [EnterpriseBrandAWApplication.java](../Apps/brandEnterpriseOnlyExtendJava/src/main/java/com/example/integrationguide/EnterpriseBrandAWApplication.java)
        
        -   In Kotlin:
            [EnterpriseBrandAWApplication.kt](../Apps/brandEnterpriseOnlyExtendKotlin/src/main/java/com/example/integrationguide/EnterpriseBrandAWApplication.kt)

-   Dynamic branding:

    -   Branding manager implementations:

        -   Dummy implementation in Java:
            [BrandingManager.java](../Apps/brandDynamicDelegateJava/src/main/java/com/example/integrationguide/BrandingManager.java)

        -   Implementation that generates bitmaps at run time, in Java:
            [BitmapBrandingManager.java](../Apps/brandDynamicDelegateJava/src/main/java/com/example/integrationguide/BitmapBrandingManager.java)

        -   Dummy implementation in Kotlin:
            [BrandingManager.kt](../Apps/brandDynamicDelegateKotlin/src/main/java/com/example/integrationguide/BrandingManager.kt)

        -   Implementation that generates bitmaps at run time, in Kotlin:
            [BitmapBrandingManager.kt](../Apps/brandDynamicDelegateKotlin/src/main/java/com/example/integrationguide/BitmapBrandingManager.kt)

    -   Branding manager registration:

        -   Android Application subclass in Java:
            [DynamicBrandApplication.java](../Apps/brandDynamicDelegateJava/src/main/java/com/example/integrationguide/DynamicBrandApplication.java)

        -   Android Application subclass in Kotlin:
            [DynamicBrandApplication.kt](../Apps/brandDynamicDelegateKotlin/src/main/java/com/example/integrationguide/DynamicBrandApplication.kt)

        -   AirWatch AWApplication subclass in Java:
            [DynamicBrandAWApplication.java](../Apps/brandDynamicExtendJava/src/main/java/com/example/integrationguide/DynamicBrandAWApplication.java)

        -   AirWatch AWApplication subclass in Kotlin:
            [DynamicBrandAWApplication.kt](../Apps/brandDynamicExtendKotlin/src/main/java/com/example/integrationguide/DynamicBrandAWApplication.kt)

    -   Enterprise resources in the application user interface:

        -   Activity in Java:
            [MainActivity.java](../Apps/brandDynamicDelegateJava/src/main/java/com/example/integrationguide/MainActivity.java)

        -   Activity in Kotlin:
            [MainActivity.kt](../Apps/brandDynamicDelegateKotlin/src/main/java/com/example/integrationguide/MainActivity.kt)

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause