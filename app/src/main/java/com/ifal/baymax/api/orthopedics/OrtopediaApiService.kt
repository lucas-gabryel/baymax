package com.ifal.baymax.api.orthopedics

import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface OrtopediaApiService {

    @GET("api/interpreter")
    suspend fun getNearbyHospitals(
        @Query("data") overpassQuery: String
    ): OverpassResponse

    companion object {
        private const val BASE_URL = "https://overpass-api.de/"
        fun create(): OrtopediaApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrtopediaApiService::class.java)
        }
    }
}