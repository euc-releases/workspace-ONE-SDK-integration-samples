﻿// Copyright 2022 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

using UIKit;

namespace WorkspaceOne.Example.iOS
{
    public class Application
    {
        // This is the main entry point of the application.
        static void Main(string[] args)
        {
            // if you want to use a different Application Delegate class from "AppDelegate"
            // you can specify it here.
            UIApplication.Main(args, null, "AppDelegate");
        }
    }
}
