package com.ayush.domain.di

import com.ayush.domain.repository.AuthRepository
import com.ayush.domain.repository.ProductRepository
import com.ayush.domain.repository.UploadProductRepository
import com.ayush.domain.usecase.GetProductUseCase
import com.ayush.domain.usecase.LoginUseCase
import com.ayush.domain.usecase.SignupUseCase
import com.ayush.domain.usecase.UploadProductUseCase
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

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authRepository: AuthRepository
    ): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authRepository: AuthRepository
    ): SignupUseCase {
        return SignupUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAddProductUseCase(
        uploadProductRepository: UploadProductRepository
    ): UploadProductUseCase {
        return UploadProductUseCase(uploadProductRepository)
    }
}
