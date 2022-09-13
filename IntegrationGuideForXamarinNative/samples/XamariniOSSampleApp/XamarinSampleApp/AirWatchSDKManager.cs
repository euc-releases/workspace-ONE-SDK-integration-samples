// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using Foundation;
using AirWatchSDK;
using System;
using CoreGraphics;
using UIKit;

namespace XamarinSampleApp
{
	public class AirWatchSDKManager : AWSDKDelegate, IUIDocumentInteractionControllerDelegate
	{
		const string LogCategory = "AirWatchSDKManager";

		private static AirWatchSDKManager instance;
		public AWProfile sdkProfile = null;
		public bool initialCheckDone = false;
		public bool recievedProfiles = false;

		// private, use the Instance below
		private AirWatchSDKManager()
		{
		}

		// singleton pattern
		public static AirWatchSDKManager sharedInstance
		{
			get
			{
				if (instance == null)
				{
					instance = new AirWatchSDKManager();
				}
				return instance;
			}
		}

		override public void InitialCheckDoneWithError(NSError error)
		{
			string message = String.Format("AWXamarin InitialCheckDoneWithError received {0}, SDK initialized without error", error);
			Console.WriteLine(message, LogCategory);
			//Post notification if error occurs, to remove the loader. In case of no error, loader will be removed after profiles are recieved.
			if (error != null)
			{
				NSNotificationCenter.DefaultCenter.PostNotificationName(Constants.InitialCheckDoneWithErrorNotification, null);
			}
			else
			{
				initialCheckDone = true;
			}
		}

		override public void ReceivedProfiles(AWProfile[] profiles)
		{
			recievedProfiles = true;
			string message = String.Format("AWXamarin ReceivedProfiles received {0}", profiles.ToString());
			Console.WriteLine(message, LogCategory);

			NSNotificationCenter.DefaultCenter.PostNotificationName(Constants.ReceivedProfilesNotification, null);

			for (int i = 0; i < profiles.Length; i++)
			{
				AWProfile profile = (AWProfile)profiles[i];
				if (profile.IsSDKProfile)
				{
					sdkProfile = profile;
				}
			}
		}

		override public void Wipe()
		{
			// Add any SDK Customization here
			Console.WriteLine("AWXamarin Wipe command received", LogCategory);
		}

		override public void Lock()
		{
			// Add any SDK Customization here
			Console.WriteLine("AWXamarin Lock command received", LogCategory);
		}

		override public void Unlock()
		{
			// Add any SDK Customization here
			Console.WriteLine("AWXamarin Unlock command received", LogCategory);
		}

		override public void StopNetworkActivity(AWNetworkActivityStatus networkActivityStatus)
		{
			// Add any SDK Customization here
			string message = String.Format("AWXamarin StopNetworkActivity received {0}", networkActivityStatus);
			Console.WriteLine(message, LogCategory);
		}

		override public void ResumeNetworkActivity()
		{
			// Add any SDK Customization here
			Console.WriteLine("AWXamarin ResumeNetworkActivity received", LogCategory);
		}

		public bool DLPEnabled()
		{
			bool DLPPersmission = false;
			AWRestrictionsPayload restrictionsPayload = sdkProfile.RestrictionsPayload;
			if (sdkProfile != null && restrictionsPayload != null)
			{
				DLPPersmission = restrictionsPayload.EnableDataLossPrevention;
			}
			return DLPPersmission;

		}
		// Returns boolean signifying if copy paste operation is allowed or not. Can be configured under DLP Settings in Settings
		public bool allowCopyPaste()
		{
			bool copyPastePermission = false;
			if (sdkProfile != null && sdkProfile.RestrictionsPayload != null)
			{
				AWRestrictionsPayload restrictionsPayload = sdkProfile.RestrictionsPayload;
				copyPastePermission = !(restrictionsPayload.PreventCopyAndPasteInTo);
			}
			return copyPastePermission;
		}

		// Returns boolean signifying if document opening is allowed in specific apps or not. If checked then list of allowed apps can be obtained from allowedApplicationsList()
		public bool restrictDocumentToApps()
		{
			bool restrictApps = false;
			if (sdkProfile != null && sdkProfile.RestrictionsPayload != null)
			{
				AWRestrictionsPayload restrictionsPayload = sdkProfile.RestrictionsPayload;
				restrictApps = restrictionsPayload.RestrictDocumentToApps;
			}
			return restrictApps;
		}

		// Returns a list of allowed application list for document opening. Can be configured under DLP Settings in Settings.
		public NSArray allowedApplicationsList()
		{
			NSArray allowedApps = null;
			if (sdkProfile != null && sdkProfile.RestrictionsPayload != null)
			{
				AWRestrictionsPayload restrictionsPayload = sdkProfile.RestrictionsPayload;
				allowedApps = restrictionsPayload.AllowedApplications;
			}
			return allowedApps;
		}

		// Returns boolean signifying if camera access is allowed or not. Can be configured under DLP Settings in Settings.
		public bool allowCamera()
		{
			bool cameraAccess = false;
			if (sdkProfile != null && sdkProfile.RestrictionsPayload != null)
			{
				AWRestrictionsPayload restrictionsPayload = sdkProfile.RestrictionsPayload;
				cameraAccess = (restrictionsPayload.EnableCameraAccess);
			}
			return cameraAccess;
		}

		// Returns boolean signifying if Watermark is allowed or not. 
		public bool allowWatermark()
		{
			bool watermarkEnabled = false;
			if (sdkProfile != null && sdkProfile.RestrictionsPayload != null)
			{
				AWRestrictionsPayload restrictionsPayload = sdkProfile.RestrictionsPayload;
				watermarkEnabled = (restrictionsPayload.EnableWatermark);
			}
			return watermarkEnabled;
		}

		//Returns a string containing Custom settings. Can be set while creating a custom profile which can be assigned as SDK profile.
		public string customSettings()
		{
			string customPayloadSettings = "";
			if (sdkProfile != null && sdkProfile.CustomPayload != null)
			{
				AWCustomPayload customPayload = sdkProfile.CustomPayload;
				customPayloadSettings = customPayload.Settings;
			}
			return customPayloadSettings;
		}

		// Returns Boolean signifying if Integrated Authentication is allowed or not.
		public bool allowIntegratedAuthentication()
		{
			bool iaAllowed = false;
			if (sdkProfile != null && sdkProfile.AuthenticationPayload != null)
			{
				AWAuthenticationPayload authPayload = sdkProfile.AuthenticationPayload;
				iaAllowed = authPayload.EnableIntegratedAuthentication;
			}
			return iaAllowed;
		}

		// Returns the list of allowed sites for Integrated Authentication. Can be configured under Integrated Authentication settings.
		public NSArray allowedSitesList()
		{
			NSArray allowedSites = null;
			if (sdkProfile != null && sdkProfile.AuthenticationPayload != null)
			{
				allowedSites = sdkProfile.AuthenticationPayload.AllowedSites;
			}
			return allowedSites;
		}
		UIDocumentInteractionController uIDocumentInteractionController;
		public void openDocumentFromUrl(NSUrl fileUrl, UIView view)
		{
			if (fileUrl != null && view != null)
			{
				
			    CGRect frame = new CGRect(0,0,0,0);
				uIDocumentInteractionController =  UIDocumentInteractionController.FromUrl(fileUrl);
				uIDocumentInteractionController.PresentOpenInMenu(frame, view, true);
            }
			else
			{
				Console.WriteLine("AWXamarin file opening URL or View is null");
			}
		}

		public void openDocumentFromFile(string fileName, string fileExtension)
		{
			if (fileName != null && fileExtension != null)
			{
				NSUrl fileURL = NSBundle.MainBundle.GetUrlForResource(fileName, fileExtension);
				var window = UIApplication.SharedApplication.KeyWindow;
				var view = window.RootViewController.View;
				openDocumentFromUrl(fileURL, view);
			}
			else
			{
				Console.WriteLine("AWXamarin file name or extension not valid");
			}
		}
    }
}