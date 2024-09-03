## VMware Workspace ONE SDK 24.07 for Android

# What's new
* Stability improvements and Bug fixes.
* Third party library updates.

# Compatibility
* Android 7.0+ (i.e., API 24)
* Workspace ONE UEM Console 2302+
* Android Studio with the Gradle Android Build System (Gradle) 7.4+
* JDK version 17
* Workspace ONE Intelligent Hub for Android version 24.07

# Integration
The SDK is accessible from a Maven repository. For integration documentation, refer to the VMware developer website: [VMware Developer Documentation](https://developer.omnissa.com/ws1-sdk-uem-android/) and KB article: [https://kb.omnissa.com/s/article/6000158](https://kb.omnissa.com/s/article/6000158)

## License
Before you integrate the SDK you must
* Review the [VMware Workspace ONE Software Development Kit License Agreement](https://developer.vmware.com/docs/12215/VMwareWorkspaceONESDKLicenseAgreement.pdf). By downloading, installing, or using the VMware Workspace ONE SDK, you agree to these license terms.
* Review the [VMware Privacy Notice](https://www.broadcom.com/company/legal/privacy) and the [Workspace ONE UEM Privacy Disclosure](https://www.omnissa.com/trust-center/) for information on applicable privacy policies.


## VMware Workspace ONE SDK 24.06.1 for Android

# What's new
* Migrated play core library to target Android API 34

## VMware Workspace ONE SDK 24.06 for Android

# What’s new
* Stability improvements and Bug fixes.
* Third party library updates.

# Compatibility
* Android 7.0+ (i.e., API 24)
* Workspace ONE UEM Console 2212+ 
* Android Studio with the Gradle Android Build System (Gradle) 7.4+ 
* JDK version 17 
* Workspace ONE Intelligent Hub for Android version 24.04

# Integration
The SDK is accessible from a Maven repository. For integration documentation, refer to the VMware developer website: [VMware Developer Documentation](https://developer.omnissa.com/ws1-sdk-uem-android/)

# Important Update Starting June 2024
From version 24.06 onwards, the Workspace ONE SDK for Android will no longer be available through the My Workspace ONE portal: ("https://my.workspaceone.com/")
Instead, it will be distributed exclusively as a Maven GitHub package (“https://maven.pkg.github.com/euc-releases/Android-WorkspaceONE-SDK/“, “https://maven.pkg.github.com/euc-releases/ws1-intelligencesdk-sdk-android/” ).

Developers must follow the instructions in the following link to integrate the Workspace ONE SDK Android package into their applications: [Workspace ONE SDK Integration Guide](https://github.com/euc-releases/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/03BaseIntegration/WorkspaceONE_Android_BaseIntegration.md).

Also, when adding module dependencies, ensure the group and module names are in lowercase.
Example:
dependencies {
    implementation ("com.airwatch.android:airwatchsdk:${airwatchVersion}")
    implementation ("com.airwatch.android:awframework:${airwatchVersion}")
    implementation ("com.airwatch.android:awnetworklibrary:${airwatchVersion}")
}

## VMware Workspace ONE SDK 24.04 for Android

# What’s new

* Compatibility updates for targeting Android 14 (i.e., API 34)
* Support for Android 5 and 6 has been discontinued.
* Bug fixes and stability improvements.
* Third party library updates.

## Compatibility

* Android 7.0 + (i.e., API 24)
* Workspace ONE UEM Console 2212+
* Android Studio with the Gradle Android Build System (Gradle) 7.4 +
* JDK version 17
* Workspace ONE Intelligent Hub for Android version 24.01

## Integration

The SDK is available from a Maven repository. Check the integration documentation on the VMware developer website. https://developer.omnissa.com/ws1-sdk-uem-android

Starting June 2024, version 24.06 onwards Workspace ONE SDK for Android will NOT be distributed through the My Workspace one portal i.e. https://my.workspaceone.com/products/Workspace-ONE-SDK 
It will only be distributed as a maven package; developers need to follow the instructions provided in the below-mentioned link to integrate the Workspace ONE SDK Android package into their applications.

## License
Before you integrate the SDK you must
* Review the VMware Workspace ONE Software Development Kit License Agreement. By downloading, installing, or using the VMware Workspace ONE SDK, you agree to these license terms.
* Review the VMware Privacy Notice and the Workspace ONE UEM Privacy Disclosure for information on applicable privacy policies. 
