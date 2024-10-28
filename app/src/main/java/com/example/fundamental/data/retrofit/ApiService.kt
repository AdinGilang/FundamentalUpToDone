package com.example.fundamental.data.retrofit

import com.example.fundamental.data.response.DetailEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events/{id}")
    fun getEventById(@Path("id") id: Int): Call<DetailEventResponse>

    @GET("events")
    fun getEvents(
        @Query("active") active: Int? = null,
        @Query("q") query: String? = null,
        @Query("limit") limit: Int? = null
    ): Call<DetailEventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<DetailEventResponse>
}
