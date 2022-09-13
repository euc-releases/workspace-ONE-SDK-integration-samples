// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System.Collections.Generic;

using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace XamarinAndroidSampleApp.Information
{
    class InfoAdapter : BaseAdapter<ItemType>
    {
        List<ItemType> items;
        Context context;

        public InfoAdapter(Context context, List<ItemType> items)
        {
            this.context = context;
            this.items = items;
        }

        public override int ViewTypeCount
        {
            get
            {
                return 2;
            }
        }

        public override int GetItemViewType(int position)
        {
            ItemType item = items[position];
            return item.getType();
        }

        public override long GetItemId(int position)
        {
            return position;
        }

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            var view = convertView;
            int type = GetItemViewType(position);
            // Header view
            if (type == 0)
            {
                if (view == null)
                {
                    var inflater = context.GetSystemService(Context.LayoutInflaterService).JavaCast<LayoutInflater>();
                    view = inflater.Inflate(Resource.Layout.HeaderRowLayout, parent, false);
                }
                TextView header = view.FindViewById<TextView>(Resource.Id.header_text);
                header.Text = ((HeaderItem)items[position]).Heading;
            } else
            {
                if (view == null)
                {
                    var inflater = context.GetSystemService(Context.LayoutInflaterService).JavaCast<LayoutInflater>();
                    view = inflater.Inflate(Resource.Layout.ListItemRowLayout, parent, false);
                }
                TextView setting = view.FindViewById<TextView>(Resource.Id.setting_text);
                TextView value = view.FindViewById<TextView>(Resource.Id.value_text);
                setting.Text = ((ListItem)items[position]).Setting;
                value.Text = ((ListItem)items[position]).Value;
            }

            return view;
        }
        
        public override int Count
        {
            get
            {
                return items.Count;
            }
        }

        public override ItemType this[int position]
        {
            get
            {
                return items[position];
            }
        }
    }
    
}