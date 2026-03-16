package com.example.newsapiclient.data.repository.datasource

import com.example.newsapiclient.data.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {

    fun getSavedArticles(): Flow<List<Article>>

    suspend fun isArticleSaved(url: String?, publishedAt: String?): Boolean

    suspend fun saveArticleToDB(article: Article)

    suspend fun deleteArticlesFromDB(article: Article)

    suspend fun clearAll()

}