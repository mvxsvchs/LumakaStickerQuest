package com.example.lumaka.data.repository

/**
 * Lightweight result wrapper for API calls so callers can surface server and network errors.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String? = null,
        val isNetworkError: Boolean = false,
        val statusCode: Int? = null
    ) : ApiResult<Nothing>()
}
