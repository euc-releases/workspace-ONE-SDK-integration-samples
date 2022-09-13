// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

using Com.Airwatch.Sdk;
using Com.Airwatch.Sdk.Profile;
using Com.Airwatch.Sdk.Shareddevice;

namespace XamarinAndroidSampleApp
{
    [Service(Label = "AirWatchSDKIntentService")]
    public class AirWatchSDKIntentService : AirWatchSDKBaseIntentService
    {
        protected override void OnAnchorAppStatusReceived(Context p0, AnchorAppStatus p1)
        {
            // OnAnchorAppStatusReceived
        }

        protected override void OnAnchorAppUpgrade(Context p0, bool p1)
        {
            // On upgrade of AnchorApp
        }

        protected override void OnApplicationConfigurationChange(Bundle p0)
        {
            // onApplicationConfigurationChange called
        }

        protected override void OnApplicationProfileReceived(Context p0, string p1, ApplicationProfile p2)
        {
            // onApplicationProfileReceived
        }

        protected override void OnClearAppDataCommandReceived(Context p0, ClearReasonCode p1)
        {
            // onClearAppDataCommandReceived
        }

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);

            // Create your application here
        }
    }
}