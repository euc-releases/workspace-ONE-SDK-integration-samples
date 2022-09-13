// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using WorkspaceOne.Forms;
using WorkspaceOne.Forms.Interfaces;
using Xamarin.Forms;

namespace WorkspaceOne.Example.Pages
{
    public partial class RemoteConfigPage : ContentPage
    {
        public RemoteConfigPage()
        {
            InitializeComponent();
        }

        protected override void OnAppearing()
        {
            base.OnAppearing();

            try
            {
                if (DependencyService.Get<IWorkspaceOne>() is IWorkspaceOne wso && wso != null)
                {
                    editor.Text = $"{wso.SharedInstance.RemoteConfiguration ?? String.Empty}";
                    Debug.WriteLine($"[RemoteConfigPage] wso.RemoteConfiguration = {wso.SharedInstance.RemoteConfiguration}");
                }
                else
                {
                    Debug.WriteLine($"[RemoteConfigPage] wso.RemoteConfiguration == null");
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine(e);
            }
        }
    }
}
