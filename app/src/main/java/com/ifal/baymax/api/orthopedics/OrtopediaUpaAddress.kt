package com.ifal.baymax.api.orthopedics

import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    val elements: List<HospitalElement>
)
data class HospitalElement(
    val id: Long,
    val lat: Double?,
    val lon: Double?,
    val center: OverpassCenter?,
    val tags: HospitalTags?
) {
    val latitude: Double? get() = lat ?: center?.lat
    val longitude: Double? get() = lon ?: center?.lon
}

data class OverpassCenter(
    val lat: Double,
    val lon: Double
)

data class HospitalTags(
    val name: String?,
    @SerializedName("addr:street")
    val street: String?,
    @SerializedName("addr:suburb")
    val suburb: String?
)