package com.skitto.utils

typealias SimpleResource = Response<Unit>

sealed class Response<T>(var data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null): Response<T>(data)
    class Success<T>(data: T?): Response<T>(data)
    class Error<T>(message: String?, data: T? = null): Response<T>(data, message)
}