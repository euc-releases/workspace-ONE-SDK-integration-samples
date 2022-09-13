// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Plugin.Hud;
using WorkspaceOne.Forms;
using WorkspaceOne.Forms.Interfaces;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WorkspaceOne.Example
{
    public partial class App : Application, IAWSDKDelegate
    {
        public delegate void ProfilesReceivedEventHandler(object sender, EventArgs e);
        public static event ProfilesReceivedEventHandler ProfilesReceived;

        public static AWProfile[] Profiles { get; private set; }
        public static bool InitialCheckDone { get; internal set; }
        public static bool RecievedProfiles { get; internal set; }

        public App()
        {
            Debug.WriteLine($"[{this.GetType()}] App()");
            InitializeComponent();

            if (DependencyService.Get<IWorkspaceOne>() is IWorkspaceOne ws)
            {
                if (!DesignMode.IsDesignModeEnabled)
                {
                    ws.SharedInstance.FormsDelegate = this;
                    
                    Debug.WriteLine($"[{this.GetType()}] FormsDelegate assigned...");
                }
                else
                {
                    Debug.WriteLine($"[{this.GetType()}] IsDesignModeEnabled = {DesignMode.IsDesignModeEnabled}");
                }
            }
            else
            {
                Debug.WriteLine($"[{this.GetType()}] IWorkspaceOne == null");
            }

            MainPage = new NavigationPage(new MainPage());
        }

        protected override void OnStart()
        {
            // Handle when your app starts
            Debug.WriteLine($"[{this.GetType()}] OnStart()");
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
            Debug.WriteLine($"[{this.GetType()}] OnSleep()");
        }

        protected override void OnResume()
        {
            // Handle when your app resumes
            Debug.WriteLine($"[{this.GetType()}] OnResume()");
            
        }

        void IAWSDKDelegate.InitialCheckFinished(bool error)
        {
            Debug.WriteLine($"[{this.GetType()}] InitialCheckFinished()" + error.ToString());
            InitialCheckDone = error;
        }

        void IAWSDKDelegate.ProfilesReceived(AWProfile[] profiles)
        {
            Debug.WriteLine($"[{this.GetType()}] ProfilesReceived(AWProfile[])");
            if (profiles == null)
                return;
            //CrossHud.Current.SetMessage("Profiles received...");
            RecievedProfiles = true;
            Profiles = profiles;
            if (ProfilesReceived != null)
            {
                ProfilesReceived(this, new EventArgs());
            }

            //DismissHudAsync();
        }

        async void DismissHudAsync()
        {
            await Task.Delay(1250);
            CrossHud.Current.Dismiss();
        }

        void IAWSDKDelegate.Wipe()
        {

        }

        void IAWSDKDelegate.Lock()
        {

        }

        void IAWSDKDelegate.Unlock()
        {
            
        }

        void IAWSDKDelegate.StopNetworkActivity(AWNetworkActivityStatus status)
        {

        }

        void IAWSDKDelegate.ResumeNetworkActivity()
        {

        }

        void IAWSDKDelegate.UserChanged()
        {

        }

        void IAWSDKDelegate.EnrollmentStatusReceived(object status)
        {

        }

        public void OnHandleWork()
        {
            Debug.WriteLine($"[{this.GetType()}] OnHandleWork()");
        }

        public void OnAnchorAppUpgrade()
        {
            Debug.WriteLine($"[{this.GetType()}] OnAnchorAppUpgrade()");
        }

        public void OnAnchorAppStatusReceived()
        {
            Debug.WriteLine($"[{this.GetType()}] OnAnchorAppStatusReceived()");
        }

        public void OnApplicationConfigurationChange()
        {
            Debug.WriteLine($"[{this.GetType()}] OnApplicationConfigurationChange()");
        }

        public void ApplicationProfileReceived(AWApplicationProfile profile)
        {
            Debug.WriteLine($"[{this.GetType()}] ApplicationProfileReceived()");
        }
    }
}
