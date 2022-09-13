// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using UIKit;
using CoreGraphics;
using AirWatchSDK;
using Foundation;

namespace XamarinSampleApp
{
	public partial class SDKSettingsTableViewController : UITableViewController, IUITableViewDelegate, IUITableViewDataSource
	{
		AirWatchSDKManager sdkManager = AirWatchSDKManager.sharedInstance;
		UIActivityIndicatorView activityIndicator;
		static nfloat TableViewCellHeight = 80;

        NSDictionary<NSString, NSString>[] sdkUseCasesMatrix = new NSDictionary<NSString, NSString>[]
        {
            new NSDictionary<NSString, NSString>((NSString)"Information", (NSString)"'Provides user related information'"),
	        new NSDictionary<NSString, NSString>((NSString)"Tunneling", (NSString)"'Access your internal websites or APIs'"),
            new NSDictionary<NSString, NSString>((NSString)"Authentication", (NSString)"'Automated local and network authentication'"),
            new NSDictionary<NSString, NSString>((NSString)"Instrumentation", (NSString)"'Provides logging and analytics capabilities'"),
            new NSDictionary<NSString, NSString>((NSString)"Remote Config", (NSString)"'Push configurations and settings to the app'"),
            new NSDictionary<NSString, NSString>((NSString)"DLP", (NSString)"'Data loss prevention capabilities'"),
            //new NSDictionary<NSString, NSString>((NSString)"Encryption", (NSString)"'SDK data encryption APIs'") not in scope for 1.1 release
        };

		public SDKSettingsTableViewController() : base("SDKSettingsTableViewController", null)
		{
		}

		public SDKSettingsTableViewController(IntPtr handle) : base (handle)
		{
		}

		public override void ViewDidLoad()
		{
			base.ViewDidLoad();
			addActivityIndicator();
			NSNotificationCenter.DefaultCenter.AddObserver((NSString)Constants.ReceivedProfilesNotification, reloadTableData);
			NSNotificationCenter.DefaultCenter.AddObserver((NSString)Constants.InitialCheckDoneWithErrorNotification, removeActivityIndicator);
			TableView.TableFooterView = new UIView(new CGRect(0, 0, 0, 0));
		}

		public override void DidReceiveMemoryWarning()
		{
			base.DidReceiveMemoryWarning();
		}

		public override void ViewDidUnload()
		{
			NSNotificationCenter.DefaultCenter.RemoveObserver(this, (NSString)Constants.ReceivedProfilesNotification, null);
			NSNotificationCenter.DefaultCenter.RemoveObserver(this, (NSString)Constants.InitialCheckDoneWithErrorNotification, null);
			base.ViewDidUnload();
		}

		public void addActivityIndicator()
		{
			activityIndicator = new UIActivityIndicatorView();
			activityIndicator.ActivityIndicatorViewStyle = UIActivityIndicatorViewStyle.Gray;
			activityIndicator.Alpha = 1;
			CGSize mainScreenSize = UIScreen.MainScreen.Bounds.Size;
			CGPoint indicatorCenter = new CGPoint((mainScreenSize.Width * 0.5), (mainScreenSize.Height * 0.5));
			activityIndicator.Center = indicatorCenter;
			activityIndicator.Hidden = false;
			activityIndicator.HidesWhenStopped = true;
			View.AddSubview(activityIndicator);
			View.BringSubviewToFront(activityIndicator);
			activityIndicator.StartAnimating();

			View.UserInteractionEnabled = false;
		}

		public void removeActivityIndicator(NSNotification notification)
		{
			View.UserInteractionEnabled = true;

			if (activityIndicator != null)
			{
				activityIndicator.RemoveFromSuperview();
			}
		}

		public void reloadTableData(NSNotification notification)
		{

			new System.Threading.Thread(new System.Threading.ThreadStart(() => {
				InvokeOnMainThread(() => {
					removeActivityIndicator(notification);
					TableView.ReloadData();
				});
			})).Start();
			
		}

        //UITableView Delegates
		public override nint NumberOfSections(UITableView tableView)
		{
			return 1;
		}

		public override nint RowsInSection(UITableView tableView, nint section)
		{
            return sdkUseCasesMatrix.Length;
		}

		public override void RowSelected(UITableView tableView, NSIndexPath indexPath)
		{
			tableView.DeselectRow(indexPath, true);

			switch (indexPath.Row)
			{
				case 0:
					NavigationController.PushViewController(new GeneralInfoViewController(), true);
					break;

				case 1:
					NavigationController.PushViewController(new TunnelingViewController(), true);
				break;

				case 2:
					NavigationController.PushViewController(new IntegratedAuthViewController(), true);
				break;

				case 3:
					NavigationController.PushViewController(new SDKLifecycleViewController(), true);
				break;

				case 4:
					NavigationController.PushViewController(new CustomSettingsViewController(), true);
				break;

				case 5:
					NavigationController.PushViewController(new DLPViewController(), true);
				break;

                case 6:
                    NavigationController.PushViewController(new EncryptionViewController(), true);
                    break;
					
				default:
					break;
			}
		}

		[Export("tableView:heightForRowAtIndexPath:")]
        public override nfloat GetHeightForRow(UITableView tableView, NSIndexPath indexPath)
        {
            return TableViewCellHeight;
        }

        [Export("tableView:cellForRowAtIndexPath:")]
		public override UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
		{
            NSString cellIdentifier = (NSString)"CellId";
			SDKUseCaseTableViewCell cell = (SDKUseCaseTableViewCell)tableView.DequeueReusableCell(cellIdentifier);
			if (cell == null)
            {
                cell = new SDKUseCaseTableViewCell(cellIdentifier, UITableViewCellStyle.Subtitle);
            }

			//cell accessory none when SDK profiles are not recieved
            cell.Accessory = UITableViewCellAccessory.None;
			cell.UserInteractionEnabled = true;

			//change cell accessory to disclosure after recieving sdk profiles
			if (sdkManager.sdkProfile != null)
			{
				cell.Accessory = UITableViewCellAccessory.DisclosureIndicator;
			}

            nuint rowNo = (System.nuint)indexPath.Row;
            cell.setCellInterface(sdkUseCasesMatrix[rowNo].Keys[0], sdkUseCasesMatrix[rowNo].Values[0]);
			return cell;
        }

		void disableCell(UITableViewCell cell)
		{
			cell.Accessory = UITableViewCellAccessory.None;
			cell.SelectionStyle = UITableViewCellSelectionStyle.None;
			cell.UserInteractionEnabled = false;
		}
	}
}

