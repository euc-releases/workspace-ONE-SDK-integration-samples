// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using UIKit;
using CoreGraphics;
using System.Net;
using Foundation;

namespace XamarinSampleApp
{
	public partial class TunnelingViewController : UIViewController, INSUrlSessionDelegate, INSUrlSessionTaskDelegate
	{
        UITextField urlTextField = null;
		UIWebView webView = null;

		public TunnelingViewController() : base("TunnelingViewController", null)
		{
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

		void setUpViews()
		{
			this.Title = "Tunneling";
			CGSize mainScreenSize = UIScreen.MainScreen.Bounds.Size;

			float offsetXForUIElements = 10.0f;
			float widthForUIElements = (float)(mainScreenSize.Width - (offsetXForUIElements * 2.0f));
			float btnHeight = 40;

			//uisegmented control
			string[] stringArray = { "UIWebView", "NSUrlSession" };
			CGRect segmentedControlFrame = new CGRect(offsetXForUIElements, 100, widthForUIElements, btnHeight);
			UISegmentedControl segmentedControl = new UISegmentedControl(stringArray);
			segmentedControl.Frame = segmentedControlFrame;
			segmentedControl.SelectedSegment = 0;
			View.AddSubview(segmentedControl);

			//tunnel url text field

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
					NSUrlRequest request = createRequestFromString(urlTextField.Text);
                    if (request != null)
                    {
                        if (segmentedControl.SelectedSegment == 0)
                        {
                            useWebView(request);
                        }
                        else if (segmentedControl.SelectedSegment == 1)
                        {
                            useUrlSession(request);
                        }
                    }
				}
				else
				{
					showAlert("Please enter the URL");
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

        public static HttpStatusCode getStatusCode(Uri url)
        {
            HttpStatusCode result = default(HttpStatusCode);

            var request = WebRequest.Create(url);
            request.Method = "HEAD";
            using (var response = request.GetResponse() as HttpWebResponse)
            {
                if (response != null)
                {
                    result = response.StatusCode;
                    response.Close();
                }
            }

            return result;
        }

        void useWebView(NSUrlRequest request)
        {
            NSUrlProtocol.UnregisterClass(new ObjCRuntime.Class(typeof(CustomUrlProtocol)));

			webView.LoadRequest(request);
			
            //webview events
			webView.LoadStarted += (sender, e) =>
			{
				UIApplication.SharedApplication.NetworkActivityIndicatorVisible = true;
			};

			webView.LoadFinished += (sender, e) =>
			{
				UIApplication.SharedApplication.NetworkActivityIndicatorVisible = false;
			};

			webView.LoadError += (sender, e) =>
			{
                Console.WriteLine("load error {0}", e.ToString());
				UIApplication.SharedApplication.NetworkActivityIndicatorVisible = false;
				var alert = UIAlertController.Create("Load Error", "Error occured while loading the url", UIAlertControllerStyle.Alert);
                alert.AddAction(UIAlertAction.Create("Ok", UIAlertActionStyle.Cancel, null));
				this.PresentViewController(alert, animated: true, completionHandler: null);
			};
        }

		public void useUrlSession(NSUrlRequest request)
		{
			NSUrlProtocol.RegisterClass(new ObjCRuntime.Class(typeof(CustomUrlProtocol)));
			webView.LoadRequest(request);
		}

        public NSUrlRequest createRequestFromString(String url)
        {
			bool containsHttp = url.IndexOf("http", StringComparison.OrdinalIgnoreCase) >= 0;

			//add https if required
			if (!containsHttp)
			{
				url = "https://" + url;
			}
			Uri uri = new Uri(url);

			if (uri.Host != null)
			{
                return (new NSUrlRequest(new Uri(url)));
			}
            else
            {
				Console.WriteLine("url host is null");
                return null;
			}
        }

	}
}

