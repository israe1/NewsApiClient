package com.example.newsapiclient.data.repository

import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.data.repository.datasource.NewsLocalDataSource
import com.example.newsapiclient.data.repository.datasource.NewsRemoteDataSource
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {
    override suspend fun getNewsHeadlines(country: String, category: String, page: Int): Resource<APIResponse> =
        responseToResource(newsRemoteDataSource.getTopHeadlines(country, category, page))

    override suspend fun getSearchedNews(searchQuery: String, country: String, page: Int):
            Resource<APIResponse> =
        responseToResource(newsRemoteDataSource.getSearchedNews(searchQuery, country, page))


    override suspend fun saveNews(article: Article) = newsLocalDataSource.saveArticleToDB(article)


    override suspend fun deleteNews(article: Article) = newsLocalDataSource.deleteArticlesFromDB(article)


    override fun getSavedNews(): Flow<List<Article>> = newsLocalDataSource.getSavedArticles()

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())
    }
}