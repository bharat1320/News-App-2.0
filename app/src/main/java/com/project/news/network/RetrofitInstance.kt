package com.project.news.network

import androidx.fragment.app.Fragment
import com.project.news.BuildConfig.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance : Fragment() {

    companion object {

        fun createService(): Retrofit {
            return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        var client = OkHttpClient.Builder().addInterceptor(getInterceptor()).build()
//            .addInterceptor(
//                ChuckerInterceptor.Builder(context)
//                    .collector(ChuckerCollector(context))
//                    .maxContentLength(250000L)
//                    .redactHeaders(emptySet())
//                    .alwaysReadResponseBody(false)
//                    .build()
//            ).build()

        private fun getInterceptor(): Interceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return interceptor
        }
    }
}