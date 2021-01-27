package com.example.tactalk.Retrofit

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface IMyService {
    @POST("register")
    @FormUrlEncoded
    fun registerUser(@Field("name") name: String,
                     @Field("email") email: String,
                     @Field("password") password: String,
                     @Field("confirmPassword") confirmPassword: String): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("name") name: String,
                  @Field("password") password: String): Observable<String>
}