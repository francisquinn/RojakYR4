package com.example.tactalk.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://tactalk-rojak.herokuapp.com/"
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface StatsApiService {
    @GET("user/games/updateGame/?dummyData=0&game_id=1&user_id=1")
    suspend fun getProperties(): StatsProperty
}

object StatsApi {
    val retrofitService: StatsApiService by lazy {
        retrofit.create(StatsApiService::class.java)
    }
}