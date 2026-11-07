package com.ifal.baymax.api.orthopedics

import com.google.gson.annotations.SerializedName
data class HospitalElement(
    @SerializedName("place_id")
    val id: Long,
    val name: String?,
    @SerializedName("display_name")
    val displayName: String?,
    val lat: Double,
    val lon: Double
)