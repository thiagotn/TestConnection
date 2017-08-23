package br.com.uol.produtos.testconnection.server

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by tnogueira on 18/08/17.
 */
class BaseRequest(val baseUrl: String, val https: Boolean = true) {

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor())
                .connectionSpecs(Collections.singletonList(buildTLSConnSpecs()))
                .build()
    }

    private fun buildTLSConnSpecs(): ConnectionSpec {
        if (https) return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).build()
        return ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    fun retrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}