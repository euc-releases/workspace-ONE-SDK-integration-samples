// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Runtime.InteropServices;
using Foundation;
using UIKit;
using UserNotifications;
using WorkspaceOne.Forms.Interfaces;
namespace WorkspaceOne.Example.iOS
{
    // The UIApplicationDelegate for the application. This class is responsible for launching the 
    // User Interface of the application, as well as listening (and optionally responding) to 
    // application events from iOS.
    [Register("AppDelegate")]
    public partial class AppDelegate : global::Xamarin.Forms.Platform.iOS.FormsApplicationDelegate
    {
        //
        // This method is invoked when the application has loaded and is ready to run. In this 
        // method you should instantiate the window, load the UI into it and then make the window
        // visible.
        //
        // You have 17 seconds to return from this method, or iOS will terminate your application.
        //
        public override bool FinishedLaunching(UIApplication app, NSDictionary options)
        {

#if ENABLE_TEST_CLOUD
            Xamarin.Calabash.Start();
#endif
            Exception workspaceOneException = null;

            try
            {
                global::Xamarin.Forms.Forms.Init();
                global::WorkspaceOne.iOS.WorkspaceOne.Init("wsoexample");



            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine($"{this.GetType()} {e}");
                workspaceOneException = e;
            }

            if (UIDevice.CurrentDevice.CheckSystemVersion(10, 0))
            {
                UNUserNotificationCenter.Current.RequestAuthorization(UNAuthorizationOptions.Alert | UNAuthorizationOptions.Badge | UNAuthorizationOptions.Sound,
                                                                        (granted, error) =>
                        InvokeOnMainThread(UIApplication.SharedApplication.RegisterForRemoteNotifications));
            }
            else if (UIDevice.CurrentDevice.CheckSystemVersion(8, 0))
            {
                var pushSettings = UIUserNotificationSettings.GetSettingsForTypes(
                UIUserNotificationType.Alert | UIUserNotificationType.Badge | UIUserNotificationType.Sound,
                new NSSet());

                UIApplication.SharedApplication.RegisterUserNotificationSettings(pushSettings);
                UIApplication.SharedApplication.RegisterForRemoteNotifications();
            }
            else
            {
                UIRemoteNotificationType notificationTypes = UIRemoteNotificationType.Alert | UIRemoteNotificationType.Badge | UIRemoteNotificationType.Sound;
                UIApplication.SharedApplication.RegisterForRemoteNotificationTypes(notificationTypes);
            }

            LoadApplication(new App());

            if (workspaceOneException != null)
            {
                System.Diagnostics.Debug.WriteLine($"Error: {workspaceOneException.GetType()}", workspaceOneException.Message);
            }
            return base.FinishedLaunching(app, options);
        }

        public override void OnActivated(UIApplication uiApplication)
        {
            WorkspaceOne.iOS.WorkspaceOne.OnActivated();
        }

        public override bool HandleOpenURL(UIApplication application, NSUrl url)
        {
            return WorkspaceOne.iOS.WorkspaceOne.HandleOpenUrl(url, "");
        }

        public override void RegisteredForRemoteNotifications(UIApplication application, NSData deviceToken)
        {
            System.Diagnostics.Debug.WriteLine("Token: {0}", deviceToken.ToString());
            byte[] result = new byte[deviceToken.Length];
            Marshal.Copy(deviceToken.Bytes, result, 0, (int)deviceToken.Length);
            var token = BitConverter.ToString(result).Replace("-", ""); //Remove "-" character from token string
            System.Diagnostics.Debug.WriteLine("Token: {0}", token);
            WorkspaceOne.iOS.WorkspaceOne.regisgterToken(token); //Register for Push Notification with SDK
        }

        public override void FailedToRegisterForRemoteNotifications(UIApplication application, NSError error)
        {
            System.Diagnostics.Debug.WriteLine("Token Error: {0}", error.ToString());
        }
    }
}