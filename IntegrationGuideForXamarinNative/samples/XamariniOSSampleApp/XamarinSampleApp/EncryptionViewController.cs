// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause


using UIKit;

namespace XamarinSampleApp
{
    public partial class EncryptionViewController : UIViewController
    {
        public EncryptionViewController() : base("EncryptionViewController", null)
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

		void setUpViews()
		{
			this.Title = "Encryption";
		}
    }
}

