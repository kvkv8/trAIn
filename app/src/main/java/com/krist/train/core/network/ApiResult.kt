package com.krist.train.core.network

sealed interface ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>
    data class Failure(val message: String, val cause: Throwable? = null) : ApiResult<Nothing>
}

suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> = try {
    ApiResult.Success(block())
} catch (error: Throwable) {
    ApiResult.Failure(error.message ?: "Unexpected API error", error)
}
