// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause


namespace XamarinAndroidSampleApp.Information
{
    public class ListItem : ItemType
    {
        public string Setting { get; set; }

        public string Value { get; set; }

        public int getType()
        {
            return 1;
        }
    }
}