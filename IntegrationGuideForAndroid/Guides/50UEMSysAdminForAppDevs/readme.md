# UEM System Administration for Application Developers
## Workspace ONE for Android
Android applications can be integrated with the VMware Workspace ONE® platform.
Integration work will require access to a Workspace ONE Unified Endpoint Manager
console, and the completion of administrative tasks there. Follow the
instructions below to set up and administer a Workspace ONE console that
supports application development.

This guide only covers tasks necessary to support application development. It
doesn't replace the system administrator user guides for the Workspace ONE
product.

This document is part of the Workspace ONE Integration Guide for Android set.

# Introduction
Development of an Android application that is integrated with Workspace ONE will
require access to a Workspace ONE Unified Endpoint Manager console. The console
will be used to

-   manage enrollment of mobile devices and applications in various modes.
-   manage the availability and installation of mobile applications, including
    applications under development.
-   manage the configuration of applications.
-   manage end users.

This guide covers the following enrollment modes.

-   Managed Android in Device Owner (DO) mode, sometimes referred to as Work
    Managed mode.
-   Managed Android in Profile Owner (PO) mode, sometimes referred to as Work
    Profile mode.
-   Unmanaged Android, known as registered mode.

All enrollment modes can be supported by a single console. The Workspace ONE
platform supports other enrollment modes for Android, such as Corporate Owned
Personally Enabled (COPE), but these aren't in scope of this guide.

Best practice is to have a separate console instance, tenant, or organisation
group, set aside for software development.

## VMware TestDrive service
VMware operates a service, VMware TestDrive, that can be used to host a
management console instance for application development support. The service is
free, doesn't need system administrator expertise to utilize, and doesn't
require the installation of any client nor server software.

This guide starts with instructions for setting up a TestDrive UEM. In case you
don't use TestDrive, the remaining instructions will still be applicable to your
UEM deployment.

## Abbreviations and Terms
The following abbreviations and terms are used with the following meanings in
this guide.

-   EMM is an abbreviation for Enterprise Mobility Management.
-   UEM is an abbreviation for Unified Endpoint Manager and is used here to mean
    the Workspace ONE management console.
-   Enrollment means the establishment of a trusted connection between a
    management console and a mobile device or application.

# [Task: Set up a management console](01Task_Set-up-a-management-console/readme.md)

# [Task: Register for Android Enterprise Mobility Management](02Task_Register-for-Android-Enterprise-Mobility-Management/readme.md)

# [Task: Configure management console enrollment](03Task_Configure-management-console-enrollment/readme.md)

## [Recommended Organization Group Structure](03Task_Configure-management-console-enrollment/01Recommended-Organization-Group-Structure/readme.md)

## [How to set up the recommended Organization Group structure](03Task_Configure-management-console-enrollment/02How-to-set-up-the-recommended-Organization-Group-structure/readme.md)

## [How to log in and select an Organization Group](03Task_Configure-management-console-enrollment/03How-to-log-in-and-select-an-Organization-Group/readme.md)

# [Task: Set up the mobile application catalog](04Task_Set-up-the-mobile-application-catalog/readme.md)

# [Task: Configure end users](05Task_Configure-end-users/readme.md)

## [Recommended End User Configuration](05Task_Configure-end-users/01Recommended-End-User-Configuration/readme.md)

## [How to create an end user account](05Task_Configure-end-users/02How-to-create-an-end-user-account/readme.md)

## [How to delete an end user account](05Task_Configure-end-users/03How-to-delete-an-end-user-account/readme.md)

# [Task: Enroll a developer device](06Task_Enroll-a-developer-device/readme.md)

## [How to find out the enrollment server address](06Task_Enroll-a-developer-device/01How-to-find-out-the-enrollment-server-address/readme.md)

## [How to enroll an Android device in Device Owner managed mode](06Task_Enroll-a-developer-device/02How-to-enroll-an-Android-device-in-Device-Owner-managed-mode/readme.md)

## [How to enroll an Android device in Profile Owner managed mode](06Task_Enroll-a-developer-device/03How-to-enroll-an-Android-device-in-Profile-Owner-managed-mode/readme.md)

## [How to enroll an Android device in Registered mode](06Task_Enroll-a-developer-device/04How-to-enroll-an-Android-device-in-Registered-mode/readme.md)

# [Task: Configure security settings](07Task_Configure-security-settings/readme.md)

# [Troubleshooting](13Troubleshooting/readme.md)

# [Appendix: How to enroll an app in standalone mode](14Appendix_How-to-enroll-an-app-in-standalone-mode/readme.md)

# Document Information
## Published Locations
This document is available

-   in Markdown format, in the repository that also holds the sample code:  
    [https://github.com/vmware-samples/...UEMSysAdminForAppDevs/](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForAndroid/Guides/50UEMSysAdminForAppDevs/readme.md)

-   in Portable Document Format (PDF), on the VMware website:  
    [https://developer.vmware.com/...UEMSysAdminForAppDevs.pdf](https://developer.vmware.com/docs/17139/WorkspaceONE_Android_UEMSysAdminForAppDevs.pdf)

## Revision History
|Date     |Revision          |
|---------|------------------|
|09nov2022|First publication.|

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