// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

namespace WorkspaceOne.Example.Model
{
    public class MainMenuItem
    {
        public string Title { get; }
        public string Subtitle { get; }
        public string ImageSource { get; }

        public MainMenuItem(string title, string subtitle, string imageSource)
        {
            Title = title;
            Subtitle = subtitle;
            ImageSource = imageSource;
        }
    }
}
