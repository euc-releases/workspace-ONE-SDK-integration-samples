// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.ComponentModel;
using Android.Content;
using WorkspaceOne.Android.Renderer;
using WorkspaceOne.Example.Controls;
using WorkspaceOne.Example.Droid.Renderers;
using WorkspaceOne.Forms.UI;
using Xamarin.Forms;
using Xamarin.Forms.Platform.Android;

[assembly: ExportRenderer(typeof(BorderlessEntry), typeof(BorderlessEntryRenderer))]
namespace WorkspaceOne.Example.Droid.Renderers
{
    public class BorderlessEntryRenderer : AWEntryRenderer
    {
        #region Constructor

        public BorderlessEntryRenderer(Context context) : base(context)
        {
            AutoPackage = false;
        }

        #endregion

        #region Methods

        protected override void OnElementChanged(ElementChangedEventArgs<AWEntry> e)
        {
            base.OnElementChanged(e);
            if (e.OldElement == null)
            {
                Control.Background = null;
             
            }
        }
        protected override void OnElementPropertyChanged(object sender, PropertyChangedEventArgs e)
        {
            base.OnElementPropertyChanged(sender, e);
        }
        #endregion

    }
    
}

