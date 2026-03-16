package com.example.newsapiclient.presentation.di

import com.example.newsapiclient.presentation.adapter.NewsAdapter
import com.example.newsapiclient.presentation.adapter.SavedNewsAdapter
import com.example.newsapiclient.presentation.adapter.SearchedNewsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Singleton
    @Provides
    fun provideNewsAdapter(): NewsAdapter {
        return NewsAdapter()
    }

    @Singleton
    @Provides
    fun provideSearchedNewsAdapter(): SearchedNewsAdapter {
        return SearchedNewsAdapter()
    }

    @Singleton
    @Provides
    fun provideSavedNewsAdapter(): SavedNewsAdapter {
        return SavedNewsAdapter()
    }

}