// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using Foundation;
using UIKit;
using CoreGraphics;

namespace XamarinSampleApp
{
    public partial class SDKUseCaseTableViewCell : UITableViewCell
    {
        public static readonly NSString Key = new NSString("SDKUseCaseTableViewCell");
        public static readonly UINib Nib;
        NSString primaryText = null; 
        NSString secondaryText = null;
        double imageFrameWidth = 70.0;
        double imageFrameLeftMargin = UIScreen.MainScreen.Bounds.Width * 0.02; 

        static SDKUseCaseTableViewCell()
        {
            Nib = UINib.FromName("SDKUseCaseTableViewCell", NSBundle.MainBundle);
        }

        protected SDKUseCaseTableViewCell(IntPtr handle) : base(handle)
        {
            // Note: this .ctor should not contain any initialization logic.
        }

        public SDKUseCaseTableViewCell(NSString reuseIdentifier, UITableViewCellStyle style) : base(style, reuseIdentifier)
        {
        }

        public void setCellInterface(NSString primaryText, NSString secondaryText)
        {
            this.primaryText = primaryText;
            this.secondaryText = secondaryText;
			setUpViews();
		}

        void setUpViews()
        {
            //set image
            CGRect imageFrame = new CGRect(imageFrameLeftMargin, 0.0, imageFrameWidth, imageFrameWidth);
            UIImageView cellImageView = new UIImageView(imageFrame);

            string imageFile = string.Concat("Images/" + primaryText + ".png");
            cellImageView.Image = UIImage.FromFile(imageFile);
            ContentView.AddSubview(cellImageView);

            //set primary text
            this.IndentationLevel = 6;
            this.TextLabel.Text = primaryText;

            //set secondary text
            this.DetailTextLabel.Text = secondaryText;
            this.DetailTextLabel.Lines = 0;
            this.DetailTextLabel.TextColor = new UIColor(0.458f, 0.458f, 0.458f, 1.0f);
        }
    }
}
