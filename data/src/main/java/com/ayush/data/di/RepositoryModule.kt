package com.ayush.data.di

import com.ayush.data.repository.ProductRepositoryImpl
import com.ayush.domain.network.NetworkService
import com.ayush.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        networkService: NetworkService
    ): ProductRepository {
        return ProductRepositoryImpl(networkService)
    }
}
