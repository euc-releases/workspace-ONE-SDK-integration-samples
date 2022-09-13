// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using WorkspaceOne.Forms;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WorkspaceOne.Example.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DlpPage : ContentPage
    {
        private ObservableCollection<string> _items;
        public ObservableCollection<string> Items
        {
            get { return _items; }
            set { _items = value; OnPropertyChanged(); }
        }

        public DlpPage()
        {
            InitializeComponent();
            BindingContext = this;

            App.ProfilesReceived += HandleProfilesReceived;

           HandleProfilesReceived(null, null);
        }

        private void HandleProfilesReceived(object sender, EventArgs e)
        {
            try
            {
                Debug.WriteLine($"[{this.GetType()}] HandleProfilesReceived(object sender, EventArgs e  1)");
             
                var restrictionPayload = App.Profiles.FirstOrDefault(p => p.ProfileType == AWProfileType.SDKProfile)?.RestrictionsPayload;
                if (restrictionPayload != null)
                {
                    Debug.WriteLine($"[{this.GetType()}] HandleProfilesReceived(object sender, EventArgs e  2)");

                    Items = new ObservableCollection<string>
                    {
                        $"Allow Camera: {restrictionPayload.EnableCameraAccess}",
                        $"Allow Copy/Paste Into: {restrictionPayload.EnableCopyAndPasteInTo}",
                        $"Allow Copy/Paste Out: {restrictionPayload.EnableCopyAndPasteOut}",
                        $"Allow Open In: {restrictionPayload.AllowedApplications.Count().ToString()}",
                        $"Enable Watermark: {restrictionPayload.EnableWatermark}",
                        $"Open PDF"
                    };
                }
                else
                {
                    Info.Text = "Restriction Payload is Null, Kindly configure Restriction Payload";
                }
       
                
            }
            catch (ArgumentNullException)
            {
                Items = new ObservableCollection<string>();
            }

            MyListView.ItemsSource = Items;
        }

        async void Handle_ItemTapped(object sender, ItemTappedEventArgs e)
        {
            if (e.Item == null)
                return;

            await DisplayAlert("Item Tapped", "An item was tapped.", "OK");

            //Deselect Item
            ((ListView)sender).SelectedItem = null;
        }
    }
}
