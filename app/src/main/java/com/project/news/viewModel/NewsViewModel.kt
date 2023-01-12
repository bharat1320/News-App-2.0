package com.project.news.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.news.data.News
import com.project.news.viewModel.repository.NewsRepository

class NewsViewModel :ViewModel() {

    val repository = NewsRepository(this)

    var newsResponse : MutableLiveData<ArrayList<News>> = MutableLiveData()

    var newsErrorResponse : MutableLiveData<String> = MutableLiveData()

}