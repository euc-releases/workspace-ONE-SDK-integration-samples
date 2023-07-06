// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using WorkspaceOne.Android;

namespace WorkspaceOne.Example.Droid
{
    [Application]
    public class Application : WorkspaceOneApplication
    {
        public Application(IntPtr javaReference, JniHandleOwnership transfer) : base(javaReference, transfer)
        {
            global::Android.Util.Log.Debug(this.GetType().ToString(), "ctor(IntPtr, JniHandleOwnership)");
        }

        
        public override Intent MainActivityIntent
        {
            get
            {
                return new Intent(AwAppContext, typeof(MainActivity));
            }
        }

        public override void OnCreate()
        {
            base.OnCreate(this);
        }

        public override void OnSSLPinningRequestFailure(string p0, Java.Security.Cert.X509Certificate p1)
        {
            base.OnSSLPinningRequestFailure(p0, p1);
        }

        public override void OnSSLPinningValidationFailure(string p0, Java.Security.Cert.X509Certificate p1)
        {
            base.OnSSLPinningValidationFailure(p0, p1);
        }
    }
}
