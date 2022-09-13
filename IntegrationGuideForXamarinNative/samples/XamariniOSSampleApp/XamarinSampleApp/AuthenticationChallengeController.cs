// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using Foundation;
using UIKit;

namespace XamarinSampleApp
{
    public partial class AuthenticationChallengeController : NSObject
    {
        public Action<NSUrlSessionAuthChallengeDisposition, NSUrlCredential> completionHandler;

        public void createCredAlertForChallenge(NSUrlAuthenticationChallenge challenge)
		{
            InvokeOnMainThread(() =>
            {
                var alertController = UIAlertController.Create("Login", challenge.ProtectionSpace.Realm, UIAlertControllerStyle.Alert);
				UIAlertAction loginAction = UIAlertAction.Create("Submit", UIAlertActionStyle.Default, (UIAlertAction obj) =>
				{
                    string username = alertController.TextFields[0].Text;
                    string password = alertController.TextFields[1].Text;
                    if(username != null && password != null)
                    {
                        useCredentialsForLogin(username, password);
                    }
                    else
                    {
						Console.WriteLine("Cant create creds");
					}
				});
                alertController.AddAction(loginAction);
				alertController.AddAction(UIAlertAction.Create("Cancel", UIAlertActionStyle.Cancel, (UIAlertAction obj) =>
				{
                    completionHandler(NSUrlSessionAuthChallengeDisposition.CancelAuthenticationChallenge, null);
                }));

                alertController.AddTextField(textField =>
                {
                    textField.Placeholder = "Username";
                    textField.ClearButtonMode = UITextFieldViewMode.WhileEditing;
                });

                alertController.AddTextField(textField =>
                {
                    textField.Placeholder = "Password";
                    textField.ClearButtonMode = UITextFieldViewMode.WhileEditing;
                    textField.SecureTextEntry = true;
                });
                XamarinAlertController.showAlertOnTopViewController(alertController);
            });
        }

        public void useCredentialsForLogin(string username, string password)
        {
            var cred = new NSUrlCredential(username, password, NSUrlCredentialPersistence.None);
            completionHandler(NSUrlSessionAuthChallengeDisposition.UseCredential, cred);
        }
	}
}

