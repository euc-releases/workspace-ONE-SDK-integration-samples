## Recommended Organization Group Structure
This guide recommends creating an OG structure that includes all types of UEM
enrollment. This will enable the application development effort to proceed and
support all types of enrollment without the need to reconfigure the UEM.

The following recommendations are also made.

-   Configure selection of enrollment type by the end user.
-   Use two- or three-letter values for OG Group ID values, to facilitate manual
    entry in the mobile user interface.
-   Use longer, descriptive texts for OG names.
-   Add a separate child OG for standalone enrollment. Standalone enrollment is
    supported by the Workspace ONE Boxer email app, the Workspace ONE Web
    browser, Workspace ONE PIV-D Manager, and other apps in the VMware
    productivity suite.

<p class="always-page-break" />
This diagram represents a recommended OG structure, as a UML Object Diagram.

![**Diagram 1:** Organization Group Structure](ob01_OrganizationGroups.svg)

Follow the [How to set up the recommended Organization Group structure](../02How-to-set-up-the-recommended-Organization-Group-structure/readme.md)
instructions to set up the above OG structure.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause