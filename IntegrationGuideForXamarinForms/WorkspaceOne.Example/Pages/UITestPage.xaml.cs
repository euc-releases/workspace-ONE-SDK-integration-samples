// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Runtime.CompilerServices;
using System.Windows.Input;
using Xamarin.Forms;

namespace WorkspaceOne.Example
{
    public partial class UITestPage : ContentPage
    {
        public UITestPage()
        {
            InitializeComponent();
            BindingContext = new MyViewModel();
            openUri.Clicked += OpenUri_Clicked;
        }

        private void OpenUri_Clicked(object sender, EventArgs e)
        {
            
            WorkspaceOne.Forms.Device.OpenUri(new Uri("https://www.google.com"));
        }
         
    }
    public class MyViewModel : INotifyPropertyChanged
    {
        private int i;
        public ICommand ChangeTextCmd { get; set; }
        public ICommand ChangePasswordCmd { get; set; }
        private string _myText;
        private bool _isSecure ;
        public string MyText
        {
            get => _myText;
            set
            {
                Debug.WriteLine("Inside MyText + " + value);
                _myText = value;
                OnPropertyChanged("MyText");

            }
        }

        public bool IsSecure
        {
            get => _isSecure;
            set
            {
                _isSecure = value;
                OnPropertyChanged("IsSecure");
            }
        }
        public MyViewModel()
        {
            i = 0;
            MyText = "Here is my TextBox";
            IsSecure = true;
            ChangeTextCmd = new Command(ChangeText);
            ChangePasswordCmd = new Command(ChangePassword);

        }
        private void ChangeText()
        {
            MyText = "Hi I am new text" + i;
            i++;
        }
        private void ChangePassword()
        {
            Debug.WriteLine("Inside ChangePassword + " + IsSecure.ToString());
            IsSecure = !IsSecure;

            Debug.WriteLine("Inside ChangePassword + " + IsSecure.ToString());
        }
        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            Debug.WriteLine("Inside OnPropertyChanged + " + propertyName);
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }

}
