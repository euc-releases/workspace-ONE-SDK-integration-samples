// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace XamarinAndroidSampleApp.Landing
{
    class SdkUseCaseAdapter : BaseAdapter<SdkUseCase>
    {
        List<SdkUseCase> useCases;
        Context context;

        public SdkUseCaseAdapter(Context context, List<SdkUseCase> cases)
        {
            this.context = context;
            this.useCases = cases;
        }


        public override SdkUseCase this[int position]
        {
            get { return useCases[position]; }
        }

        public override long GetItemId(int position)
        {
            return position;
        }

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            var view = convertView;
            SdkUseCaseAdapterViewHolder holder = null;
            SdkUseCase currentCase = useCases[position];

            if (view != null)
                holder = view.Tag as SdkUseCaseAdapterViewHolder;

            if (holder == null)
            {
                holder = new SdkUseCaseAdapterViewHolder();
                var inflater = context.GetSystemService(Context.LayoutInflaterService).JavaCast<LayoutInflater>();
                view = inflater.Inflate(Resource.Layout.SdkUseCaseRowLayout, parent, false);
                holder.PrimaryText = view.FindViewById<TextView>(Resource.Id.use_case_primary_text);
                holder.SecondaryText = view.FindViewById<TextView>(Resource.Id.use_case_secondary_text);
                holder.UseCaseImage = view.FindViewById<ImageView>(Resource.Id.use_case_image);
                view.Tag = holder;
            }


            //fill in your items
            holder.PrimaryText.Text = currentCase.PrimaryText;
            holder.SecondaryText.Text = currentCase.SecondaryText;
            holder.UseCaseImage.SetImageResource(currentCase.ImageResourceId);

            return view;
        }
        
        public override int Count
        {
            get
            {
                return useCases.Count;
            }
        }

    }

    class SdkUseCaseAdapterViewHolder : Java.Lang.Object
    {
        //Your adapter views to re-use
        public TextView PrimaryText { get; set; }
        public TextView SecondaryText { get; set; }
        public ImageView UseCaseImage { get; set; }
    }
}