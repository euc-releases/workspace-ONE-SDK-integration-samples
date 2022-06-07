/*
 * Copyright (c) $2018 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 */

package com.example.integrationguide.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPIInterface {

    @GET("/api/users/2")
    Call<User> getUserInfo();
}
