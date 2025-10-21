package com.calorias.ai.di

import android.content.Context
import com.calorias.ai.health.HealthConnectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HealthModule {

    @Provides
    @Singleton
    fun provideHealthConnectRepository(@ApplicationContext context: Context): HealthConnectRepository =
        HealthConnectRepository(context)
}
