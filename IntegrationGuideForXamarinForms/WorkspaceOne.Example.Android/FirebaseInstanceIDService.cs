// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using Android.App;
using Firebase.Iid;
using Firebase.Messaging;

namespace WorkspaceOne.Example.Droid
{
    [Service]
    public class FirebaseInstanceIDService: FirebaseMessagingService
    {
      
        public override void OnNewToken(string p0)
        {
            System.Console.WriteLine(p0);
            base.OnNewToken(p0);
           
            WorkspaceOne.Android.WorkspaceOne.regisgterToken(p0);

        }

        public override void OnMessageReceived(RemoteMessage p0)
        {
            base.OnMessageReceived(p0);
            var notification = p0.GetNotification();
            if (notification != null)
            {
                WorkspaceOne.Android.WorkspaceOne.processMessage(notification.Title, notification.Body, null);
            }
            
        }
    }
}
