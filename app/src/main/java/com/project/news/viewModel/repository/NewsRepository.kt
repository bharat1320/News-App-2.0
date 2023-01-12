package com.project.news.viewModel.repository

import com.project.news.data.News
import com.project.news.network.RetrofitInstance
import com.project.news.network.URLS
import com.project.news.viewModel.NewsViewModel
import com.project.news.viewModel.repository.api.NewsApis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class NewsRepository(private val vm : NewsViewModel) {
    private val api = RetrofitInstance.createService().create(NewsApis::class.java)

    fun getNews(country :String = "in", category :String? = null) {
        // Make Request Query
        val url = if(category.isNullOrBlank() || category.equals("all",true))
            "country=${country.lowercase(Locale.ROOT)}"
        else
            "country=${country.lowercase(Locale.ROOT)}&category=${category.lowercase(Locale.ROOT)}"

        // Make Network request
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getNews("?" + url + URLS.apiKey)
            try {
                // filtering news that has all parameters
                val news = arrayListOf<News>()
                (response["articles"] as ArrayList<Map<String,String?>>).forEach {
                    if(it["title"]?.isNotBlank() == true &&
                        it["url"]?.isNotBlank() == true &&
                        it["urlToImage"]?.isNotBlank() == true) {
                        news.add(
                            News(
                            it["title"]?:"",
                            it["url"]?:"",
                            it["urlToImage"]?:""
                        )
                        )
                    }
                }
                vm.newsResponse.postValue(news)
            } catch(e :Exception) {
                vm.newsErrorResponse.postValue(e.localizedMessage)
            }
        }
    }
}