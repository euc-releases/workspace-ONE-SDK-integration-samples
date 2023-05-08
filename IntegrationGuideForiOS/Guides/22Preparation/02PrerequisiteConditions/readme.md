# Prerequisite Conditions
Before you begin, you will need the following.

-   Access to a Workspace ONE management console.

    You will need access to a Workspace ONE management console to work on
    application integration. The management console is sometimes referred to as
    the UEM, an abbreviation for Unified Endpoint Manager.

    You will need to know the following:

    -   Server address.
    -   Administrative login credentials.

    You will need the following privileges:

    -   Upload a mobile application installer, which will be an iOS Package
        Archive (IPA) file.
    -   Either create an organisation group for an end user, or get the name of
        an existing group.
    -   Either create a new end user with a suitable profile for development
        purposes, or get the name of an existing suitable user.
    -   Either create enrolment credentials for an end user, or get existing
        credentials.

    Best practice is to have a separate console, or organisation group, for 
    software development.

    Workspace ONE supports installation of custom apps on managed and unmanaged
    devices. If you have a choice, it will be easier for development if the use
    of unmanaged devices, sometimes referred to as Hub Registered mode, is
    allowed in the device enrollment policies.

-   Apple account, ideally with a developer membership.

    In case you don't have a developer membership, it is possible to use a
    personal Apple account for Workspace ONE integration work. A developer
    account will be better. For a discussion of the differences, see the
    [Appendix: Apple Accounts](../21Appendix_Apple-Accounts/readme.md).

-   Developer environment.

    The instructions in the integration guide documents assume you use the Apple
    Xcode integrated developer environment (IDE). Check the
    [Compatibility](01Compatibility/readme.md) table for a recommended version.

    The Xcode IDE can be obtained from Apple, for example by downloading from
    their developer website here.  
    [https://developer.apple.com/download/all/](https://developer.apple.com/download/all/)

-   Developer device.

    You will need a physical iOS or iPadOS device to to work on application
    integration. You will install the Workspace ONE Intelligent Hub app on the
    device as part of preparation.
    
    The iOS and iPadOS simulators cannot be used because they may appear as
    jailbroken or otherwise compromised to the Hub app. The Hub must be used to
    install the application that is being integrated at least once during the
    integration work. After the first installation via Hub, subsequent
    installations can be made using Xcode in the usual way.

    The device must be set up to trust your computer. When you connect the
    device for the first time a prompt will be shown on the device, which you
    must accept to use the device for development purposes.
    
    The device might also have to be set up for developer use, depending on its
    operating system version. Instructions for setting up a developer device can
    be found on the Apple developer website, for example here.  
    [developer.apple.com/…/enabling-developer-mode-on-a-device](https://developer.apple.com/documentation/xcode/enabling-developer-mode-on-a-device)

    The device must be compatible with the Apple account that you are using. If
    you are using a developer account then the device might have to be
    registered to the associated developer organization. Xcode can do this
    automatically. Check the Apple developer website for details, for example here.  
    [developer.apple.com/…/register-a-single-device](https://developer.apple.com/help/account/register-devices/register-a-single-device)

    Don't use a device that is already enrolled with a production Workspace ONE
    console. A device can sometimes be unenrolled from within the Hub
    application on the device, or by resetting to factory defaults.

When the prerequisite conditions are met, you can start the first
[Task: Select or create an application to integrate](../03Task_Select-or-create-an-application-to-integrate/readme.md).

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause