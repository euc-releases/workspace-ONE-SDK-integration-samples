// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using Android.App;
using Android.OS;
using Android.Views;
using Android.Widget;
using Android.Webkit;
using Android.Util;

using Com.Airwatch.Gateway.UI;
using Com.Airwatch.Gateway.Clients;

using Java.IO;
using System.Threading.Tasks;
using Android.Text;
using Android.Net.Http;
using Javax.Net.Ssl;
using Java.Security.Cert;
using Java.Net;
using Square.OkHttp3;

namespace XamarinAndroidSampleApp.IntegratedAuth
{
    [Activity(Label = "IntegratedAuthenticationActivity")]
    public class IntegratedAuthenticationActivity : GatewayBaseActivity
    {
        private const string TAG = "IntegratedAuthenticationActivity";

        private EditText mUrlEditText;
        private TextView mResponseText;
		private AWWebView mAWWebView;
		private Button mAWWebViewButton;
        private Button mNtlmHttpUrlConnectionButton;
		private Button mAWOkHttpClientButton;
        private ProgressDialog mProgressDialog;
        private string mResponse;
        
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.IntegratedAuthLayout);

            mUrlEditText = FindViewById<EditText>(Resource.Id.url_text);
            mResponseText = FindViewById<TextView>(Resource.Id.response_text);
			mAWWebViewButton = FindViewById<Button>(Resource.Id.aw_webview_button);
            mAWWebViewButton.Click += OnButtonClick;
            mNtlmHttpUrlConnectionButton = FindViewById<Button>(Resource.Id.ntlm_url_connection);
            mNtlmHttpUrlConnectionButton.Click += OnButtonClick;
			mAWOkHttpClientButton = FindViewById<Button>(Resource.Id.aw_ok_http_client);
            mAWOkHttpClientButton.Click += OnButtonClick;

            mAWWebView = FindViewById<AWWebView>(Resource.Id.aw_webview);
            // neglect ssl errors
            mAWWebView.SetWebViewClient(new AllowAllWebViewClient());
        }

        async void OnButtonClick(object sender, EventArgs e)
        {
            if (TextUtils.IsEmpty(mUrlEditText.Text))
            {
                Toast.MakeText(this, "Please Enter URL", ToastLength.Short).Show();
                return;
            }
            mResponseText.Text = "No Status Available";

            mProgressDialog = ProgressDialog.Show(this, "Checking", "Please Wait...", true);

            string urlText = mUrlEditText.Text;
            if ((!urlText.StartsWith("http://")) && (!urlText.StartsWith("https://")))
            {
                urlText = "http://" + urlText;
                mUrlEditText.Text = urlText;
            }

            Button button = (Button)sender;
			if (button == mAWWebViewButton)
            {
                mResponseText.Visibility = ViewStates.Invisible;
                mAWWebView.Visibility = ViewStates.Visible;
                mAWWebView.LoadUrl(urlText);
            }
            else if (button == mNtlmHttpUrlConnectionButton)
            {
                mAWWebView.Visibility = ViewStates.Invisible;
                mResponseText.Visibility = ViewStates.Visible;
                mResponse = await Task.Run<string>(() => LoadNtlmHttpUrlConnectionAsync(urlText));
                mResponseText.Text = mResponse;
            }
            else if (button == mAWOkHttpClientButton)
            {
                mResponseText.Visibility = ViewStates.Invisible;
                mAWWebView.Visibility = ViewStates.Visible;
                mResponse = await Task.Run<string>(() => LoadAWOkHttpClientAsync(urlText));
                mResponseText.Text = mResponse;
            }

            if (mProgressDialog != null)
            {
                mProgressDialog.Hide();
            }

        }

        string LoadNtlmHttpUrlConnectionAsync(string urlText)
        {
            URL url;
            NtlmHttpURLConnection ntlmConnection = null;
            try
            {
                url = new URL(urlText);
                // Trust manager that does not validate certificate chains; ONLY for testing purpose
                AllowAllHosts();
                ntlmConnection = new NtlmHttpURLConnection((HttpURLConnection)AWUrlConnection.OpenConnection(url));
                ntlmConnection.UseCaches = false;
                string response = "Response Code: " + ntlmConnection.ResponseCode;
                return response;
            }
            catch (IOException e)
            {
                return "IO Exception during network request";
            }
            finally
            {
                if (ntlmConnection != null)
                {
                    ntlmConnection.Disconnect();
                }
            }
        }

        string LoadAWOkHttpClientAsync(string urlText)
        {
            Response response = null;
            try
            {
                OkHttpClient clientOrg = new OkHttpClient();
                OkHttpClient client = AWOkHttpClient.CopyWithDefaults(clientOrg, new AllowAllTrustManager());
                Request request = new Request.Builder()
                        .Url(urlText)
                        .Build();

                response = client.NewCall(request).Execute();
                string responseCode = "Response Code: " + response.Code();
                return responseCode;
            }
            catch (IOException e)
            {
                return "IO Exception during network request";
            }
            finally
            {
                if (response != null)
                {
                    response.Body().Close();
                }
            }
        }

        private void AllowAllHosts()
        {
            // Setting TrustAll trust manager and AllowAllHostNameVerifier
            ITrustManager[] trustAllCerts = new ITrustManager[] { new AllowAllTrustManager() };
            try
            {
                SSLContext sc = SSLContext.GetInstance("SSL");
                sc.Init(null, trustAllCerts, new Java.Security.SecureRandom());
                HttpsURLConnection.DefaultSSLSocketFactory = sc.SocketFactory;
                HttpsURLConnection.DefaultHostnameVerifier = new AllowAllHostNameVerifier();
            }
            catch (Exception e)
            {
                Log.Error(TAG, "Exception during setting TrustAll key manager", e);
            }
        }
    }

    public class AllowAllWebViewClient : AWWebViewClient
    {
        public override void OnReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
        {
            handler.Proceed();
        }
    }

    public class AllowAllTrustManager : Java.Lang.Object, IX509TrustManager
    {
        public void CheckClientTrusted(X509Certificate[] chain, string authType)
        {
            // we are the client
        }

        public void CheckServerTrusted(X509Certificate[] chain, string authType)
        {
            // don't do any verification
            // all certificates are valid
        }

        public X509Certificate[] GetAcceptedIssuers()
        {
            // we are not the server
            return null;
        }
    }

    public class AllowAllHostNameVerifier : Java.Lang.Object, IHostnameVerifier
    {
        public bool Verify(string hostname, ISSLSession session)
        {
            return true;
        }
    }

}   