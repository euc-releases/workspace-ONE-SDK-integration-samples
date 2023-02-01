// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.retrofit;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("data")
    public Data userInfo;
    public class Data {
        @SerializedName("last_name")
        public String lastName;
        @SerializedName("id")
        public String id;
        @SerializedName("avatar")
        public String avatarValue;
        @SerializedName("first_name")
        public String firstName;
        @SerializedName("email")
        public String emailInfo;
    }
}
