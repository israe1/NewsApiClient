package com.example.newsapiclient.data.repository.datasource

import com.example.newsapiclient.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(country: String, category: String, page: Int): Response<APIResponse>

    suspend fun getSearchedNews(searchQuery: String, country: String, page: Int): Response<APIResponse>

}