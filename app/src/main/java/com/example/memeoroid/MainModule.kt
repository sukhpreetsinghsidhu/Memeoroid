package com.example.memeoroid

import android.app.Activity
import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class MainModule {

    @ActivityScoped
    @Provides
    @Named("String2")
    fun provideTestString2(@ApplicationContext context : Context, @Named("String1") testString1 : String
    ) = "${context.getString(R.string.string_to_inject)} - $testString1"
}