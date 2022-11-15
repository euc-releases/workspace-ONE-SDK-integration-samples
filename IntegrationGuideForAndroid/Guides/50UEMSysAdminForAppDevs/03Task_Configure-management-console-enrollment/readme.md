# Task: Configure management console enrollment
Configuring the management console for enrollment is a system administrator task
for application developers. The enrollment configuration task is dependent on
the [Task: Register for Android Enterprise Mobility Management](../02Task_Register-for-Android-Enterprise-Mobility-Management/readme.md).
The following instructions assume that the dependent task is complete already.

Workspace ONE supports the following types of enrollment for Android.

-   Managed Android in Device Owner (DO) mode, sometimes referred to as Work
    Managed mode.
-   Managed Android in Profile Owner (PO) mode, sometimes referred to as Work
    Profile mode.
-   Unmanaged Android, known as registered mode.

The types of enrollment that will be available for a mobile device or app are
specified in the UEM configuration, in the *Organization Group* structure.

## Introduction to Organization Groups
The organization group (OG) is a fundamental concept of Workspace ONE UEM
administration. This introduction gives an overview for application developers.

A UEM can have multiple OGs, organized in a hierarchical tree structure.
Features for enrollment, policies, and settings, are all configured in the OG
structure. When an end user device or app enrolls, it will be assigned to one
OG. The policies and settings of that OG then apply to that device or app.

An OG at a lower level of the structure, referred to as a *child* OG, inherits
the configuration of its parent OG. The configuration of the parent OG will
specify which parts of the configuration can be overridden in a child OG. Each
OG has up to one parent.

Each OG has an identifier, referred to as its *Group ID*, and a *name*. The
Group ID sometimes has to be entered in the mobile user interface. The name will
be used for display purposes in the console and mobile user interfaces.

When the TestDrive hosting service instantiates a sandbox UEM server, it
configures one OG, referred to here as the *root* OG.

-   You cannot remove the root OG.
-   The root OG doesn't have a parent OG.
-   You cannot create siblings to the root OG.
-   You can rename the root OG.
-   You can create child OGs under the root OG.

This guide recommends adding a layer of child OGs to the structure so that the
configuration can support all types of UEM enrollment without being changed. See
the [Recommended Organization Group Structure](01Recommended-Organization-Group-Structure/readme.md)
for details and a diagram.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause