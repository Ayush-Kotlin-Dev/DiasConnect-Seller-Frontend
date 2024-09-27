package com.ayush.data.di

import com.ayush.data.repository.AuthRepositoryImpl
import com.ayush.data.repository.ProductRepositoryImpl
import com.ayush.data.repository.UploadProductRepositoryImpl
import com.ayush.domain.network.NetworkService
import com.ayush.domain.repository.AuthRepository
import com.ayush.domain.repository.ProductRepository
import com.ayush.domain.repository.UploadProductRepository
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

    @Provides
    @Singleton
    fun provideAuthRepository(
        networkService: NetworkService
    ): AuthRepository {
        return AuthRepositoryImpl(networkService)
    }

    @Provides
    @Singleton
    fun provideUploadProductRepository(
        networkService: NetworkService
    ): UploadProductRepository {
        return UploadProductRepositoryImpl(networkService)
    }

}
