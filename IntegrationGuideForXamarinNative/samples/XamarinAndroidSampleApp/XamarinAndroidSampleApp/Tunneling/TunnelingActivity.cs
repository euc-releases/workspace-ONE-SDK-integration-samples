// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using Com.Airwatch.Gateway.UI;
using Android.App;
using Android.OS;
using Android.Text;
using Android.Views;
using Android.Webkit;
using Android.Widget;
using System.Threading.Tasks;

using Java.Net;
using Java.IO;
using Square.OkHttp3;
using Com.Airwatch.Gateway.Clients;
using XamarinAndroidSampleApp.IntegratedAuth;
using Javax.Net.Ssl;
using Android.Util;

namespace XamarinAndroidSampleApp.Tunneling
{
    
    [Activity(Label = "TunnelingActivity")]
    public class TunnelingActivity : GatewayBaseActivity
    {
        private const string TAG = "TunnelingActivity";
        private EditText mUrlEditText;
        private TextView mResponseText;
        private AWWebView mAWWebView;
        private Button mWebViewButton;
        private Button mUrlConnectionButton;
        private Button mOkHttpClientButton;
        private ProgressDialog mProgressDialog;
        private string mResponse;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.TunnelingLayout);

            mUrlEditText = FindViewById<EditText>(Resource.Id.url_text);
            mResponseText = FindViewById<TextView>(Resource.Id.response_text);
            mAWWebView = FindViewById<AWWebView>(Resource.Id.webview);
            mWebViewButton = FindViewById<Button>(Resource.Id.webview_button);
            mWebViewButton.Click += OnButtonClick;
            mUrlConnectionButton = FindViewById<Button>(Resource.Id.url_connection);
            mUrlConnectionButton.Click += OnButtonClick;
            mOkHttpClientButton = FindViewById<Button>(Resource.Id.ok_http_client);
            mOkHttpClientButton.Click += OnButtonClick;
            mAWWebView.SetWebViewClient(new AWWebViewClient());
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
            if (button == mWebViewButton)
            {
                mResponseText.Visibility = ViewStates.Invisible;
                mAWWebView.Visibility = ViewStates.Visible;
                mAWWebView.LoadUrl(urlText);
            }
            else if (button == mUrlConnectionButton)
            {
                mAWWebView.Visibility = ViewStates.Invisible;
                mResponseText.Visibility = ViewStates.Visible;
                mResponse = await Task.Run<string>(() => LoadUrlConnectionAsync(urlText));
                mResponseText.Text = mResponse;
            }
            else if (button == mOkHttpClientButton)
            {
                mAWWebView.Visibility = ViewStates.Invisible;
                mResponseText.Visibility = ViewStates.Visible;
                mResponse = await Task.Run<string>(() => LoadOkHttpClientAsync(urlText));
                mResponseText.Text = mResponse;
            }

            if (mProgressDialog != null)
            {
                mProgressDialog.Hide();
            }
        }

        string LoadUrlConnectionAsync(string urlText)
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

        string LoadOkHttpClientAsync(string urlText)
        {
            Response response = null;
            try
            {
                OkHttpClient clientOrg = new OkHttpClient();
                OkHttpClient client = AWOkHttpClient.CopyWithDefaults(clientOrg);
                Request request = new Request.Builder()
                    .Url(urlText)
                    .Build();

                response = client.NewCall(request).Execute();
                string responseCode = "Response Code: " + response.Code();
                return responseCode;
            } catch (IOException e)
            {
                return "IO Exception during network request";
            } finally
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
}