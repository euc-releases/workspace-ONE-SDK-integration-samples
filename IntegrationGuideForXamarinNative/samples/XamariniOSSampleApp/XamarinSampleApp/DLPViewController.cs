// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause
using UIKit;
using Foundation;
using System;
using CoreGraphics;

namespace XamarinSampleApp
{
	public partial class DLPViewController : UIViewController, IUITableViewDelegate, IUITableViewDataSource
	{
		UITableView tableView;
		AirWatchSDKManager sdkManager = AirWatchSDKManager.sharedInstance;
		string[] supportedElements = new string[] { "Allow Camera: ", "Allow Copy/paste: ", "Allow Open In: ", "Allow Watermark: ", "Open PDF " };
		public DLPViewController() : base("DLPViewController", null)
		{
		}

		public override void ViewDidLoad()
		{
			base.ViewDidLoad();
			setUpViews();
		}

		void setUpViews()
		{
			this.Title = "DLP";
			tableView = new UITableView(View.Bounds);
			tableView.DataSource = this;
			tableView.Delegate = this;
			View.AddSubview(tableView);

			tableView.TableFooterView = new UIView(new CGRect(0, 0, 0, 0));
		}

		public override void DidReceiveMemoryWarning()
		{
			base.DidReceiveMemoryWarning();
		}

		public nint NumberOfSections(UITableView tableView)
		{
			return 1;
		}

		public nint RowsInSection(UITableView tableview, nint section)
		{
			return supportedElements.Length;
		}

		public UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
		{
			string cellIdentifier = "cell";
			UITableViewCell cell = tableView.DequeueReusableCell(cellIdentifier);
			if (cell == null)
				cell = new UITableViewCell(UITableViewCellStyle.Default, cellIdentifier);
			cell.SelectionStyle = UITableViewCellSelectionStyle.None;

			nuint rowNo = (System.nuint)indexPath.Row;
			switch (rowNo)
			{
				case 0:
					cell.TextLabel.Text = string.Concat(supportedElements[rowNo] + sdkManager.allowCamera());
				break;

				case 1:
					cell.TextLabel.Text = string.Concat(supportedElements[rowNo] + sdkManager.allowCopyPaste());
				break;

				case 2:
					cell.TextLabel.Text = string.Concat(supportedElements[rowNo] + sdkManager.restrictDocumentToApps());
				break;

				case 3:
					cell.TextLabel.Text = string.Concat(supportedElements[rowNo] + sdkManager.allowWatermark());
				break;

				case 4:
					cell.SelectionStyle = UITableViewCellSelectionStyle.Gray;
					cell.TextLabel.Text = supportedElements[rowNo];
				break;
					
				default:
					cell.TextLabel.Text = "Title Not Set";
				break;
			}
			return cell;
		}

		//Always include Export attribute before the delegate(if delegate is optional), for optional delegates to get called. Otherwise the delegates wont be called.
		[Export("tableView:didSelectRowAtIndexPath:")]
		public void RowSelected(UITableView tableView, NSIndexPath indexPath)
		{
			tableView.DeselectRow(indexPath, true);
			switch (indexPath.Row)
			{
				case 4:     //OpenIn utility : open document
					AirWatchSDKManager.sharedInstance.openDocumentFromFile("SDKGuide", "pdf");
					break;

				default:
					break;
			}
		}


	}
}

