// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using WorkspaceOne.Forms;
using WorkspaceOne.Forms.Classes;
using WorkspaceOne.Forms.Interfaces;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WorkspaceOne.Example.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class InformationPage : ContentPage
    {
        public ObservableCollection<string> Items { get; set; }
        private AWLogger logger = new WorkspaceOne.Forms.AWLogger();
        public InformationPage()
        {
            InitializeComponent();

            var wso = DependencyService.Get<IWorkspaceOne>().SharedInstance;
            var not = wso != null ? "not" : "";

            Debug.WriteLine($"[{this.GetType()}] wso is {not} null");
            wso?.sendLogs();




            Items = new ObservableCollection<string>
            {

                $"SDK Version: {wso?.SdkVersion ?? string.Empty}",
                $"Server URL: {wso?.DeviceServicesUrl ?? string.Empty}",
                //Below feilds can be used with Version 2.1.0 and above
                $"Group ID: {wso?.GroupId ?? string.Empty}",
                $"Username: {wso?.EnrollmentUserName ?? string.Empty}"
            };

            MyListView.ItemsSource = Items;


        }
        void OnSendError(object sender, EventArgs args)
        {
            StackFrame callStack = new StackFrame(1, true);
            logger.E("Sending Error Message", callStack.GetMethod().Name, callStack.GetFileName(), (uint)callStack.GetFileLineNumber());
        }

        void OnSendInfo(object sender, EventArgs args)
        {
            StackFrame callStack = new StackFrame(1, true);
            logger.I("Sending Info Message ", callStack.GetMethod().Name, callStack.GetFileName(), (uint)callStack.GetFileLineNumber());
        }

        void OnSendVerbose(object sender, EventArgs args)
        {
            StackFrame callStack = new StackFrame(1, true);
            logger.V("Sending Verbose Message", callStack.GetMethod().Name, callStack.GetFileName(), (uint)callStack.GetFileLineNumber());
        }

        void OnSendWarning(object sender, EventArgs args)
        {
            StackFrame callStack = new StackFrame(1, true);
            logger.W("Sending Warning Message", callStack.GetMethod().Name, callStack.GetFileName(), (uint)callStack.GetFileLineNumber());
        }
    }
}
