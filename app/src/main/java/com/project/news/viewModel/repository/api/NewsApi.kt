package com.project.news.viewModel.repository.api

import retrofit2.http.GET
import retrofit2.http.Url

interface NewsApi {
    @GET
    suspend fun getNews(@Url url :String) : Map<String,Any>
}