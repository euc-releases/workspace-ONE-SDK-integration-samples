// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPIInterface {
    @get:GET("/api/users/2")
    val userInfo: Call<User?>?
}