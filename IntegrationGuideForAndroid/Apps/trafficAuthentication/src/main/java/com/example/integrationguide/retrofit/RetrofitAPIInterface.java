// Copyright 2018 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPIInterface {

    @GET("/api/users/2")
    Call<User> getUserInfo();
}
