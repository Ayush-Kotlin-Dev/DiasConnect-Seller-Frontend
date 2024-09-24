package com.ayush.domain.di

import com.ayush.domain.repository.ProductRepository
import com.ayush.domain.usecase.GetProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetProductUseCase(
        productRepository: ProductRepository
    ): GetProductUseCase {
        return GetProductUseCase(productRepository)
    }
}
