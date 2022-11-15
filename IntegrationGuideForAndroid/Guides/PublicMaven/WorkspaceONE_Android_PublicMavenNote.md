# Public Maven Repository Integration Note
## Workspace ONE for Android
The VMware Workspace ONE® software development kit for Android can now be
utilized directly from a public Maven repository. Direct utilization from the
repository is an alternative to downloading the software development kit from
the My Workspace ONE website.

# Agreement
Before downloading, installing or using the VMware Workspace ONE SDK you must:

-   Review the
    [VMware Workspace ONE Software Development Kit License Agreement](https://developer.vmware.com/docs/12215/WorkspaceONE_SDKLicenseAgreement.pdf).
    By downloading, installing, or using the VMware Workspace ONE SDK you agree
    to these license terms. If you disagree with any of the terms, then do not
    use the software.

-   Review the [VMware Privacy Notice](https://www.vmware.com/help/privacy.html)
    and the
    [Workspace ONE UEM Privacy Disclosure](https://www.vmware.com/help/privacy/uem-privacy-disclosure.html),
    for information on applicable privacy policies.

That applies however you obtain or integrate the software.

# Table of Contents
{{TOC}}

# Tasks and Documentation
Integration of an Android application with the Workspace ONE platform includes a
number of tasks for the application developer. Instructions for these tasks are
given in a set of documents: the Workspace ONE Integration Guide for Android.

The integration guide documents are available

-   in Markdown format, in the repository that also holds official sample code:  
    [https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples)

-   in Portable Document Format (PDF), on the VMware website.  
    [https://developer.vmware.com/web/sdk/Native/airwatch-android](https://developer.vmware.com/web/sdk/Native/airwatch-android)

Most of the instructions apply the same whether the SDK is utilized directly
from the public Maven repository or by downloading from My Workspace ONE.
However, some instructions apply only if downloading and must be changed for
direct utilization.

Changed instructions are given below for each document in the integration guide
set.

There are no changes for the public Maven repository in the

-   Integration Overview.
-   Branding Integration Guide.

## Integration Preparation Guide Changes
Follow these changed instructions instead of those in the Integration
Preparation Guide to utilize the public Maven repository.

Skip the **Task: Obtain software development kit** instructions.

You should still download and review the 
[VMware Workspace ONE SDK License Agreement](https://developer.vmware.com/docs/12215/VMwareWorkspaceONESDKLicenseAgreement.pdf). If you don't accept the terms, stop 
here and don't integrate the SDK.

## Base Integration Guide Changes
Follow these changed instructions instead of those in the Base Integration
Guide to utilize the public Maven repository.

In the **Task: Add Client SDK** section, the **Build Configuration and
Files** instructions are changed as follows.

-   In the step **Add the required libraries to the build**:

    Instead of adding all the separate dependencies given in the guide, add only
    the following.

        repositories {
            maven {
                url 'https://vmwaresaas.jfrog.io/artifactory/Workspace-ONE-Android-SDK/'
            }
        }

        dependencies {
            // Integrate Workspace ONE at the Client level.
            //
            // Before downloading, installing, or using the VMware Workspace ONE
            // SDK you must:
            //
            // -   Review the VMware Workspace ONE Software Development Kit
            //     License Agreement that is posted here.
            //     https://developer.vmware.com/docs/12215/WorkspaceONE_SDKLicenseAgreement.pdf
            //
            //     By downloading, installing, or using the VMware Workspace ONE SDK you
            //     agree to these license terms. If you disagree with any of the terms, then
            //     do not use the software.
            //
            // -   Review the VMware Privacy Notice and the Workspace ONE UEM Privacy
            //     Disclosure for information on applicable privacy policies.
            //     https://www.vmware.com/help/privacy.html
            //     https://www.vmware.com/help/privacy/uem-privacy-disclosure.html
            implementation "com.airwatch.android:AirWatchSDK:21.3"
        }

-   Skip the step **Copy the required library files**.

-   Skip the step
    **Add the library files' location to the application build configuration**.

In the **Task: Add Framework** section, the **Build Configuration and
Files** instructions are changed as follows.

-   In the step **Add the required libraries to the build**:

    Instead of adding all the separate dependencies given in the guide, add only
    the following.

        repositories {
            maven {
                url 'https://vmwaresaas.jfrog.io/artifactory/Workspace-ONE-Android-SDK/'
            }
        }

        dependencies {
            // Integrate Workspace ONE at the Framework level.
            //
            // Before downloading, installing, or using the VMware Workspace ONE
            // SDK you must:
            //
            // -   Review the VMware Workspace ONE Software Development Kit
            //     License Agreement that is posted here.
            //     https://developer.vmware.com/docs/12215/WorkspaceONE_SDKLicenseAgreement.pdf
            //
            //     By downloading, installing, or using the VMware Workspace ONE SDK you
            //     agree to these license terms. If you disagree with any of the terms, then
            //     do not use the software.
            //
            // -   Review the VMware Privacy Notice and the Workspace ONE UEM Privacy
            //     Disclosure for information on applicable privacy policies.
            //     https://www.vmware.com/help/privacy.html
            //     https://www.vmware.com/help/privacy/uem-privacy-disclosure.html
            implementation "com.airwatch.android:AWFramework:21.3"
        }

-   Skip the step **Copy the required library files**.

# Open Source Sample Code
Open Source sample code for integration of Android applications with Workspace
ONE is published to a GitHub repository. The build files of one set of the
applications in the repository have been updated to utilize the public Maven
repository.

The sample code repository is here:  
[github.com/vmware-samples/workspace-ONE-SDK-integration-samples](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples)

The following sample apps have updated build files.

-   [brandStaticDelegateJava](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/tree/main/IntegrationGuideForAndroid/Apps/brandStaticDelegateJava)
-   [brandStaticDelegateKotlin](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/tree/main/IntegrationGuideForAndroid/Apps/brandStaticDelegateKotlin)
-   [brandStaticExtendJava](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/tree/main/IntegrationGuideForAndroid/Apps/brandStaticExtendJava)
-   [brandStaticExtendKotlin](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/tree/main/IntegrationGuideForAndroid/Apps/brandStaticExtendKotlin)

Each app directory has the following build files.

-   `publicMavenClient.gradle` to integrate at the Client level.
-   `publicMavenFramework.gradle` to add integration at the Framework level.

# Document Information
## Revision History
|Date     |Revision                                    |
|---------|--------------------------------------------|
|21apr2021|First publication, for 21.3 SDK for Android.|

## Legal
-   **VMware, Inc.** 3401 Hillview Avenue Palo Alto CA 94304 USA
    Tel 877-486-9273 Fax 650-427-5001 www.vmware.com
-   Copyright © 2022 VMware, Inc. All rights reserved.
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