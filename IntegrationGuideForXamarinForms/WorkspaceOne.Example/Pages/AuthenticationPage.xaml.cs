// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace WorkspaceOne.Example.Pages
{
    public partial class AuthenticationPage : ContentPage
    {
        public AuthenticationPage()
        {
            InitializeComponent();
        }

        void GoButton_Clicked(object sender, EventArgs e)
        {
            var url = entry.Text;

            try
            {
                webView.Source = url;
            }
            catch (Exception ex)
            {
                DisplayAlert("Error", ex.Message, "OK");
            }
        }
    }
}
