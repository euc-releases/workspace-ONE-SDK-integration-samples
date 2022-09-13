// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using System;
using System.Collections.Generic;
using System.ComponentModel;
using WorkspaceOne.Forms.Exceptions;
using WorkspaceOne.Forms.Interfaces;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WorkspaceOne.Example.Pages
{
    [DesignTimeVisible(false), XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class EncryptionPage : ContentPage
    {
        readonly IWorkspaceOne ws;

        public EncryptionPage()
        {
            InitializeComponent();

            plain_text.TextChanged += HandlePlainTextChanged;
            text_to_decrypt.TextChanged += HandleTextToDecryptChanged;
            copy_button.Clicked += HandleCopyButtonPressed;

            ws = DependencyService.Get<IWorkspaceOne>().SharedInstance;
        }

        private void HandleCopyButtonPressed(object sender, EventArgs e)
        {
            text_to_decrypt.Text = encrypted_text.Text;
        }

        private void HandlePlainTextChanged(object sender, TextChangedEventArgs e)
        {
            try
            {
                if (ws.EncodeAndEncrypt(e.NewTextValue) is string encryptedText)
                {
                    encrypted_text.Text = encryptedText;
                }
            }
            catch (AirWatchSDKException)
            {
                encrypted_text.Text = string.Empty;
            }
        }

        private void HandleTextToDecryptChanged(object sender, TextChangedEventArgs e)
        {
            try
            {
                if (ws.DecodeAndDecrypt(e.NewTextValue) is string decryptedText)
                {
                    decrypted_text.Text = decryptedText;
                }
            }
            catch (AirWatchSDKException)
            {
                decrypted_text.Text = string.Empty;
            }
        }
    }
}
