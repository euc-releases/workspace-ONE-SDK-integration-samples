# Task: Initialize the software development kit runtime
Initializing the SDK runtime is a Workspace ONE platform integration task for
mobile application developers. This task is dependent on the
[Task: Add the software development kit package](../03Task_Add-the-software-development-kit-package/readme.md).
The following instructions assume that the dependent task is complete already.

Before you begin, you will need to know the Team ID of your Apple developer
account. Instructions for locating the required value can be found on the Apple
developer website, for example here.  
[developer.apple.com/help/account/manage-your-team/locate-your-team-id](https://developer.apple.com/help/account/manage-your-team/locate-your-team-id)

Note that your app must have the same bundle identifier as it did when the
Integration Preparation Guide was being followed, as discussed in the
[Welcome](../01Welcome/readme.md) section. This could mean that you must
continue to use the same Team ID also.

How you initialize the SDK runtime depends on how the app user interface is
implemented.

-   If the app user interface is implemented in SwiftUI, follow
    the [Initialize from SwiftUI](01Initialize-from-SwiftUI/readme.md)
    instructions.

-   If the app user interface is implemented in a storyboard, follow
    the [Initialize from Storyboard](./02Initialize-from-Storyboard/readme.md)
    instructions.

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause