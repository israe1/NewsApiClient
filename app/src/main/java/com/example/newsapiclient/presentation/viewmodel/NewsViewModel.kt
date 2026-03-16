package com.example.newsapiclient.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsapiclient.data.model.APIResponse
import com.example.newsapiclient.data.model.Article
import com.example.newsapiclient.data.util.Resource
import com.example.newsapiclient.domain.usecase.DeleteSavedNewsUseCase
import com.example.newsapiclient.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsapiclient.domain.usecase.GetSavedNewsUseCase
import com.example.newsapiclient.domain.usecase.GetSearchedNewsUseCase
import com.example.newsapiclient.domain.usecase.IsArticleSavedUseCase
import com.example.newsapiclient.domain.usecase.SaveNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(
    application: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase,
    private val isArticleSavedUseCase: IsArticleSavedUseCase
): AndroidViewModel(application = application) {
    private val _newsHeadlines: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    val newsHeadlines: LiveData<Resource<APIResponse>> = _newsHeadlines
    private val _searchedNewsHeadlines: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    val searchedNewsHeadlines: LiveData<Resource<APIResponse>> = _searchedNewsHeadlines
    val showSaveButton: MutableLiveData<Boolean> = MutableLiveData()


    var country: String = ""
    var category: String = ""
    var selectedCategoryIndex: Int = 0

    fun getNewsHeadlines(country: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isInternetAvailable(application)) {
                _newsHeadlines.postValue(Resource.Loading())
                val apiResult = getNewsHeadlinesUseCase.execute(country, category, page)
                _newsHeadlines.postValue(apiResult)
            } else {
                _newsHeadlines.postValue(Resource.Error("Internet is not available"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _newsHeadlines.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun searchNewsHeadlines(searchQuery: String, country: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isInternetAvailable(application)) {
                _searchedNewsHeadlines.postValue(Resource.Loading())
                val apiResult = getSearchedNewsUseCase.execute(searchQuery, country, page)
                _searchedNewsHeadlines.postValue(apiResult)
            } else {
                _searchedNewsHeadlines.postValue(Resource.Error("Internet is not available"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _searchedNewsHeadlines.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun saveArticleToDB(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        saveNewsUseCase.execute(article)
    }

    fun getSavedArticles() = liveData {
        getSavedNewsUseCase.execute().collect {
            emit(it)
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        deleteSavedNewsUseCase.execute(article)
    }

    fun isArticleSaved(url: String?, publishedAt: String?) = viewModelScope.launch(Dispatchers.IO) {
        val saved = isArticleSavedUseCase.invoke(url, publishedAt)
        showSaveButton.postValue(!saved)
    }

    fun isInternetAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

}