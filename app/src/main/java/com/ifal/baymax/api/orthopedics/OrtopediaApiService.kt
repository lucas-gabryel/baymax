package com.ifal.baymax.api.orthopedics

import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.net.Inet4Address

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
            val client = OkHttpClient.Builder()
                .dns { host ->
                    Dns.SYSTEM.lookup(host).filterIsInstance<Inet4Address>()
                        .ifEmpty { Dns.SYSTEM.lookup(host) }
                }
                .build()
            return Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrtopediaApiService::class.java)
        }
    }
}