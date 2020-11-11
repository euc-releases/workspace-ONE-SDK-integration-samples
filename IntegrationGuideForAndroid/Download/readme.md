# Directory for the Software Development Kit
This directory is set aside for installation of the Workspace ONE SDK for
Android. For context, see the instructions in the
[build](../Documentation/build.md) file.

The project build configuration has been set up to facilitate inclusion of the
SDK from this directory. Follow these instructions.

1.  Unzip the SDK download under this directory.
2.  Set the `airwatchVersion` ext variable in the
    [Apps/build.gradle](../Apps/build.gradle) file.

# Detailed Instructions
1.  Copy the root of the SDK to the following location:

    `/wherever/you/pulled/this/repository/IntegrationGuideForAndroid/Download/Android SDK v*major*.*minor*`

    Leave the sub-directories as they are in the download.

    For example, if you are using the 20.10 version, you will have a structure
    like this:

        IntegrationGuideForAndroid/Download/Android SDK v20.10/
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

2.  Set the version in the build configuration.

    The project build.gradle file, [Apps/build.gradle](../Apps/build.gradle),
    defines an `ext` variable, `airwatchVersion` with the version.

    Update the value of that variable if you are using a different version to
    the one in the revision controlled file.

    Look for this code:

        ext {
            airwatchVersion = '20.10'

            ...
        }

That concludes the installation of the SDK for the repository.

# Notes
The following notes give some details on how the build configuration picks up
the SDK from this directory.

## Project Build
The project build.gradle file, [Apps/build.gradle](../Apps/build.gradle),
defines the following `ext` variables and methods.

-   `airwatchVersion` variable which you update to the required version.
-   `sdkVersionFile` and `sdkVersionDir` methods which return paths under the
    SDK download directory for the airwatchVersion.

In case this has been copied and pasted far away from the original project, the
definitions are like this:

    ext {
        airwatchVersion = '20.10'
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

See the following files for examples of repository declarations.

-   [integrateClient.gradle](../Apps/clientKotlin/integrateClient.gradle)
-   [integrateFramework.gradle](../Apps/frameworkExtendKotlin/integrateFramework.gradle)

Those files are applied as script plugins by the build.gradle files for the
integrated applications in this repository, either directly or indirectly.

Code to apply is like this:

    apply from: file("integrateFramework.gradle")

See the
[Apps/frameworkExtendKotlin/build.gradle](../Apps/frameworkExtendKotlin/build.gradle)
file for an example of applying the script plugins.

# License
Copyright 2020 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause