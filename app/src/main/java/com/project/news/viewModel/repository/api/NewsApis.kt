package com.project.news.viewModel.repository.api

import retrofit2.http.GET
import retrofit2.http.Url

interface NewsApis {
    @GET
    suspend fun getNews(@Url url :String) : Map<String,Any>
}