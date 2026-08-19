package com.example.charityapp.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MpesaApi {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    suspend fun generateAccessToken(
        @Header("Authorization") auth: String
    ): Response<AccessTokenResponse>

    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun stkPush(
        @Header("Authorization") auth: String,
        @Body request: StkPushRequest
    ): Response<StkPushResponse>
}
