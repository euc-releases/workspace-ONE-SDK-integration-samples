// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using AirWatchSDK;
using Foundation;
using UIKit;
using CoreGraphics;

namespace XamarinSampleApp
{
	public partial class SDKLifecycleViewController : UIViewController, IUITableViewDelegate, IUITableViewDataSource
	{
		UITableView tableView;
		AirWatchSDKManager sdkManager = AirWatchSDKManager.sharedInstance;

		public SDKLifecycleViewController() : base("SDKLifecycleViewController", null)
		{
		}

		public override void ViewDidLoad()
		{
			base.ViewDidLoad();
			// Perform any additional setup after loading the view, typically from a nib.
			setUpViews();
		}

		public override void DidReceiveMemoryWarning()
		{
			base.DidReceiveMemoryWarning();
			// Release any cached data, images, etc that aren't in use.
		}

		public void setUpViews()
		{
			this.Title = "Instrumentation";
			tableView = new UITableView(View.Bounds);
			tableView.DataSource = this;
			View.AddSubview(tableView);

			tableView.TableFooterView = new UIView(new CGRect(0, 0, 0, 0));
		}

		public nint NumberOfSections(UITableView tableView)
		{
			return 1;
		}

		public nint RowsInSection(UITableView tableview, nint section)
		{
			return 2;
		}

		public UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
		{
			string cellIdentifier = "cell";
			UITableViewCell cell = tableView.DequeueReusableCell(cellIdentifier);
			if (cell == null)
				cell = new UITableViewCell(UITableViewCellStyle.Default, cellIdentifier);
			cell.SelectionStyle = UITableViewCellSelectionStyle.None;

			switch (indexPath.Row)
			{
				case 0:
					bool checkDone = sdkManager.initialCheckDone;
					cell.TextLabel.Text = string.Concat("Initial Check Done : " + checkDone.ToString());
					break;

				case 1:
					bool recievedProfiles = sdkManager.recievedProfiles;
					cell.TextLabel.Text = string.Concat("Recieved Profiles : " + recievedProfiles.ToString());
				break;

				default:
					cell.TextLabel.Text = "Title Not Set";
					break;
			}
			return cell;
		}
	}
}

