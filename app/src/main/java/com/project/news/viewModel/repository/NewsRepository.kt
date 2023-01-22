package com.project.news.viewModel.repository

import com.project.news.data.News
import com.project.news.network.RetrofitInstance
import com.project.news.network.URLS
import com.project.news.viewModel.NewsViewModel
import com.project.news.viewModel.repository.api.NewsApis
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.Exception
import kotlin.collections.ArrayList

@ActivityRetainedScoped
class NewsRepository
@Inject constructor(
    private val api : NewsApis
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