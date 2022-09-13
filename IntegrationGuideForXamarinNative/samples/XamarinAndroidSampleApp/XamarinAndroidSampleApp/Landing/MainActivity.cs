// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using Android.App;
using Android.Content;
using Android.Widget;
using Android.OS;
using Com.Airwatch.Gateway.UI;
using System.Collections.Generic;
using Com.Airwatch.Gateway.Clients.Utils;
using System;
using System.Threading.Tasks;
using System.Threading;

namespace XamarinAndroidSampleApp.Landing
{
    [Activity(Label = "@string/sdk_use_case", Theme = "@style/AppTheme")]
    public class MainActivity : GatewayBaseActivity
    {
        ListView listView;
        List<SdkUseCase> useCases = new List<SdkUseCase>();

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);
            SetContentView(Resource.Layout.Main);
            listView = FindViewById<ListView>(Resource.Id.listview);

            useCases.Add(new SdkUseCase() { PrimaryText = "Information", SecondaryText = "Provides user related information", ImageResourceId = Resource.Drawable.Info});
            useCases.Add(new SdkUseCase() { PrimaryText = "Tunneling", SecondaryText = "Access your internal websites or APIs", ImageResourceId = Resource.Drawable.Tunnel});
            useCases.Add(new SdkUseCase() { PrimaryText = "Authentication", SecondaryText = "Automated local and network authentication", ImageResourceId = Resource.Drawable.Auth });
            useCases.Add(new SdkUseCase() { PrimaryText = "Remote Config", SecondaryText = "Push configurations and settings to the app", ImageResourceId = Resource.Drawable.Payload });
            useCases.Add(new SdkUseCase() { PrimaryText = "Dlp", SecondaryText = "Data loss prevention capabilities", ImageResourceId = Resource.Drawable.Dlp });
            useCases.Add(new SdkUseCase() { PrimaryText = "Logout", SecondaryText = "Check Logout", ImageResourceId = Resource.Drawable.Dlp });

            listView.Adapter = new SdkUseCaseAdapter(this, useCases);

            listView.ItemClick += OnListItemClick;

        }

        protected void OnListItemClick(object sender, Android.Widget.AdapterView.ItemClickEventArgs e)
        {
            if (e.Position == 0)
            {
                var intent = new Intent(this, typeof(Information.GeneralInfoActivity));
                StartActivity(intent);
            } else if (e.Position == 1)
            {
                var intent = new Intent(this, typeof(Tunneling.TunnelingActivity));
                StartActivity(intent);
            } else if (e.Position == 2)
            {
                var intent = new Intent(this, typeof(IntegratedAuth.IntegratedAuthenticationActivity));
                StartActivity(intent);
            } else if (e.Position == 3)
            {
                var intent = new Intent(this, typeof(CustomSettingsActivity));
                StartActivity(intent);
            } else if (e.Position == 4)
            {
                var intent = new Intent(this, typeof(DLPSettingsActivity));
                StartActivity(intent);
            }
            else if (e.Position == 5)
            {
                new Thread(() =>
                {
                    Thread.CurrentThread.IsBackground = true;
                    IACredentialsManagerFactory.Instance.IaCredentialsManager.PromptAndUpdateCredentials(this, "");
                }).Start();
            }

        }
    }
}