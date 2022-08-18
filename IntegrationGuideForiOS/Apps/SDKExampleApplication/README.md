# Workspace ONE Example Application for iOS
The SDKExampleApplication app provides an overview of the common use cases of
the Workspace ONE® software development kit (SDK) for iOS. Where appropriate,
comments are added to the code.

A complete developer guide is published on the VMware Code website, on the SDK
home page, see:  
[developer.vmware.com/web/sdk/Native/airwatch-ios](https://developer.vmware.com/web/sdk/Native/airwatch-ios).

These instructions assume that you will use a physical device and the Apple
Xcode integrated development environment (IDE).

# Agreement
Before downloading, installing or using the VMware Workspace ONE SDK you must:

1.  Review the
    [VMware Workspace ONE Software Development Kit License Agreement](https://developer.vmware.com/docs/12215/VMwareWorkspaceONESDKLicenseAgreement.pdf).
    By downloading, installing, or using the VMware Workspace ONE SDK you agree
    to these license terms.  If you disagree with any of the terms, then do not
    use the software.

2.  Review the
    [VMware Privacy Notice](https://www.vmware.com/help/privacy.html)
    and the
    [Workspace ONE UEM Privacy Disclosure](https://www.vmware.com/help/privacy/uem-privacy-disclosure.html),
    for information on applicable privacy policies.

This applies however you obtain the software.

# Set up the SDK example application
The Workspace ONE SDK frameworks are excluded from this app. Add them to the
project prior to building from source. 

The recommended way to add the frameworks is through Swift Package Manager
(SPM). See under [Integrate the Swift package], below.

The Swift package distribution is available from the 21.09 release of Workspace
ONE for iOS. If you are using an earlier version, or aren't using SPM for any
reason, then integrate the XCFramework instead. See under
[Integrate the XCFramework distribution], below.

If your app is integrated with an earlier version of Workspace ONE for iOS,
using the XCFramework distribution, you can migrate to the Swift package. See
under [Migrate from XCFramework integration to the Swift package], below.

## Integrate the Swift package
The Workspace ONE SDK for iOS is available as a Swift package. This is the
recommended way to integrate the SDK.

Proceed as follows.

1.  Open your app project in Xcode.

2.  Navigate to File, Swift Packages, Add Package Dependency...

    This opens the Choose Package Repository screen.

3.  Enter the address of this repository
    `https://github.com/vmwareairwatchsdk/iOS-WorkspaceONE-SDK` and click Next.

    This opens the Choose Package Options screen.

4.  Select the rule Branch, leave the default value for branch name, and click
    Next.

    Xcode will resolve the package dependency, which might take some time.

    When resolution finishes, an Add Package screen opens.

5.  Select to add the AWSDK package product to your app target and click Finish.

6.  Check that the package has been added.

    In your app target, on the General tab, under Frameworks, Libraries, and
    Embedded Content, the name AWSDK should be listed.

This completes integration of the Workspace ONE for iOS Swift package. You are
now ready for the next step, to [Build the SDK example application].

## Migrate from XCFramework integration to the Swift package
The Workspace ONE SDK for iOS is available as a Swift package since the 21.09
release. If your app is integrated with an earlier version, using the
XCFramework distribution, you can migrate to the Swift package.

Proceed as follows.

1.  Open your app project in Xcode.

2.  Navigate to the app target, select the General tab and scroll down to the
    Frameworks, Libraries, and Embedded Content section.

3.  Remove the item: `AWSDK.xcframework`

4.  In the Xcode menu, select Product, Clean Build Folder.

The XCFramework integration has now been removed and the Swift package can be
integrated instead. See under [Integrate the Swift Package], above.

## Integrate the XCFramework distribution
The Workspace ONE SDK for iOS is available as a Swift package since the 21.09
release. That is the recommended way to integrate the SDK. See under [Integrate
the Swift Package], above.

The following instructions apply to earlier releases of the SDK and are included
here for completeness.

1.  In a web browser, open the Workspace ONE for iOS home page on the VMware
    Code website:  
    [developer.vmware.com/web/sdk/Native/airwatch-ios](https://developer.vmware.com/web/sdk/Native/airwatch-ios).

2.  Follow the instructions for downloading the Workspace ONE SDK for iOS
    archive, which is a DMG file.

3.  Unzip the DMG file and locate the `AWSDK.xcframework` file.

4.  Open your app project in Xcode.

5.  Navigate to the app target, select the General tab and scroll down to the
    Frameworks, Libraries, and Embedded Content section.

6.  Drag and drop AWSDK.xcframework from the archive into your app target
    Frameworks, Libraries, and Embedded Content section.

    This action also adds the AWSDK.xcframework into the target Build Phases, in
    the Link Binary with Libraries section.

This completes integration of the Workspace ONE for iOS Swift package. You are
now ready for the next step, to [Build the SDK example application].

# Add the SDK example application to the management console
Adding the SDK example application to your Workspace ONE management console is
the next step after setting up the app project. If you haven't set up the
project yet, do so now. See under [Set up the SDK example application], above.

Proceed as follows.

1.  Build app and generate an IPA file in the usual way.

2.  Upload the IPA file as a new app to your Workspace ONE Unified Endpoint
    Manager (UEM) console. You will need some UEM administrator privileges.
 
    **Note**: Uploading the IPA file is a way to make the UEM recogize your app
    as one that is allowed access to enterprise resources.

3.  Configure the default SDK profile or create a custom SDK profile in the UEM
    console to set the required configurations for the app.

    Several of the ViewController classes in the example app check for SDK
    profile settings and demonstrate their configuration in the UEM console.

4.  Assign the app to the required users, for example your developer user. You
    can use Smart Group assignment for this.

5.  Save and Publish the changes.

This completes adding the SDK example application to the management console. You
are now ready for the next step, to
[Run the SDK example application on a device].

# Run the SDK example application on a device
Running the SDK example application is the next step after adding the app to
your Workspace ONE management console. If you haven't added the app to your
console, do so now. See under
[Add the SDK example application to the management console] above.

You can install the app onto your device using Workspace ONE, or install from
Xcode. In either case, the following conditions apply.

-   The device must be enrolled against a Workspace ONE management console.

-   The app must already have been uploaded to the UEM console or be recognized
    in some other way.

-   The app must be assigned to the device and end user.

-   A physical device must be used. The app isn't compatible with the iOS
    Simulator that comes with Xcode for example.

# Next Steps
You can use the SDK example application for iOS as the starting point for your
own Workspace ONE mobile application. You can also refer to the source code for
examples of how to utilize Workspace ONE features.

# Developer Resources
For the developer guide and other documentation, see the Workspace ONE for iOS home page on the VMware Code website, here:  
[developer.vmware.com/web/sdk/Native/airwatch-ios](https://developer.vmware.com/web/sdk/Native/airwatch-ios).

You can file support requests in VMware Customer Connect. See the following page
for instructions:  
[https://kb.vmware.com/s/article/2006985](https://kb.vmware.com/s/article/2006985)

[Code in this directory](CustomHTTPProtocol/) is based on Apple sample code.
The original code is published on the Apple website, [Apple sample code] (https://developer.apple.com/library/archive/samplecode/CustomHTTPProtocol/Introduction/Intro.html).

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause
