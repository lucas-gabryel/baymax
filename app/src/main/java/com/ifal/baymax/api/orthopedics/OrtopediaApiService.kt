package com.ifal.baymax.api.orthopedics

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

data class HospitalElement(
    @SerializedName("place_id")
    val id: Long,
    val name: String?,
    @SerializedName("display_name")
    val displayName: String?,
    val lat: Double,
    val lon: Double
)

interface OrtopediaApiService {

    @Headers(
        "User-Agent: BaymaxIFAL/1.0 (https://github.com/lucas-gabryel/baymax)",
        "Accept-Language: pt-BR"
    )
    @GET("search?q=hospital&format=jsonv2&bounded=1&limit=15")
    suspend fun getNearbyHospitals(
        @Query("viewbox") viewbox: String
    ): List<HospitalElement>

    companion object {
        fun create(): OrtopediaApiService {
            return Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrtopediaApiService::class.java)
        }
    }
}