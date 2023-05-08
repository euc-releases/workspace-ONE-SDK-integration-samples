## Initialize from Storyboard
If the app user interface is implemented in a Storyboard, you can follow these
instructions to start the
[Task: Initialize the software development kit runtime](../readme.md).
The instructions assume that all dependencies of that task are completed
already.

These instructions are intended to meet general requirements for Workspace ONE
integration. They should be easy to adapt to your own specific requirements if
needed.

Proceed as follows.

### Update the app delegate
Start by extending your app delegate class so that it will start and control the
Workspace ONE SDK.

1. Open the application project in Xcode and then the class that implements the
   app delegate.

    By default the app delegate class is named `AppDelegate` and will be in the
    `AppDelegate.swift` file.

    The required class will implement the `UIApplicationDelegate` protocol and
    might be annotated as the main entry point of the app.

2.  Declare the app delegate as implementing the Workspace ONE SDK controller
    delegate protocol.

    To do that
    
    -   add an import statement for `AWSDK`
    -   add the `AWControllerDelegate` protocol to the app delegate's
        declaration.

    The top of the class file could look like this.

        import UIKit
        import AWSDK

        @main
        class AppDelegate: UIResponder, UIApplicationDelegate, AWControllerDelegate {

            // Rest of the class here.
        
        }

3.  Add code to start the SDK when the app is launched.

    To start the SDK, do the following.

    1.  Access the AWController singleton through the `clientInstance()` class
        function.

    2.  Set the `callbackScheme` to the URL Type added in
        the [Declare a custom URL scheme](../../02Task_Configure-application-properties/01Declare-a-custom-URL-scheme/readme.md)
        instructions.

        You can do this by adding code to read the app properties. For an
        example see the
        [Appendix: Callback Scheme Sample Code](../../21Appendix_Callback-Scheme-Sample-Code/readme.md).

    3.  Set the `teamID` to the team identifier used to build the app.

        Instructions for locating the required value can be found on the Apple
        developer website, for example here.  
        [developer.apple.com/help/account/manage-your-team/locate-your-team-id](https://developer.apple.com/help/account/manage-your-team/locate-your-team-id)

    4.  Set the controller `delegate` to the app delegate instance.

    5.  Initialize the SDK by calling the controller `start()` function.

    In case you want to display a status message, you can add a suitable
    variable that can be observed from another class.

    The code could look like this.

        @objc dynamic var appMessage = "App uninitialised."

        func application(
            _ application: UIApplication,
            didFinishLaunchingWithOptions launchOptions: [
                UIApplication.LaunchOptionsKey: Any
            ]?) -> Bool
        {
            // Override point for customization after application launch.
            
            let awController = AWController.clientInstance()

            // Set the callback scheme to the custom URL Type.
            do {
                awController.callbackScheme = try schemeForWS1()
                appMessage = "schemeForWS1() OK \"\(awController.callbackScheme)\"."
                print(appMessage)
            }
            catch {
                appMessage = "schemeForWS1() failed \(error)."
                print(appMessage)
                return false
            }

            // Set the team identifier.
            awController.teamID = "YOUR1TEAM2IDENTIFIER2HERE"

            // Set the controller delegate to the helper instance.
            awController.delegate = self

            // Actually initialize the SDK.
            awController.start()

            return true
        }

4.  Make the app delegate conform to the Workspace ONE SDK controller delegate
    protocol.

    To conform, implement the callback `controllerDidFinishInitialCheck`. Best
    practice would be to do this in your implementation.

    -   If SDK initialization failed, show an error message and block all or
        part of the app user interface.
    -   If SDK initialization succeeded, don't block the app user interface.

    In case you want to display a status message, you can add a suitable
    variable that can be observed from another class.

    The code could look like this.

        @objc dynamic var sdkMessage = "SDK uninitialised."

        func controllerDidFinishInitialCheck(error: NSError?) {
            // Good spot for a debug checkpoint.
            if let nsError = error {
                sdkMessage = "controllerDidFinishInitialCheck failed \(nsError)."
            }
            else {
                sdkMessage = "controllerDidFinishInitialCheck OK."
            }

            print(sdkMessage)
        }

Build the application to ensure that no mistakes have been made. Then continue
with the next instructions.

### Update the scene delegate
The SDK will process open URL requests from Hub during enrollment. This is
discussed in the introduction to the
[Task: Configure application properties](../../02Task_Configure-application-properties/readme.md).

URL requests will be received in the scene delegate, which must be modified to
convey the required URL requests to the SDK.

Proceed as follows.

1. Open the application project in Xcode and then the class that implements the
   scene delegate.

    By default the scene delegate class is named `SceneDelegate` and will be in
    the `SceneDelegate.swift` file.

    The required class will implement the `UIWindowSceneDelegate` protocol.

2.  Add an open URL handler that conveys requests to the SDK handler.

    To do this, implement the `scene(_:openURLContexts:)` callback. The
    reference documentation for that callback can be found on the Apple
    developer website, for example here.  
    [developer.apple.com/.../uiscenedelegate/...](https://developer.apple.com/documentation/uikit/uiscenedelegate/3238059-scene)

    To convey open URL requests, do the following.

    1.  Add an import statement for `AWSDK`.
    2.  Access the AWController singleton through the `clientInstance()` class
        function.
    3.  Invoke the `handleOpenURL()` method for each URL context received.
    4.  If the method returns true, the SDK has handled the open URL request.
    5.  Otherwise, the app should handle the request.

    The code could look like this.

        // Other imports here, such as UIKit
        import AWSDK

        class SceneDelegate: UIResponder, UIWindowSceneDelegate {

            var window: UIWindow?

            func scene(
                _ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>
            ) {
                for context in URLContexts {
                    if AWController.clientInstance().handleOpenURL(
                        context.url, fromApplication: context.options.sourceApplication
                    ) { continue }
                    
                    print("URL not handled by WS1 \(context.url)")
                    // App handling of url goes here.
                }
            }

This completes SDK initialization from Storyboard. Now follow the instructions
to [Test runtime initialization](../10Test-runtime-Initialization/readme.md).

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause