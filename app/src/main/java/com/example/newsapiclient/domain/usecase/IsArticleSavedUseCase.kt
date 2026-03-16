package com.example.newsapiclient.domain.usecase

import com.example.newsapiclient.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class IsArticleSavedUseCase(private val newsRepository: NewsRepository) {

    suspend fun invoke(url: String?, publishedAt: String?): Boolean =
        newsRepository.isArticleSaved(url, publishedAt)

}