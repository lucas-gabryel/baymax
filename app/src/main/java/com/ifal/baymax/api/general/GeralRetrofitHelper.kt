package com.ifal.baymax.api.general

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeralRetrofitHelper {

    private val client = Retrofit.Builder()
        .baseUrl("https://apidadosabertos.saude.gov.br/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val unidadeSaudeApi: UnidadeSaudeApi = client.create(UnidadeSaudeApi::class.java)
}