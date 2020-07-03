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

# Integration Paths Diagram
The following diagram shows the tasks involved in integrating an Android
application with the Workspace ONE platform. The document in the Integration
Guide for Android set that includes the instructions for each task is also
indicated, where there is a current document in the set.

The Privacy Module is independent of the Client, Framework, Networking path.

![**Diagram 1:** Workspace ONE for Android Integration Paths](IntegrationPaths.svg)

# Document Information
## Revision History
|         |
|---------|
|03jul2020|First publication, for 20.4 SDK for Android.

## Legal
-   **VMware, Inc.** 3401 Hillview Avenue Palo Alto CA 94304 USA
    Tel 877-486-9273 Fax 650-427-5001 www.vmware.com
-   Copyright © 2020 VMware, Inc. All rights reserved.
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