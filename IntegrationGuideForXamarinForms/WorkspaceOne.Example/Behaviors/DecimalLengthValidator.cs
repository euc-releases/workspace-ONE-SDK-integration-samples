// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

using System;
using System.Diagnostics;
using System.Text.RegularExpressions;
using WorkspaceOne.Example.Controls;
using WorkspaceOne.Forms.UI;
using Xamarin.Forms;

namespace WorkspaceOne.Example.Behaviours
{
    public class DecimalLengthValidator : Behavior<Entry>
    {

        protected override void OnAttachedTo(Entry bindable)
        {
            bindable.TextChanged += bindable_TextChanged;
        }

        private void bindable_TextChanged(object sender, TextChangedEventArgs e)
        {
            Debug.WriteLine(" bindable_TextChanged " + (sender as Entry).Text + "   " + e.OldTextValue);

            string enteredText = (sender as Entry).Text;
            if (enteredText != null)
            {
                string[] splitByDecimal = enteredText.Split('.');

                if (splitByDecimal.Length == 1 && splitByDecimal[0].Length == 6) // no decimal entered yet
                {
                    Debug.WriteLine(" Numberic " + splitByDecimal[0].Length.ToString() + "   "  + e.OldTextValue);
                    if (e.OldTextValue.Contains("."))
                    {

                    }
                    else
                    {
                        (sender as Entry).Text = String.Concat(enteredText, ".");
                    }


                }


                if (splitByDecimal.Length > 1 && splitByDecimal[1].Length > 2)
                {
                    Debug.WriteLine(" Decimal Exceeded  " + splitByDecimal[0].Length.ToString() + "   " + e.OldTextValue);

                    (sender as Entry).Text = enteredText.Remove(enteredText.Length - 1);
                }
                else if (splitByDecimal.Length > 1 && splitByDecimal[1].Length <= 2 && splitByDecimal[0].Length > 6)
                {
                    Debug.WriteLine(" Number Exceeded  " + splitByDecimal[0].Length.ToString() + "   " + splitByDecimal[0] + "  " + splitByDecimal[0].Remove(splitByDecimal[0].Length - 1) + "." + splitByDecimal[1]);
                    (sender as Entry).Text = splitByDecimal[0].Remove(splitByDecimal[0].Length - 1) + "." + splitByDecimal[1];
                }

                if (e.OldTextValue != null && e.OldTextValue.Contains(".") && e.NewTextValue[e.NewTextValue.Length - 1] == '.')
                {
                    (sender as Entry).Text = enteredText.Remove(enteredText.Length - 1);
                }
            }
        }

        protected override void OnDetachingFrom(Entry bindable)
        {
            bindable.TextChanged -= bindable_TextChanged;

        }
    }
}

