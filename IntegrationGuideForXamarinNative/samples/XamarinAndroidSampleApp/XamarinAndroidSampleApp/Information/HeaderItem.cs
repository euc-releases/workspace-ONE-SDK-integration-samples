﻿
// Copyright 2022 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause


namespace XamarinAndroidSampleApp.Information
{
    public class HeaderItem : ItemType
    {
        public string Heading { get; set; }

        public int getType()
        {
            return 0; // to indicate Header
        }
    }
}