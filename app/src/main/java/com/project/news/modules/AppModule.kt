package com.project.news.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.project.news.BuildConfig
import com.project.news.viewModel.MainViewModel
import com.project.news.viewModel.repository.NewsRepository
import com.project.news.viewModel.repository.api.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private fun getInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    fun createService(): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(getInterceptor()).build())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesNewsApi(retrofit: Retrofit) :NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsRepository(newsApi: NewsApi) :NewsRepository {
        return NewsRepository(newsApi)
    }
}