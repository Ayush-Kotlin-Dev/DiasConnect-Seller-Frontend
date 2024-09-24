package com.ayush.diasconnect_seller.di

import android.content.Context
import com.ayush.diasconnect_seller.DiasConnectApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
        // Provide the Application context
        @Singleton
        @Provides
        fun provideApplication(app: DiasConnectApp): Context {
            return app.applicationContext
        }
}