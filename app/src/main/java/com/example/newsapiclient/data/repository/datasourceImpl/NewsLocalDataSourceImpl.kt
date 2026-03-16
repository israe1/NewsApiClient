package com.example.newsapiclient.data.repository.datasourceImpl

import com.example.newsapiclient.data.db.ArticleDao
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.data.repository.datasource.NewsLocalDataSource
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSourceImpl(
    private val articleDao: ArticleDao
) : NewsLocalDataSource {

    override fun getSavedArticles(): Flow<List<Article>> = articleDao.getAllArticles()

    override suspend fun saveArticleToDB(article: Article) = articleDao.insert(article)

    override suspend fun deleteArticlesFromDB(article: Article) = articleDao.deleteArticle(article)

    override suspend fun clearAll() = articleDao.deleteAllArticles()

}