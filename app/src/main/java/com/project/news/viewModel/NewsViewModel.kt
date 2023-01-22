package com.project.news.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.news.data.News
import com.project.news.viewModel.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    val repository: NewsRepository
) :ViewModel() {

    var newsResponse : MutableLiveData<ArrayList<News>> = MutableLiveData()

    var newsErrorResponse : MutableLiveData<String> = MutableLiveData()

    fun getNews(country :String = "in", category :String? = null) {
        viewModelScope.launch {
            val response = repository.getNews(country,category)
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
                newsResponse.postValue(news)
            } catch(e :Exception) {
                newsErrorResponse.postValue(e.localizedMessage)
            }
        }
    }
}