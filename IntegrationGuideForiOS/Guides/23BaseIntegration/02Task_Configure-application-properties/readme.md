# Task: Configure application properties
Configuring application properties is a Workspace ONE platform integration task
for mobile application developers. This task is dependent on all the tasks in
the Integration Preparation Guide, as discussed in
the [Welcome](../01Welcome/readme.md)
section. The following instructions assume that the dependent tasks are complete
already.

These are the required configurations.

-   The Workspace ONE Intelligent Hub app will use a **custom URL scheme** to
    send enrollment details to your app. Therefore your app must declare a
    custom URL scheme.

-   Particular **Queried URL Schemes** will be used to discover Hub
    communication channels and must be declared by all SDK apps.

-   A **Camera Usage Description** must be declared by all SDK apps in order to
    support Workspace ONE QR code enrollment. If a description isn't declared
    then the operating system blocks the app from access to the device camera.

-   A **Face ID Usage Description** must be declared in order to support face
    recognition for biometric authentication. If a description isn't declared
    then the operating system blocks the app from access to Face ID. Note that
    no declaration is needed for access to Touch ID.
    
    Biometric authentication may be allowed in the Workspace ONE unified
    endpoint manager (UEM) console, as a security policy. Therefore it must be
    supported by all SDK apps.

-   **Workspace ONE doesn't support multiple windows** at time of writing. If
    your app has a scene manifest then it must declare that multiple windows
    aren't supported.

-   If you are developing more than one app, then add a
    **shared keychain group** for secure inter-application communication. The
    shared keychain group must be named `asdk` and be first in the Keychain
    Groups list.

You can set those configurations as you like if you are familiar with the Xcode
project user interface already. Skip ahead to the screen captures at the end of
the second and third sections for reference. Or you can follow these
step-by-step instructions.

## [Declare a custom URL scheme](01Declare-a-custom-URL-scheme/readme.md)

## [Add Queried URL Schemes and other required properties](02Add-Queried-URL-Schemes-and-other-required-properties/readme.md)

## [Add a shared keychain group](03Add-a-shared-keychain-group/readme.md)

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause