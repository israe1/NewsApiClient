package com.example.newsapiclient.data.repository.datasource

import com.example.newsapiclient.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(): Response<APIResponse>

    suspend fun getSearchedNews(searchQuery: String): Response<APIResponse>

}