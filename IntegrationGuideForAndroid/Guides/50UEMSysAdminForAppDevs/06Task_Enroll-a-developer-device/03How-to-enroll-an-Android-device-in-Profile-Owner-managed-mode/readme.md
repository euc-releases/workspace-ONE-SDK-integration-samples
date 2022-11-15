## How to enroll an Android device in Profile Owner managed mode
A device that doesn't already have a work profile have can be enrolled in
Android Profile Owner (PO) managed mode. PO mode isn't recommended for general
application development for the reasons given in the introduction to this [Task: Enroll a developer device](../../06Task_Enroll-a-developer-device/readme.md)
but might be required as a test case.

**Warnings**

-   The Workspace ONE Intelligent Hub application cannot be enrolled with more
    than one management console at a time. If Hub is already installed and
    enrolled on your developer device, then it must now be removed and
    re-installed, or must be reset, i.e. have its storage cleared. Removing or
    resetting the Hub may cause removal of any associated applications from the
    device.

-   Any VMware productivity apps already installed on an unmanaged developer
    device might stop working when it is enrolled in PO mode. This applies
    whether the apps were enrolled standalone or through Hub running in
    registered mode. The VMware productivity apps include the Workspace ONE
    Boxer email app, and the Workspace ONE Web browser, for example.

**Tip**: Set a device passcode before you begin enrolment. Typical UEM
configurations will require a passcode, as a security policy. If a device
passcode isn't set at the start of the enrolment interaction, you will be forced
to set it as an enrolment step, which sometimes doesn't go smoothly.

These instructions assume that the [Recommended Organization Group Structure](../../03Task_Configure-management-console-enrollment/01Recommended-Organization-Group-Structure/readme.md)
has been configured in the UEM. Some steps will be different if that isn't the
case.

Proceed as follows.

1.  Install the Workspace ONE Intelligent Hub mobile application.

    The Hub can be installed from the Google Play Store. Search for "workspace
    one intelligent hub", for example.

2.  Open the Hub app.

    The screen will show the Workspace ONE Intelligent Hub logo and a prompt for
    email address or server, as in the following screen capture.

    ![**Screen Capture:** Workspace ONE Intelligent Hub logo and prompt](../02How-to-enroll-an-Android-device-in-Device-Owner-managed-mode/ScreenCapture_HubPrompt.png)

3.  Enter the enrollment server address and tap Next.

    See the instructions [How to find out the enrollment server address](../01How-to-find-out-the-enrollment-server-address/readme.md)
    if necessary.

    There will be some processing and then the prompt will reappear with an
    additional field requiring entry: Group ID.

4.  Enter the Group ID of your root OG.

    In the [Recommended Organization Group Structure](../../03Task_Configure-management-console-enrollment/01Recommended-Organization-Group-Structure/readme.md)
    the Group ID is: `og`

    There will be some more processing and then you will be prompted to select a
    group for your device.

5.  Select the group Profile and tap to continue.

    You will be prompted for a Username and Password.

8.  Enter the username and password of an end user account and tap Next.

    If the [Recommended End User Configuration](../../05Task_Configure-end-users/01Recommended-End-User-Configuration/readme.md)
    has been set up then the username and password could both be: `a`

    There will be some more processing. You might be prompted to save the
    password just entered. Ignore or decline the option.

    When enrollment processing has finished, you will be prompted to accept a
    privacy agreement.

9.  Accept the privacy agreement by tapping: I Understand.

    You will be prompted to opt in to additional data sharing.

10. Select "Not Now" and confirm by tapping "Don't Send" when prompted.

    There will be some more processing then you will be prompted to change the
    way that you work.

11. Tap Accept & Continue.

    There will be some more processing and notification that Hub configuration
    is in process.

This completes Android Profile Owner mode enrollment. The device is now
ready for developer use.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause