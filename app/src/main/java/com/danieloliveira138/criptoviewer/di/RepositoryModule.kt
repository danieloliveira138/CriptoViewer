package com.danieloliveira138.criptoviewer.di

import com.danieloliveira138.criptoviewer.data.repository.ExchangeRepositoryImpl
import com.danieloliveira138.criptoviewer.domain.repository.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExchangeRepository(impl: ExchangeRepositoryImpl): ExchangeRepository
}
