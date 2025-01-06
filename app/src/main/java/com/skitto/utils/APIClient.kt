package com.skitto.utils

import com.loopj.android.http.SyncHttpClient




class APIClient {
    companion object {
        const val BASE_URL: String = "https://sellercenter.styleecho.net/api/v1/"
    }

    private val client = SyncHttpClient()

    fun getInstance(): SyncHttpClient {
        return client
    }


}