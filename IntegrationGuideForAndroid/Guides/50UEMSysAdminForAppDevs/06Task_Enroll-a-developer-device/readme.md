# Task: Enroll a developer device
Enrolling a developer device is a common task for application developers. The
following instructions refer to some concepts introduced in the
[Task: Configure management console enrollment](../03Task_Configure-management-console-enrollment/readme.md).

This guide gives instructions for enrolling in any of the following modes.

-   Managed Android in Device Owner (DO) mode, sometimes referred to as Work
    Managed mode.
-   Managed Android in Profile Owner (PO) mode, sometimes referred to as Work
    Profile mode.
-   Unmanaged Android, known as registered mode.

You should test your application in all modes that your end users will use in
production. In case you don't have known end users yet, consider the following
general recommendations.

-   Android DO managed mode can be used on a developer device that you don't
    mind resetting to its factory default state. Enrollment will delete all data
    on the device.

-   Android PO managed mode can be used on a developer device that doesn't
    already have a work profile. However, PO mode isn't recommended for
    application development for the following reasons.

    -   You will have to upload your app to an enterprise Play Store instance.
        The store doesn't allow some package name prefixes, and doesn't appear
        to support removal, nor replacement without upgrade.
    -   You will have to follow a different procedure to side load your app in
        development from Android Studio or the Android Debug Bridge (adb) tool.
        In PO mode, the device has an additional user account. The default
        Gradle files mightn't facilitate automated testing of your app in PO
        mode.

-   Registered mode can be used on any device that isn't already enrolled
    against a Workspace ONE UEM console, and doesn't have any standalone
    enrolled apps. (The Workspace ONE Boxer email app, the Workspace ONE Web
    browser, and other apps in the VMware productivity suite support standalone
    enrollment.) No device data will be lost.

The enrollment instructions for different modes are substantially but not
completely the same. Common instructions are duplicated to so that each
subsection can be followed independently.

In any mode, you will first need to know [How to find out the enrollment server address](01How-to-find-out-the-enrollment-server-address/readme.md).

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause