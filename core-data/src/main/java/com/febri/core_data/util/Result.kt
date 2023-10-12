package com.febri.core_data.util

sealed class Result<out T : Any> {
        data class Success<out T : Any>(val value: T) : Result<T>()
        data object Loading : Result<Nothing>()
        data class Failure(val throwable: Throwable) : Result<Nothing>()
    }