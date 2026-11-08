package com.ifal.baymax.api.general

import retrofit2.http.GET
import retrofit2.http.Query

interface UnidadeSaudeApi {

    @GET("cnes/estabelecimentos")
    suspend fun buscarUnidades(
        @Query("codigo_municipio") codigoMunicipio: Int,
        @Query("codigo_tipo_unidade") codigoTipoUnidade: Int,
        @Query("limit") limite: Int = 20
    ): EstabelecimentosResponse
}