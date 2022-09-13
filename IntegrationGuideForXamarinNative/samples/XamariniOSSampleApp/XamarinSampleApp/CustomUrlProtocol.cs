// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using UIKit;
using Foundation;
using AirWatchSDK;
using Security;

namespace XamarinSampleApp
{
    public partial class CustomUrlProtocol : NSUrlProtocol, INSUrlSessionDelegate, INSUrlSessionTaskDelegate, INSUrlSessionDataDelegate
    {
        const string SDKErrorLoginFailedTitle = "SDK Error";
        const string SDKErrorLoginFailedMessage = "An Error Occured while SDK was trying to perform Authentication. Please make sure your enrollment credentials have access to this endpoint";
        const string SDKErrorAuthNotSupportedTitle = "Authentication Reqired";
        const string SDKErrorAuthNotSupportedMessage = "Authentication challenge is not supported by the SDK";

        [Export("canInitWithRequest:")]
        public static bool canInitWithRequest(NSUrlRequest request)
        {
            return true;
        }

        [Export("canonicalRequestForRequest:")]
        public static new NSUrlRequest GetCanonicalRequest(NSUrlRequest request)
        {
            return request;
        }

        [Export("initWithRequest:cachedResponse:client:")]
        public CustomUrlProtocol(NSUrlRequest request, NSCachedUrlResponse cachedResponse, INSUrlProtocolClient client)
        : base(request, cachedResponse, client)
        {
        }

        [Export("startLoading")]
        public override void StartLoading()
        {
            var config = NSUrlSession.SharedSession.Configuration;
            var session = NSUrlSession.FromConfiguration(config, this, null);

            //var taskRequest = session.CreateDataTaskAsync(Request, out dataTask);
            //dataTask.Resume();
            var task = session.CreateDataTask(Request);
            task.Resume();
        }

        [Export("stopLoading")]
        public override void StopLoading()
        {
            Console.WriteLine("StopLoading");
        }

        //NSURLSessionTaskDelegate
        [Export("URLSession:task:willPerformHTTPRedirection:newRequest:completionHandler:")]
        public virtual void WillPerformHttpRedirection(NSUrlSession session, NSUrlSessionTask task, NSHttpUrlResponse response, NSUrlRequest newRequest, Action<NSUrlRequest> completionHandler)
        {
            Console.WriteLine("Redirecting request");
            completionHandler(newRequest);
        }

        [Export("URLSession:task:didReceiveChallenge:completionHandler:")]
        public virtual void DidReceiveChallenge(NSUrlSession session, NSUrlSessionTask task, NSUrlAuthenticationChallenge challenge, Action<NSUrlSessionAuthChallengeDisposition, NSUrlCredential> completionHandler)
        {
            string authMethod = challenge.ProtectionSpace.AuthenticationMethod;
            Console.WriteLine("DidReceiveChallenge {0}", authMethod);

            if (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodServerTrust")
            {
                var trustRef = challenge.ProtectionSpace.ServerSecTrust;
                var trustResult = SecTrustResult.Invalid;
                if (trustRef != null)
                {
                    trustResult = trustRef.Evaluate();
                }

                NSUrlCredential cred = NSUrlCredential.FromTrust(trustRef);
                completionHandler(NSUrlSessionAuthChallengeDisposition.UseCredential, cred);

            }
            else if (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodHTTPBasic")
            {
                showAuthenticationViewForChallenge(challenge, completionHandler);
            }
            else if ((challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodNTLM") || (challenge.ProtectionSpace.AuthenticationMethod == "NSURLAuthenticationMethodClientCertificate"))
            {
                handleChallengeforSession(challenge, completionHandler);
            }
            else
            {
                completionHandler(NSUrlSessionAuthChallengeDisposition.CancelAuthenticationChallenge, null);
                //XamarinAlertController.showAlertViewForController(this, SDKErrorAuthNotSupportedTitle, SDKErrorAuthNotSupportedMessage);
            }
        }

        //NSURLSessionDataDelegate
        [Export("URLSession:dataTask:didReceiveResponse:completionHandler:")]
        public virtual void DidReceiveResponse(NSUrlSession session, NSUrlSessionDataTask dataTask, NSUrlResponse response, Action<NSUrlSessionResponseDisposition> completionHandler)
        {
            Console.WriteLine("DidReceiveResponse");
            completionHandler(NSUrlSessionResponseDisposition.Allow);
            this.Client.ReceivedResponse(this, response, NSUrlCacheStoragePolicy.Allowed);
        }

        [Export("URLSession:dataTask:didReceiveData:")]
        public virtual void DidReceiveData(NSUrlSession session, NSUrlSessionDataTask dataTask, NSData data)
        {
            Console.WriteLine("DidReceiveData");
            this.Client.DataLoaded(this, data);
        }

        [Export("URLSession:task:didCompleteWithError:")]
        public virtual void DidCompleteWithError(NSUrlSession session, NSUrlSessionTask task, NSError error)
        {
            Console.WriteLine("DidCompleteWithError");
            if (error != null)
            {
                this.Client.FailedWithError(this, error);
            }
            else
            {
                this.Client.FinishedLoading(this);
            }
        }

        //user method
        void handleChallengeforSession(NSUrlAuthenticationChallenge challenge, Action<NSUrlSessionAuthChallengeDisposition, NSUrlCredential> completionHandler)
        {
            NSError outError;
            if (AWController.ClientInstance().CanHandleProtectionSpace(challenge.ProtectionSpace, out outError))
            {
                bool success = AWController.ClientInstance().HandleChallengeForURLSessionChallenge(challenge, completionHandler);
                if (success)
                {
                    Console.WriteLine("AWXamarin Client Challenge successful using Session");
                }
            }
        }

        void showAuthenticationViewForChallenge(NSUrlAuthenticationChallenge challenge, Action<NSUrlSessionAuthChallengeDisposition, NSUrlCredential> handler)
        {
            AuthenticationChallengeController challengeController = new AuthenticationChallengeController();
            challengeController.completionHandler = handler;
            challengeController.createCredAlertForChallenge(challenge);
		}
    }
}

