## Workspace ONE SDK 24.11 for Android

# What's new
- Branding Update: SDK now features a new logo and splash screens as part of our transition to Omnissa.
- Enhancements in Multi part log improvements.
- Disabled Shift Based Access feature.

# Compatibility
- Android 7.0+ (i.e., API 24)
- Workspace ONE UEM Console 2302+
- Android Studio with the Gradle Android Build System (Gradle) 7.4+
- JDK version 17
- Workspace ONE Intelligent Hub for Android version 24.10


## Workspace ONE SDK 24.10 for Android

# What's new
- Support for POST requests for sending SCEP (Simple Certificate Enrollment Protocol) requests
- Decommissioning of MAG and Standard Proxy
- Stability improvements and Bug fixes.

# Compatibility

- Android 7.0+ (i.e., API 24)
- Workspace ONE UEM Console 2302+
- Android Studio with the Gradle Android Build System (Gradle) 7.4+
- JDK version 17
- Workspace ONE Intelligent Hub for Android version 24.07

## Omnissa Workspace ONE SDK 24.07 for Android

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
The SDK is accessible from a Maven repository. For integration documentation, refer to the Omnissa Developer website: [Omnissa Developer Documentation](https://developer.omnissa.com/ws1-sdk-uem-android/) and KB article: [https://kb.omnissa.com/s/article/6000158](https://kb.omnissa.com/s/article/6000158)

## Downloads

Omnissa provides this Software Development Kit (the “Software”) to you subject to the following terms and conditions. By downloading, installing, or using the Software, you agree to be bound by the terms of [SDK License Agreement](https://static.omnissa.com/sites/default/files/omnissa-sdk-agreement.pdf). If you disagree with any of the terms, then do not use the Software.

For additional information, please visit the [Omnissa Legal Center](https://www.omnissa.com/legal-center/).

## License

This software is licensed under the [Omnissa Software Development Kit (SDK) License Agreement](https://static.omnissa.com/sites/default/files/omnissa-sdk-agreement.pdf); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

## Omnissa Workspace ONE SDK 24.06.1 for Android

# What's new
* Migrated play core library to target Android API 34

## Omnissa Workspace ONE SDK 24.06 for Android

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
The SDK is accessible from a Maven repository. For integration documentation, refer to the Omnissa Developer website: [Omnissa Developer Documentation](https://developer.omnissa.com/ws1-sdk-uem-android/)

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

## Omnissa Workspace ONE SDK 24.04 for Android

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

The SDK is available from a Maven repository. Check the integration documentation on the Omnissa Developer website. https://developer.omnissa.com/ws1-sdk-uem-android

Starting June 2024, version 24.06 onwards Workspace ONE SDK for Android will NOT be distributed through the My Workspace one portal i.e. https://my.workspaceone.com/products/Workspace-ONE-SDK 
It will only be distributed as a maven package; developers need to follow the instructions provided in the below-mentioned link to integrate the Workspace ONE SDK Android package into their applications.
