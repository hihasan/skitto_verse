package com.skitto.utils

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private var httpClient: OkHttpClient.Builder? = null
    private lateinit var builder: Retrofit.Builder
    private var ConnectionTimeout = 64000
    private var ReadTimeout = 64000


    /**
     *
     * @param serviceClass
     * @param serverURL
     * @param withHeader
     * @param <S>
     * @return
    </S> */
    //Retrofit service method for all api calling
    fun <S> createService(serviceClass: Class<S>, serverURL: String, withHeader: Boolean): S {
        val gson = GsonBuilder().setLenient().create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient = OkHttpClient.Builder()
        httpClient!!.addInterceptor(logging)
        ConnectionTimeout = 64000
        httpClient!!.connectTimeout(ConnectionTimeout.toLong(), TimeUnit.SECONDS)
        ReadTimeout = 64000
        httpClient!!.readTimeout(ReadTimeout.toLong(), TimeUnit.SECONDS)
        if (withHeader) {
            httpClient!!.addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json-patch+json")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("X-Requested-By", "test")
                        .build()
                    chain.proceed(request)
                }
            )
        }
        builder = Retrofit.Builder()
            .baseUrl(serverURL)
//            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(StringConverterFactory(GsonConverterFactory.create(gson)))
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit = builder.client(httpClient!!.build()).build()
        return retrofit.create(serviceClass)

    }
}