package com.example.client__android_app.di

import com.example.client__android_app.data.repository.SettingsRepository
import com.example.client__android_app.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // To make the repository a singleton accessible app-wide
abstract class RepositoryModule {

    @Binds
    @Singleton // Ensures the same instance is provided
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}