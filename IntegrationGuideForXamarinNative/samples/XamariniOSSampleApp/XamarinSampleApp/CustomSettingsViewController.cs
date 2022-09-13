// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using UIKit;

namespace XamarinSampleApp
{
	public partial class CustomSettingsViewController : UIViewController
	{
		UITextView textView;
		AirWatchSDKManager sdkManager = AirWatchSDKManager.sharedInstance;

		public CustomSettingsViewController() : base("CustomSettingsViewController", null)
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

		public void setUpViews()
		{
			this.Title = "Remote Config";
			textView = new UITextView(View.Bounds);
			View.AddSubview(textView);
			textView.Text = sdkManager.customSettings();
		}
	}
}

