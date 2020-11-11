# Applications
The project in the Workspace ONE Integration Guide for Android code repository
contains a number of demonstration applications. Links to the application code
are here. For an introduction to the repository, see the [parent directory](..)
readme file.

There is a single project under the [Apps](../Apps) directory. Individual
applications are in sub-directories, each holding a complete application written
either in Java or in Kotlin, as follows.

# Base Application
The base applications have no integration with the Workspace ONE for Android
software development kit (SDK). The code is used as a base for other
applications.

-   [Apps/baseJava](../Apps/baseJava)
-   [Apps/baseKotlin](../Apps/baseKotlin)

The user interface is a single text that appears in the centre of the screen. If
you tap the text, it cycles between

-   Fixed message.
-   Module name of the application, "base Kotlin" for example.
-   Dark mode message.

The user interface appears in dark mode if the device is in dark mode, and vice
versa.

# Base Integration Guide
The following applications are demonstrations for the
[Base Integration Guide](../Guides/03BaseIntegration/WorkspaceONE_Android_BaseIntegration.md).

## Client level integration
Demonstration apps for client level integration.

-   [Apps/clientJava](../Apps/clientJava)
-   [Apps/clientKotlin](../Apps/clientKotlin)

The user interface displays the console version. Tapping the console version
message toggles a display of the SDK profile that was retrieved from the
console.

The console version is also displayed in an Android toast message when the SDK
has been initialized.

## Framework level integration
Demonstration apps for framework level integration, either by delegation or by
extension.

-   [Apps/frameworkDelegateJava](../Apps/frameworkDelegateJava)
-   [Apps/frameworkDelegateKotlin](../Apps/frameworkDelegateKotlin)
-   [Apps/frameworkExtendJava](../Apps/frameworkExtendJava)
-   [Apps/frameworkExtendKotlin](../Apps/frameworkExtendKotlin)

The user interface is the same as the Client level integration demonstration
apps except as follows.

-   SDK status is displayed instead of the console version.
-   The SDK profile has a plain text layout instead of JavaScript Object
    Notation (JSON).

# Branding Integration Guide
The following applications are demonstrations for the 
[Branding Integration Guide](../Guides/04Branding/WorkspaceONE_Android_Branding.md).

These applications are all integrated to the Framework level, either by
delegation or by extension.

## Enterprise branding support only
Demonstration apps for support of enterprise branding.

-   [Apps/brandEnterpriseOnlyDelegateJava](../Apps/brandEnterpriseOnlyDelegateJava)
-   [Apps/brandEnterpriseOnlyDelegateKotlin](../Apps/brandEnterpriseOnlyDelegateKotlin)
-   [Apps/brandEnterpriseOnlyExtendJava](../Apps/brandEnterpriseOnlyExtendJava)
-   [Apps/brandEnterpriseOnlyExtendKotlin](../Apps/brandEnterpriseOnlyExtendKotlin)

Branding resources from the management console, if configured, will appear in
the SDK user interface, as follows.

-   On the SDK splash screen that is displayed when the application starts
    cold:
    -   Logo will be a branding image.
    -   Color of the progress bar will be from the brand palette.

-   On the SDK login screen that is displayed when, for example, the end
    user sets a passcode after registration:
    -   Logo will be a branding image.
    -   Background color of, for example, the Next and Confirm navigation
        controls will be from the brand palette.

If any of the above branding resources don't appear, check the configuration in
the management console. Instructions can be found in the appendix to the
Branding Integration Guide.

## Static application branding
Demonstration apps for support of static branding.

-   [Apps/brandStaticDelegateJava](../Apps/brandStaticDelegateJava)
-   [Apps/brandStaticDelegateKotlin](../Apps/brandStaticDelegateKotlin)
-   [Apps/brandStaticExtendJava](../Apps/brandStaticExtendJava)
-   [Apps/brandStaticExtendKotlin](../Apps/brandStaticExtendKotlin)

Resources from the app appear in the SDK user interface, as follows.

-   On the SDK splash screen that is displayed when the application starts
    cold:
    -   Logo will be an app drawable.
    -   Color of the progress bar will be an app color.
    -   Background color will be an app color, unless in dark mode.

-   On the SDK login screen that is displayed when, for example, the end
    user sets a passcode after registration:
    -   Logo will be an app drawable.
    -   Background color will be an app color, unless in dark mode.

A drawable from the app resources also appears in the app user interface.

## Static application branding with optional override to enterprise branding
Demonstration apps for support of static branding overridden by enterprise
branding.

-   [Apps/brandEnterprisePriorityDelegateJava](../Apps/brandEnterprisePriorityDelegateJava)
-   [Apps/brandEnterprisePriorityDelegateKotlin](../Apps/brandEnterprisePriorityDelegateKotlin)
-   [Apps/brandEnterprisePriorityExtendJava](../Apps/brandEnterprisePriorityExtendJava)
-   [Apps/brandEnterprisePriorityExtendKotlin](../Apps/brandEnterprisePriorityExtendKotlin)

Resources from the app and the management console appear in the SDK and app user
interface, as described in the preceding sections. In case both the console and
app specify a resource, the console specification has priority.

## Dynamic branding
Demonstration apps for dynamic branding.

-   [Apps/brandDynamicDelegateJava](../Apps/brandDynamicDelegateJava)
-   [Apps/brandDynamicDelegateKotlin](../Apps/brandDynamicDelegateKotlin)
-   [Apps/brandDynamicExtendJava](../Apps/brandDynamicExtendJava)
-   [Apps/brandDynamicExtendKotlin](../Apps/brandDynamicExtendKotlin)

Dynamically generated resources appear in the SDK user interface, as follows.

-   On the SDK splash screen that is displayed when the application starts cold:
    -   Logo will be an image generated at run time from the following texts:
        -   The word "Loading".
        -   The current date and time.
        -   A hexadecimal representation of the Primary Color in the enterprise
            brand palette if one is set, or the word "null" if unset or
            unavailable.
    -   Color of the progress bar will be red.

-   On the SDK login screen that is displayed when, for example, the end user
    sets a passcode after registration:
    -   Logo will be an image generated at run time from the following texts:
        -   The word "Input".
        -   The current date and time.
        -   A hexadecimal representation of the Primary Color in the enterprise
            brand palette if one is set, or the word "null" if unset or
            unavailable.
    -   Background color of, for example, the Next and Confirm navigation
        controls will be red.

-   When the SDK posts a notification, the small icon that is displayed in the
    device status bar will be an app drawable.
    
    In some deployments, power cycling the mobile device will cause the SDK to
    post a notification, that the application requires authentication.

The app user interface has the following elements.

-   An image from the app drawable resources is displayed in the upper half of
    the screen. When the image is tapped, an Android notification is posted. The
    notification icon will be an app drawable.
-   An enterprise branding image is displayed in the lower half of the screen,
    if configured in the management console.

# Duplication
A lot of the code in the project is duplicated between applications. In theory,
code could be, for example, pulled in from common directories by Gradle or a
custom tool. In practice, those approaches have limitations, lead to more
maintenance overhead than duplication, and don't result in a repository that is
easy to understand for prospective application developers.  
Duplication is managed by the maintainers of the repository. See also the
[samers.md](samers.md) file.

# License
Copyright 2020 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause