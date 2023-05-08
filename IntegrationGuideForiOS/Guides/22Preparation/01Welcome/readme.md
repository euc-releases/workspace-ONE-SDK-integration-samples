# Welcome
Welcome to the Workspace ONE Integration Preparation Guide for iOS and iPadOS.

## Objectives
The objectives of this guide are for you to

-   verify your app works.
-   verify your developer device is viable.
-   use the Workspace ONE Unified Endpoint Manager (UEM) console.
-   verify the round trip from integrated developer environment (IDE) to end
    user device.
-   verify you will be able to install the app from your IDE later, when
    integration work is in progress.

The integration preparation guide doesn't include any work at the code level.
That will start in the base integration guide, which is next.

## Overview
This is an overview of the steps in the guide.

1.  Build your app without Workspace ONE integration.
2.  Install the app to a developer device from the IDE.
3.  Uninstall the app from the developer device.
4.  Generate an iOS Package Archive (IPA) file for the app.
5.  Upload the IPA to the UEM as a new internal app.
6.  Install Hub on the developer device.
7.  Enrol Hub with the UEM console.
8.  From Hub, install your app.
9.  From the IDE, make a visible change to the app and install that as an
    upgrade.

Enrolling Hub with the UEM console establishes trust between the device and the
UEM. Installing your app from Hub will enable it to be trusted by the UEM
subsequently.

## Installation Order Note
If you follow the integration guide set, you will install your application on a
developer device as follows.

-   The first installation will be of a non-integrated version of the
    application via Workspace ONE.

-   Subsequent installations will be of integrated versions of the application
    from your IDE. This type of installation is referred to as a *side load*.

The side-load installations will be upgrades. The application won't ever be
uninstalled after the first installation via Workspace ONE.

It actually isn't necessary to install an application via Workspace ONE if it
isn't integrated. It might therefore seem natural to delay installation via
Workspace ONE until some integration work has been done. This guide doesn't
follow that order though.

The rationale is that installation via Workspace ONE involves activities with
which you may be unfamiliar, such as generating an IPA file, and use of the
Workspace ONE management console. It's better to do those activities with the
application in a known working state.

## First Steps
Begin by checking the
[Prerequisite Conditions](../02PrerequisiteConditions/readme.md).

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause