package com.example.fundamental.data.response

import com.google.gson.annotations.SerializedName

data class DetailEventResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("event") val event: Event,
    @SerializedName("listEvents") val listEvents: List<ListEventsItem>?
)

data class Event(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("quota") val quota: Int,
    @SerializedName("registrants") val registrants: Int,
    @SerializedName("mediaCover") val mediaCover: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("imageLogo") val imageLogo: String,
    @SerializedName("link") val link: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("beginTime") val beginTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("category") val category: String
)
