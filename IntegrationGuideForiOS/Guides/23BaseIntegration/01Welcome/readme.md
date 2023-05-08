# Welcome
Welcome to the Workspace ONE Base Integration Guide for iOS and iPadOS.

The objective of this guide is for you to complete initial integration of your
app with the Workspace ONE platform. This guide includes coding work.

Instructions in this guide assume that all the tasks in the Workspace ONE
Integration Preparation Guide for iOS and iPadOS are already complete.

The Integration Preparation Guide is available

-   in Markdown format, in the repository that also holds this guide:  
    [github.com/vmware-samples/workspace-ONE-SDK-integration-samples](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples).

-   in Portable Document Format (PDF), from the SDK home page on the VMware
    developer website:  
    [developer.vmware.com/web/sdk/Native/airwatch-ios](https://developer.vmware.com/web/sdk/Native/airwatch-ios).

**Warning: Don't change the bundle identifier.** Your app must have the same
bundle identifier as it did when the Integration Preparation Guide was being
followed. This is because trust of the app by the Workspace ONE platform is
based in part on the bundle identifier. The Workspace ONE unified endpoint
manager (UEM) console and the Workspace ONE Intelligent Hub app are the
components that must trust your app.  
The Integration Preparation Guide has some discussion of Apple accounts as they
pertain to Workspace ONE integration work, in an appendix.

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause