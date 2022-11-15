# Directory for the Software Development Kit
This directory is set aside for legacy installation of the Workspace ONE
Software Development Kit (SDK) for Android. The legacy installation is based on
manual download of the SDK.

# Warning
Manual download of the SDK isn't recommended. Instead, use the SDK distributed
through the public Maven repository. The build configuration of the Open Source
sample project now uses the Maven distribution only. See the instructions in the
[build](../Documentation/build.md) file.

# Instructions
Follow these instructions to use the manual download mechanism to install the
SDK.

1.  Download the SDK.

    See the SDK home page
    [https://developer.vmware.com/web/sdk/Native/airwatch-android](https://developer.vmware.com/web/sdk/Native/airwatch-android)
    for links to the download portal.

2.  Unzip the SDK download under this directory.

3.  Copy the root of the SDK to the following location:

    `/wherever/you/pulled/this/repository/IntegrationGuideForAndroid/Download/Android SDK v*major*.*minor*`

    Leave the sub-directories as they are in the download.

    For example, if you are using the 21.7 version, you will have a structure
    like this:

        IntegrationGuideForAndroid/Download/Android SDK v21.7/
        |
        +-- Documentation/...
        |
        +-- Libs/
        |   |
        |   +-- AWFramework/...
        |   |
        |   +-- AWNetworkLibrary/...
        |   |
        |   +-- AWPrivacy/...
        |   |
        |   +-- ClientSDK/...
        |
        +-- Sample Code/...

4.  Check the version of the privacy agreements module.

    The required version number appears as a suffix to the .aar file in the
    Libs/AWPrivacy/ sub-directory.
    
    For example, the path in the 21.7 SDK download is:  
    `IntegrationGuideForAndroid/Download/Android SDK v21.7/Libs/AWPrivacy/AWPrivacy-21.5.1.aar`  
    The privacy agreements module has 21.5.1 as its version number.

5.  Set the versions in the build configuration.

    The project build.gradle file, [Apps/build.gradle](../Apps/build.gradle),
    defines `ext` variables, `airwatchVersion` and `privacyVersion`, with the
    versions.

    Update the values of the variables if you are using different versions to
    those in the revision controlled file.

    Look for code like the following.

        ext {
            airwatchVersion = '21.7'
            privacyVersion = '21.5.1'

            ...
        }

That concludes the installation of the SDK for the repository.

# Notes
The following notes give some details on how to make the build configuration
pick up the SDK from this directory.

## Project Build
The project build.gradle file, [Apps/build.gradle](../Apps/build.gradle),
defines the following `ext` variables and methods.

-   `airwatchVersion` variable which you update to the required version.
-   `sdkVersionFile` and `sdkVersionDir` methods which return paths under the
    SDK download directory for the airwatchVersion.

In case this has been copied and pasted far away from the original project, the
definitions are like this:

    ext {
        airwatchVersion = '21.7'
        sdkVersionPath = {
            boolean endsWithFile, String[] segments -> new RelativePath(
                endsWithFile,
                "Download", "Android SDK v${airwatchVersion}", *segments
            ).getFile(new File(rootDir.getParent()))
        }
        sdkVersionFile = { String[] segments -> sdkVersionPath(true, segments) }
        sdkVersionDir = { String[] segments -> sdkVersionPath(false, segments) }
    }

## Application Builds
Repository declarations can be based on the above variables, like this:

    repositories {
        flatDir { dirs sdkVersionDir('Libs', 'ClientSDK') }
        flatDir { dirs sdkVersionDir('Libs', 'ClientSDK', 'dependencies') }
    }

# License
Copyright 2022 VMware, Inc. All rights reserved.
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause