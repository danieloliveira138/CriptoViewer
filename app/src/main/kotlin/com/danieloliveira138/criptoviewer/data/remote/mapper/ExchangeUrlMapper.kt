package com.danieloliveira138.criptoviewer.data.remote.mapper

import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeUrlsDTO
import com.danieloliveira138.criptoviewer.domain.model.ExchangeUrl
import jakarta.inject.Inject

class ExchangeUrlMapper @Inject constructor() : Mapper<ExchangeUrlsDTO, ExchangeUrl> {

    override fun map(input: ExchangeUrlsDTO): ExchangeUrl {
        return ExchangeUrl(
            website = input.website,
            twitter = input.twitter,
            facebook = input.facebook
        )
    }
}