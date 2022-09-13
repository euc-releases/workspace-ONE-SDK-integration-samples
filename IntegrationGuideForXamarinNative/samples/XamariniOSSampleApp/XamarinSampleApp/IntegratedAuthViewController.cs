// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using UIKit;
using Foundation;
using CoreGraphics;
using AirWatchSDK;
using CoreFoundation;

namespace XamarinSampleApp
{
	public partial class IntegratedAuthViewController : UIViewController, INSUrlConnectionDelegate, INSUrlConnectionDataDelegate, INSUrlSessionDelegate, INSUrlSessionTaskDelegate
	{
		UITextField urlTextField;
		NSUrlResponse connectionResponse;
		NSMutableData connectionData;
		UIWebView webView;
		private NSUrlSessionDataTask dataTask;
        const string SDKErrorLoginFailedTitle = "SDK Error";
        const string SDKErrorLoginFailedMessage = "An Error Occured while SDK was trying to perform Integrated Authentication. Please make sure your enrollment credentials have access to this endpoint";
        const string SDKErrorAuthNotSupportedTitle = "Authentication Reqired";
        const string SDKErrorAuthNotSupportedMessage = "Authentication challenge is not supported by the SDK";

        public IntegratedAuthViewController() : base("IntegratedAuthViewController", null)
		{
		}

		public override void ViewWillAppear(bool animated)
		{
			base.ViewWillAppear(animated);
			//remove activity indicator if in case the dismiss from webView was clicked before response events were called
			UIApplication.SharedApplication.NetworkActivityIndicatorVisible = false;
		}

		public override void ViewWillDisappear(bool animated)
		{
			NSUrlProtocol.UnregisterClass(new ObjCRuntime.Class(typeof(CustomUrlProtocol)));
			base.ViewWillDisappear(animated);
		}

		public override void ViewDidLoad()
		{
			base.ViewDidLoad();
			setUpViews();
		}

		public override void DidReceiveMemoryWarning()
		{
			base.DidReceiveMemoryWarning();
			// Release any cached data, images, etc that aren't in use.
		}

		//user defined methods
		void setUpViews()
		{
			this.Title = "Authentication";
			CGSize mainScreenSize = UIScreen.MainScreen.Bounds.Size;
			float btnHeight = 40;
			float offsetXForUIElements = 10.0f;
			float widthForUIElements = (float)(mainScreenSize.Width - (offsetXForUIElements * 2.0f));

			//uisegmented control
			string[] stringArray = { "NSUrlConnection", "NSUrlSession"};
			CGRect segmentedControlFrame = new CGRect(offsetXForUIElements, 100, widthForUIElements, btnHeight);
			UISegmentedControl segmentedControl = new UISegmentedControl(stringArray);
			segmentedControl.Frame = segmentedControlFrame;
			segmentedControl.SelectedSegment = 0;
			View.AddSubview(segmentedControl);

			//url text field
			urlTextField = new UITextField();
			urlTextField.Frame = new CGRect(offsetXForUIElements, 150, widthForUIElements, btnHeight);
			urlTextField.Placeholder = "Enter the url with http/https ";
			urlTextField.BackgroundColor = UIColor.LightGray;
			urlTextField.UserInteractionEnabled = true;
			urlTextField.Layer.CornerRadius = 3.5f;
			urlTextField.Layer.BorderWidth = 1.0f;
			urlTextField.Layer.BorderColor = UIColor.DarkGray.CGColor;

			View.AddSubview(urlTextField);

			//UIBarButtonItem - GO button
			UIBarButtonItem rightBarButtonItem = new UIBarButtonItem("GO", UIBarButtonItemStyle.Done, null);
			rightBarButtonItem.Clicked += (object sender, System.EventArgs e) =>
			{
				urlTextField.ResignFirstResponder();
				if (urlTextField.Text != "")
				{
					var url = new NSUrl(urlTextField.Text);
					NSMutableUrlRequest request = new NSMutableUrlRequest(url);
					if (segmentedControl.SelectedSegment == 0)
					{
						useUrlConnection(request);
					}
					else if (segmentedControl.SelectedSegment == 1)
					{
						useUrlSession(request);
					}
				}
				else
				{
					showAlert("Please enter test URL");
				}
			};
			this.NavigationItem.RightBarButtonItem = rightBarButtonItem;

			//uiwebview
			CGRect webViewFrame = new CGRect(offsetXForUIElements, 200, widthForUIElements, 300f);
			webView = new UIWebView(webViewFrame);

			webView.BackgroundColor = UIColor.White;
			webView.Layer.CornerRadius = 10;
			webView.Layer.BorderColor = UIColor.Black.CGColor;
			webView.Layer.BorderWidth = 3.0f;

			View.AddSubview(webView);
		}

		void showAlert(string msg)
		{
			var alert = UIAlertController.Create("Alert", msg, UIAlertControllerStyle.Alert);
			alert.AddAction(UIAlertAction.Create("Ok", UIAlertActionStyle.Cancel, null));
			this.PresentViewController(alert, animated: true, completionHandler: null);
		}

		void useUrlConnection(NSMutableUrlRequest request)
		{
			var connection = new NSUrlConnection(request, this);
			UIApplication.SharedApplication.NetworkActivityIndicatorVisible = true;
			connection.Start();
		}

        public void useUrlSession(NSMutableUrlRequest request)
		{
			NSUrlProtocol.RegisterClass(new ObjCRuntime.Class(typeof(CustomUrlProtocol)));
			webView.LoadRequest(request);
		}

		void handleChallangeforSession(NSUrlAuthenticationChallenge challenge, Action<NSUrlSessionAuthChallengeDisposition, NSUrlCredential> completionHandler)
		{
			NSError outError;
			if (AWController.ClientInstance().CanHandleProtectionSpace(challenge.ProtectionSpace, out outError))
			{
				bool success = AWController.ClientInstance().HandleChallengeForURLSessionChallenge(challenge, completionHandler);
				if (success)
				{
					Console.WriteLine("AWXamarin Client Challenge successful using Session");
				}
			}
		}

		//INSUrlConnectionDelegate
		[Export("connection:willSendRequestForAuthenticationChallenge:")]
		public virtual void WillSendRequestForAuthenticationChallenge(NSUrlConnection connection, NSUrlAuthenticationChallenge challenge)
		{
			if (challenge.PreviousFailureCount == 2)
			{
				//TODO: update cred
			}
			else if (challenge.PreviousFailureCount > 2)
			{
                //display alert
                XamarinAlertController.showAlertViewForController(this, SDKErrorLoginFailedTitle, SDKErrorLoginFailedMessage);

			}
			else
			{
				//handle challenges
				if (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodServerTrust")
				{
					var cred = new NSUrlCredential(challenge.ProtectionSpace.ServerSecTrust);
					challenge.Sender.UseCredential(cred, challenge);
				}
				else if ((challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodHTTPBasic") || (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodNTLM") || (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodClientCertificate"))
				{
					//handleChallengeForConnection(challenge);
				}
				else
				{
					Console.WriteLine("AWXamarin Authentication challenge is not supported by the SDK");
                    XamarinAlertController.showAlertViewForController(this, SDKErrorAuthNotSupportedTitle, SDKErrorAuthNotSupportedMessage);				}
			}
		}

		[Export("connection:didReceiveResponse:")]
		public virtual void ReceivedResponse(NSUrlConnection connection, NSUrlResponse response)
		{
			this.connectionResponse = response;
			if (connectionData != null)
			{
				connectionData.Length = 0;
			}
			//TODO : UI
		}

		[Export("connection:didReceiveData:")]
		public virtual void ReceivedData(NSUrlConnection connection, NSData data)
		{
			if (connectionData == null)
			{
				connectionData = new NSMutableData();
				connectionData.AppendData(data);
			}
			else
			{
				connectionData.AppendData(data);
			}
		}

		[Export("connectionDidFinishLoading:")]
		public virtual void FinishedLoading(NSUrlConnection connection)
		{
			if (connectionResponse != null && connectionData != null)
			{
				if (connectionResponse.MimeType != null && connectionResponse.TextEncodingName != null && connectionResponse.Url != null)
				{
					webView.LoadData(connectionData, connectionResponse.MimeType, connectionResponse.TextEncodingName, connectionResponse.Url);
				}
				else
				{
					NSString dataString = new NSString(connectionData, NSStringEncoding.UTF8);
					webView.LoadHtmlString(dataString, connectionResponse.Url);
				}
			}

			//TODO : UI
		}

		//NSUrlSession Delegates
		[Export("URLSession:didReceiveChallenge:completionHandler:")]
		public virtual void DidReceiveChallenge(NSUrlSession session, NSUrlAuthenticationChallenge challenge, Action<NSUrlSessionAuthChallengeDisposition, NSUrlCredential> completionHandler)
		{
			if (challenge.PreviousFailureCount == 2)
			{
				//TODO: update UserCreds
			}
			else if (challenge.PreviousFailureCount > 2)
			{
				XamarinAlertController.showAlertViewForController(this, SDKErrorLoginFailedTitle, SDKErrorLoginFailedMessage);
				completionHandler(NSUrlSessionAuthChallengeDisposition.CancelAuthenticationChallenge, null);
			}
			else
			{
				if (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodServerTrust")
				{
					completionHandler(NSUrlSessionAuthChallengeDisposition.PerformDefaultHandling, null);
				}
				else if ((challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodHTTPBasic") || (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodNTLM") || (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodClientCertificate"))
				{
					handleChallangeforSession(challenge, completionHandler);
				}
				else
				{
					completionHandler(NSUrlSessionAuthChallengeDisposition.CancelAuthenticationChallenge, null);
	                XamarinAlertController.showAlertViewForController(this, SDKErrorAuthNotSupportedTitle, SDKErrorAuthNotSupportedMessage);
                }
			}
		}
	}
}

