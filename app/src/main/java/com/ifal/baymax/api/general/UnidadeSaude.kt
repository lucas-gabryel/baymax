package com.ifal.baymax.api.general

import com.google.gson.annotations.SerializedName

data class EstabelecimentosResponse(
    @SerializedName("estabelecimentos") val estabelecimentos: List<UnidadeSaude>
)

data class UnidadeSaude(
    @SerializedName("codigo_cnes") val codigoCnes: Int,
    @SerializedName("nome_fantasia") val nome: String?,
    @SerializedName("endereco_estabelecimento") val endereco: String?,
    @SerializedName("numero_estabelecimento") val numero: String?,
    @SerializedName("bairro_estabelecimento") val bairro: String?,
    @SerializedName("numero_telefone_estabelecimento") val telefone: String?
)
