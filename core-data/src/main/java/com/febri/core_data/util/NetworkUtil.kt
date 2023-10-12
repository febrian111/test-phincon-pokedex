package com.febri.core_data.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun okHttpClientBuilder(init: OkHttpClient.Builder.() -> Unit): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.init()
    return builder.build()
}

fun retrofitBuilder(init: Retrofit.Builder.() -> Unit): Retrofit {
    val builder = Retrofit.Builder()
    builder.init()
    return builder.build()
}

fun OkHttpClient.Builder.setDefaultTimeout() {
    connectTimeout(15, TimeUnit.SECONDS)
    readTimeout(15, TimeUnit.SECONDS)
    writeTimeout(15, TimeUnit.SECONDS)
}