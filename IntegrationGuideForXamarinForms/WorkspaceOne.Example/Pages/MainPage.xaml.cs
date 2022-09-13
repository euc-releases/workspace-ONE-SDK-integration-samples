// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WorkspaceOne.Example.Model;
using WorkspaceOne.Example.Pages;
using WorkspaceOne.Forms.Interfaces;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WorkspaceOne.Example
{
    // Learn more about making custom code visible in the Xamarin.Forms previewer
    // by visiting https://aka.ms/xamarinforms-previewer
    [DesignTimeVisible(false), XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class MainPage : ContentPage
    {
        public ObservableCollection<MainMenuItem> Items { get; set; }

        public MainPage()
        {
            InitializeComponent();

            Items = new ObservableCollection<MainMenuItem>
            {
                new MainMenuItem("Information", "'Provides user related information'", "info"),
                new MainMenuItem("Tunneling", "'Access your internal websites or APIs'", "tunneling"),
                new MainMenuItem("Authentication", "'Automated local and network authentication'", "authentication"),
                new MainMenuItem("Instrumentation", "'Provides logging and nalytics capabilities'", "instrumentation"),
                new MainMenuItem("Remote Config", "'Push configurations and settings to the app'", "remote"),
                new MainMenuItem("DLP", "'Data loss prevention capabilities'", "dlp"),
                new MainMenuItem("Encryption", "'Data encryption and decryption'", "encryption"),
                new MainMenuItem("UI Test Page", "DLP UI Tests Controls", "info"),
                new MainMenuItem("Prompt Update Credentials", "Prompt Update Credentials", "info")
            };

            ListView.ItemsSource = Items;
        }

        async void Handle_ItemTapped(object sender, ItemTappedEventArgs e)
        {
            if (e.Item == null)
            {
                return;
            }

            if (sender is ListView listView && listView.SelectedItem is MainMenuItem item)
            {
                switch (Items.IndexOf(item))
                {
                    case 0:
                        {
                            await Navigation.PushAsync(new InformationPage());
                            break;
                        }
                    case 1:
                        {
                            await Navigation.PushAsync(new TunnelingPage());
                            break;
                        }
                    case 2:
                        {
                            await Navigation.PushAsync(new AuthenticationPage());
                            break;
                        }
                    case 3:
                        {
                            await Navigation.PushAsync(new InstrumentationPage());
                            break;
                        }
                    case 4:
                        {
                            await Navigation.PushAsync(new RemoteConfigPage());
                            break;
                        }
                    case 5:
                        {
                            await Navigation.PushAsync(new DlpPage());
                            break;
                        }
                    case 6:
                        {
                            await Navigation.PushAsync(new EncryptionPage());
                            break;
                        }
                    case 7:
                        {
                            await Navigation.PushAsync(new UITestPage());
                            break;
                        }
                    case 8:
                        {

                            var wso = DependencyService.Get<IWorkspaceOne>().SharedInstance;
                            wso.promptAndUpdateUserCredentials();
                            break;
                        }
                    default:
                        {
                            await DisplayAlert(item.Title, $"The {item.Title.ToLower()} feature is not yet available in the example app", "OK");
                            break;
                        }
                }

            }

            //Deselect Item
            ((ListView)sender).SelectedItem = null;
        }
    }
}
