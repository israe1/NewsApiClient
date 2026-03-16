package com.example.newsapiclient.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.newsapiclient.data.db.ArticleDao
import com.example.newsapiclient.data.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application): ArticleDatabase {
        return Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            "news.db"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(articleDatabase: ArticleDatabase): ArticleDao =
        articleDatabase.getArticleDao()

}