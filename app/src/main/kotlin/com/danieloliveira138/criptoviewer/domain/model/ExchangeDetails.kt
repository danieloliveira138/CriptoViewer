package com.danieloliveira138.criptoviewer.domain.model

data class ExchangeDetails(
    val id: Int,
    val name: String?,
    val slug: String?,
    val logo: String?,
    val description: String?,
    val dateLaunched: String?,
    val urls: List<String>
)
