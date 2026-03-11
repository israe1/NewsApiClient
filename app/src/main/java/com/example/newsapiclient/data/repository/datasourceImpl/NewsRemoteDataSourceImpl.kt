package com.example.newsapiclient.data.repository.datasourceImpl

import com.example.newsapiclient.data.api.NewsAPIService
import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.repository.datasource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService,
    private val country: String = "us",
    private val page: Int = 1
): NewsRemoteDataSource {

    override suspend fun getTopHeadlines(): Response<APIResponse> =
        newsAPIService.getTopHeadlines(country, page)

    override suspend fun getSearchedNews(searchQuery: String): Response<APIResponse> =
        newsAPIService.searchTopHeadlines(searchQuery, country, page)
}