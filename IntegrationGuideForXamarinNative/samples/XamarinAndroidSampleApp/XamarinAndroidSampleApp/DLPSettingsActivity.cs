// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.Net;
using Android.Runtime;
using Android.Views;
using Android.Widget;

using Com.Airwatch.Dlp.OpenIn;
using Com.Airwatch.Gateway.UI;
using Com.Airwatch.Sdk.Context;
using Com.Airwatch.Sdk.Configuration;
using Com.Airwatch.UI.Widget;
using Android.OS;
using System.IO;
using AndroidX.Core.Content;

namespace XamarinAndroidSampleApp
{
    [Activity(Label = "DLPSettings")]
    public class DLPSettingsActivity : GatewayBaseActivity
    {

        Button mUriOpenButton;
        Button mFileOpenButton;
        EditText mUriPathEditText;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.DLPSettingsLayout);

            var dlpTextView = FindViewById<TextView>(Resource.Id.enable_dlp);
            var screenshotTextView = FindViewById<TextView>(Resource.Id.enable_screenshot);
            var copyPasteTextView = FindViewById<AWTextView>(Resource.Id.enable_copypaste);
            var openWithTextView = FindViewById<TextView>(Resource.Id.enable_openwith);
            mUriPathEditText = FindViewById<EditText>(Resource.Id.open_uri_text);
            mUriOpenButton = FindViewById<Button>(Resource.Id.open_uri_button);
            mUriOpenButton.Click += OnButtonClick;
            mFileOpenButton = FindViewById<Button>(Resource.Id.open_file_button);
            mFileOpenButton.Click += OnButtonClick;

            SDKContext sdkContext = null;
            try
            {
                sdkContext = SDKContextManager.SDKContext;
                if (sdkContext.CurrentState != SDKContext.State.Idle)
                {
                    Bundle bundle = sdkContext.SDKConfiguration.GetSettings(SDKConfigurationKeys.TypeDataLossPrevention);

                    dlpTextView.Text = bundle.GetString(SDKConfigurationKeys.EnableDataLossPrevention);
                    copyPasteTextView.Text = bundle.GetString(SDKConfigurationKeys.EnableCopyPaste);
                    openWithTextView.Text = bundle.GetString(SDKConfigurationKeys.LimitOpenWith);
                    screenshotTextView.Text = bundle.GetString(SDKConfigurationKeys.EnableScreenCapture);
                }
                else
                {
                    dlpTextView.Text = "SDK Context is in IDLE state";
                }

            }
            catch (SDKContextException e)
            {
                dlpTextView.Text = "SDKContextException while fetching DLP settings" + e.Message;
                return;
            }

        }

        async void OnButtonClick(object sender, EventArgs e)
        {
            Button button = (Button)sender;
            Android.Net.Uri uri = null;
            if (button == mUriOpenButton)
            {
                uri = Android.Net.Uri.Parse(mUriPathEditText.Text.ToString());
            } else if (button == mFileOpenButton)
            {
                uri = GetFileUriFromAssets();

            }

            if (uri != null)
            {
                UriOpenerFactory.Instance.OpenUri(this, uri);
            }
        }

        private Android.Net.Uri GetFileUriFromAssets()
        {
            Java.IO.File dir = new Java.IO.File(ApplicationContext.FilesDir, "sharedfiles");
            if (!dir.Exists())
            {
                dir.Mkdir();
            }
            Java.IO.File f = new Java.IO.File(dir, "sample.pdf");
            string filePath = f.AbsolutePath;

            try
            {
                if (!File.Exists(filePath))
                {
                    using (var br = new BinaryReader(ApplicationContext.Assets.Open("AirWatch_brochure.pdf")))
                    {
                        using (var bw = new BinaryWriter(new FileStream(filePath, FileMode.Create)))
                        {
                            byte[] buffer = new byte[2048];
                            int length = 0;
                            while ((length = br.Read(buffer, 0, buffer.Length)) > 0)
                            {
                                bw.Write(buffer, 0, length);
                            }
                        }
                    }
                }
            }
            catch
            {

            }

            return FileProvider.GetUriForFile(this, "com.airwatch.xamarinsampleapp.provider", f);
        }
    }
}