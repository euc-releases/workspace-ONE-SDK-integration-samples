## Initialize from SwiftUI
If the app user interface is implemented in SwiftUI, you can follow these
instructions to start the
[Task: Initialize the software development kit runtime](../readme.md).
The instructions assume that all dependencies of that task are completed
already.

These instructions are intended to meet general requirements for Workspace ONE
integration. They should be easy to adapt to your own specific requirements if
needed.

Proceed as follows.

### Add a helper class
Start by adding a helper class that can interface between SwiftUI and the
Workspace ONE SDK.

1.  Open the application project in Xcode and add a new Swift class.

    For example by right-clicking on the project and selecting New File... in
    the context menu that appears. Then selecting Swift File in the template
    chooser.

2.  Add the helper class.

    This can be a singleton class. It should conform to these interfaces.

    -   NSObject as usual.
    -   AWControllerDelegate for the SDK side of the interface.
    -   ObservableObject for the SwiftUI side of the interface.

    The top of the class file could look like this.

        import AWSDK

        class WorkspaceONEHelper:
            NSObject, AWControllerDelegate, ObservableObject
        {
            // Singleton class.
            private override init() { super.init() }
            static var shared: WorkspaceONEHelper = .init()

            // Rest of the class will go here.
        }

3.  Implement a helper callback that is invoked when the SDK finishes
    initialization.

    The callback is the `controllerDidFinishInitialCheck(error:)` function and
    is required by the declared SDK protocol.

    You may add `@Published` variables to the helper so that the initialized
    status can be made visible in the app user interface.

    The code could look like this.

        @Published var sdkMessage: String = "SDK uninitialised."
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

4.  Add a helper function to start the SDK, and an instance variable to enforce
    that the SDK is only started once.

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

    4.  Set the controller `delegate` to the helper instance.

    5.  Initialize the SDK by calling the controller `start()` function.

    The code could look like this.

        @Published var appMessage: String = "App uninitialised."
        var sdkStarted = false
        func startSDK()
        {
            appMessage = "startSDK() \(sdkStarted)."
            print(appMessage)

            guard !sdkStarted else { return }
            sdkStarted = true

            // Access the AWController singleton.
            let awController = AWController.clientInstance()

            // Set the callback scheme to the custom URL Type.
            do {
                awController.callbackScheme = try schemeForWS1()
            }
            catch {
                appMessage = "schemeForWS1() failed \(error)."
                print(appMessage)
                return
            }

            // Set the team identifier.
            awController.teamID = "YOUR1TEAM2IDENTIFIER2HERE"

            // Set the controller delegate to the helper instance.
            awController.delegate = self

            // Actually initialize the SDK.
            awController.start()
        }

5.  Add a helper function to pass open URL requests to the SDK handler.

    The SDK will process open URL requests from Hub during enrollment. This is
    discussed in the introduction to the
    [Task: Configure application properties](../../02Task_Configure-application-properties/readme.md).

    The code could look like this.

        func handleOpen(url: URL, fromApplication: String?) -> Bool {
            return AWController.clientInstance()
                .handleOpenURL(url, fromApplication: fromApplication)
        }

Build the application to ensure that no mistakes have been made. Then continue
with the next instructions.

### Connect the helper
Continue by connecting the helper class to the app code and user interface.

These instructions assume your application code has a structure like this.

-   *Content view* struct that conforms to the SwiftUI `View` protocol.
-   *App main* struct that conforms to the SwiftUI `App` protocol and declares
    the Content view in a WindowGroup.

(If your structure is different you can adapt these instructions.)

Proceed as follows.

1.  Connect the helper to your Content view.

    Add an `@ObservedObject` variable to the Content view. The variable will be
    an instance of the helper. The code could look like this.

        @ObservedObject var sdkHelper: WorkspaceONEHelper

2.  Add, for example, Text instances to the Content view to show the published
    variables from the helper.

    The code could look like this.

        var body: some View {
            VStack {
                Text(self.sdkHelper.sdkMessage).font(.title2)
                Text(self.sdkHelper.appMessage).font(.title2)
            }
            .padding()
        }

    At this point the app won't build because declaring the observed object
    requires a parameter in the Content view instantiation.

3.  Update the Content view instantiation in your App main.

    Pass the singleton instance of the helper to the Content view. The code
    could look like this.

        ContentView(sdkHelper: WorkspaceONEHelper.shared)

4.  In your App main, connect the helper open URL handler to the Content view.

    Add an `onOpenURL` to the Content view instantiation. The onOpenURL should
    first call the helper function, which returns a Boolean value for whether it
    handled the URL. The code could look like this.

        ContentView(sdkHelper: WorkspaceONEHelper.shared)
            .onOpenURL { url in
                if WorkspaceONEHelper.shared.handleOpenURL(
                    url: url, sourceApplication: nil
                ) { return }
                
                print("URL not handled by WS1 \(url)")
                // App handling of url goes here.
            }

5.  Initialize the SDK from your App main.

    In your App main, add a *scene phase* listener to the WindowGroup. In the
    listener, if the scene phase is active then call the singleton helper
    function to initialize the SDK. The code could look like this.

        @Environment(\.scenePhase) private var scenePhase

        var body: some Scene {
            WindowGroup {
                // Other code was added here in the preceding instructions.
            }
            .onChange(of: scenePhase) {phase in
                if phase == .active {
                    WorkspaceONEHelper.shared.startSDK()
                }
            }
        }

    Note. An alternative to a scene phase listener could be to implement
    `@UIApplicationDelegateAdaptor` and call the SDK initialization helper from
    its `didFinishLaunchingWithOptions` callback. However, that type of adaptor
    shouldn't be used according to the reference documentation. See the Apple
    developer website here for example.  
    [developer.apple.com/documentation/swiftui/uiapplicationdelegateadaptor](https://developer.apple.com/documentation/swiftui/uiapplicationdelegateadaptor)

This completes SDK initialization from SwiftUI. Now follow the instructions to
[Test runtime initialization](../10Test-runtime-Initialization/readme.md).

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause