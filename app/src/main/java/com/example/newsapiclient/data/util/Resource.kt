package com.example.newsapiclient.data.util

sealed class Resource<out T> (
    val data: T? = null,
    val message: String? = null
) {
    data class Success<T>(val value: T) : Resource<T>(data = value)
    data class Loading<T>(val partialData: T? = null) : Resource<T>(data = partialData)
    data class Error<T>(val error: String, val lastData: T? = null) :
        Resource<T>(data = lastData, message = error)
}