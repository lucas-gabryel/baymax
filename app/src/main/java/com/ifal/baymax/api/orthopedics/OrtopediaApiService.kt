package com.ifal.baymax.api.orthopedics

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface OrtopediaApiService {

    @GET("users")
    suspend fun getSpecialists(): List<OrtopediaSpecialist>

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create(): OrtopediaApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrtopediaApiService::class.java)
        }
    }
}
