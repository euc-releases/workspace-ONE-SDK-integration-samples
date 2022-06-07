# Integration Overview
## Workspace ONE for Android
Android applications can be integrated with the VMware Workspace ONE® platform,
by using its mobile software development kit. An application that has been
integrated can use the features of the platform, such as:

-   Detection of device compromise.
-   Encrypted storage of application data on the device.
-   Single sign-on authentication with enterprise services.

This document is part of the Workspace ONE Integration Guide for Android set.

# Table of Contents
{{TOC}}

# Introduction
Android applications can be integrated with the Workspace ONE platform, by using
its mobile software development kit. An application that has been integrated can
use the features of the platform.

The Workspace ONE mobile Software Development Kit (SDK) can be integrated to
different levels, depending on requirements.

## Client Integration
Client integration is the lowest level, and requires the least work by the
application developer.

Client integration makes available the following features:

-   Enrolment status.
-   User information.
-   Partial SDK profile.
-   Device compromise detection, i.e. detection of rooted status.

None of these features require user interaction in the application. Client
integration doesn't require modification of the application user interface.

## Framework Integration
Framework is the level of integration above Client, and requires more work by
the application developer.

Framework integration makes available the following features:

-   Enrolment status.
-   User information.
-   Full SDK profile.
-   Device compromise detection.
-   Single Sign On (SSO) for apps.
-   End user authentication.
-   Inactivity lock.
-   Application data encryption.
-   Copy paste restrictions.

Some of these features require user interaction in the application. Framework
integration will require modifications to the application user interface.
Branding can be applied to the SDK user interface, to make it appear as part of
the application whole.

## Networking Integration
Networking is the level of integration above Framework, and requires more work
by the application developer.

Networking integration makes available the following features:

-   All Framework integration features.
-   Tunneling of application data.
-   NTLM and Basic Authentication.
-   Certificate-based authentication.

Networking integration is based on Framework integration and requires
modifications to the application user interface.

## Privacy Module
The Workspace ONE Privacy Module is independent of Client, Framework, and
Networking integration.

# Operational Data
VMware collects a limited set of information from the Workspace ONE SDK to
operate and support the SDK within third-party apps, such as notifying customers
about feature removal or platform compatibility. This data is anonymized and
analyzed in aggregate, and cannot be used to identify the application containing
the SDK or end user. This data is sent to scapi.vmware.com. Please
refer to [VMware's Privacy Notices](https://www.vmware.com/help/privacy.html)
online for more information about VMware data collection and privacy policies.

# Tasks and Documentation
Integration of an Android application with the Workspace ONE platform includes a
number of tasks for the application developer. Instructions for these tasks are
given in a set of documents: the Workspace ONE Integration Guide for Android.

## Integration Guide Structure
The Workspace ONE Integration Guide for Android is a particular set of
documents. Other documents exist, either legacy or maintained outside the
Integration Guide set.

Documents in the Integration Guide set contain:

-   Step-by-step instructions.
-   Snippets of Java and Kotlin code.
-   Discussion of features.
-   Explanatory diagrams.
-   Screen capture images.

Some documents include instructions for tasks in the Workspace ONE management
console. These will be for a simplified version of the task that is focussed on
a particular integration activity. They aren't intended to replace the system
administrator documentation.

At time of writing, the following documents in the Integration Guide set are
available.

## Integration Preparation Guide
The Integration Preparation guide includes instructions for tasks to complete
before starting integration, for example:

-   Install the mobile application via Workspace ONE.
-   Obtain the software development kit.

Note that it is recommended to install the application via Workspace ONE before
integration work begins. An alternative could be to complete Client or Framework
integration first but this isn't recommended. The rationale is as follows.

Installing the application will require the following steps:

-   Installing the Workspace ONE Intelligent Hub on a developer device.
-   Generating a signed Android Package (APK) for the application.
-   Uploading the APK file to the Workspace ONE management console.
-   Installing the application from the Hub.

It should be easier to do these steps, and verify correct execution, with a
known working mobile application. The other way round, integration then
installation, is a kind of big bang approach in which everything has to have
been done right or nothing works.

## Base Integration Guide
The Base Integration guide includes instructions for:

-   Client integration.
-   Framework integration.

## Branding Integration Guide
The Branding Integration guide includes instructions for user interface
customisation tasks, including the following.

-   Implement static application branding.
-   Set a notification icon.
-   Configure dark mode selection.
-   Support enterprise branding.
-   Implement dynamic branding.

Most tasks are for customization of the SDK user interface, so that it appears
as part of the application whole. The guide also includes instructions for how
to access branding resources in the enterprise management console that could be
used to customize the application user interface.

## Links
The integration guides are available

-   in Markdown format, in the repository that also holds the sample code:  
    [https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples)

-   in Portable Document Format (PDF), on the VMware website.  
    [https://developer.vmware.com/web/sdk/Native/airwatch-android](https://developer.vmware.com/web/sdk/Native/airwatch-android)

The VMware website also has links to other integration resources, including
earlier versions of the Developer Guide documentation, other technical
documentation, and the SDK itself. You will require a My Workspace One login in
order to download the SDK. Speak with your Workspace ONE representative for
access.

The following table lists the guides and gives links to their published
locations in different formats.

Document| Markdown | PDF
--------|----------|----
Integration Overview (this document) | [IntegrationOverview.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/01Overview/WorkspaceONE_Android_IntegrationOverview.md) | [IntegrationOverview.pdf](https://developer.vmware.com/docs/12354/WorkspaceONE_Android_IntegrationOverview.pdf)
Integration Preparation Guide | [IntegrationPreparation.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/02Preparation/WorkspaceONE_Android_IntegrationPreparation.md) | [IntegrationPreparation.pdf](https://developer.vmware.com/docs/12355/WorkspaceONE_Android_IntegrationPreparation.pdf)
Base Integration Guide | [BaseIntegration.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/03BaseIntegration/WorkspaceONE_Android_BaseIntegration.md) | [BaseIntegration.pdf](https://developer.vmware.com/docs/12356/WorkspaceONE_Android_BaseIntegration.pdf)
Branding Integration Guide | [Branding.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/04Branding/WorkspaceONE_Android_Branding.md) | [Branding.pdf](https://developer.vmware.com/docs/12357/WorkspaceONE_Android_Branding.pdf)



# Integration Paths Diagram
The following diagram shows the tasks involved in integrating an Android
application with the Workspace ONE platform. The document in the Integration
Guide for Android set that includes the instructions for each task is also
indicated, where there is a current document in the set.

The Privacy Module is independent of the Client, Framework, Networking path.

![**Diagram 1:** Workspace ONE for Android Integration Paths](IntegrationPaths.svg)

# Document Information
## Published Locations
This document is available

-   in Markdown format, in the repository that also holds the sample code:  
    [https://github.com/vmware-samples/...IntegrationOverview.md](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/01Overview/WorkspaceONE_Android_IntegrationOverview.md)

-   in Portable Document Format (PDF), on the VMware website:  
    [https://developer.vmware.com/...IntegrationOverview.pdf](https://developer.vmware.com/docs/12354/WorkspaceONE_Android_IntegrationOverview.pdf)

## Revision History
|Date     |Revision                                    |
|---------|--------------------------------------------|
|03jul2020|First publication, for 20.4 SDK for Android.|
|22jul2020|Updated for 20.7 SDK for Android.           |
|11oct2020|Updated URL paths to default branch main.   |

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