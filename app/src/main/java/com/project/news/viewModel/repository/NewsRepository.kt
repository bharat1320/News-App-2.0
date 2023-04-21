package com.project.news.viewModel.repository

import com.project.news.network.URLS
import com.project.news.viewModel.repository.api.NewsApi
import java.util.*
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api : NewsApi
) {
    suspend fun getNews(country :String = "in", category :String? = null): Map<String, Any> {
//        Make Request Query
        val url = if(category.isNullOrBlank() || category.equals("all",true))
            "country=${country.lowercase(Locale.ROOT)}"
        else
            "country=${country.lowercase(Locale.ROOT)}&category=${category.lowercase(Locale.ROOT)}"

//        Make Network call
        return api.getNews("?" + url + URLS.apiKey)
    }
}