package com.example.memeoroid

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(Application::class)
class AppModule {

    @Singleton
    @Provides
    @Named("String1")
    fun provideTestString1() = "This is a string we will inject"
}