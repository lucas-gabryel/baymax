package com.ifal.baymax.api.cardiology

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

data class RecomendacaoCardio(
    val nivel: String,
    val sintoma: String,
    val recomendacao: String
)

interface CardiologiaApiService {
    @GET
    suspend fun buscarRecomendacoes(@Url url: String): List<RecomendacaoCardio>

    companion object {
        fun create() = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardiologiaApiService::class.java)
    }
}