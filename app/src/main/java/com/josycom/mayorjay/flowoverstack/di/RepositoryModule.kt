package com.josycom.mayorjay.flowoverstack.di

import com.josycom.mayorjay.flowoverstack.data.repository.PreferenceRepository
import com.josycom.mayorjay.flowoverstack.data.repository.PreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: PreferenceRepositoryImpl): PreferenceRepository
}