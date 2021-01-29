package com.ferelin.repository.db.response

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Failed<out T>(val throwable: Throwable? = null) : Response<T>()
}