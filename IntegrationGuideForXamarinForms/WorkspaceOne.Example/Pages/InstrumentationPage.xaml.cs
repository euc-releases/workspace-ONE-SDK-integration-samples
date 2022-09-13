// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using WorkspaceOne.Forms.Interfaces;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WorkspaceOne.Example.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class InstrumentationPage : ContentPage
    {
        public ObservableCollection<string> Items { get; set; }

        public InstrumentationPage()
        {
            InitializeComponent();

            Items = new ObservableCollection<string>
            {
                $"Initial Check Done: {App.InitialCheckDone}",
                $"Received Profiles: {App.RecievedProfiles}"
            };

            MyListView.ItemsSource = Items;
        }
    }
}
