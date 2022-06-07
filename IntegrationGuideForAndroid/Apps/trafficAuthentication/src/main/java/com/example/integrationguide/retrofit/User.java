/*
 * Copyright (c) $2018 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 */

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
