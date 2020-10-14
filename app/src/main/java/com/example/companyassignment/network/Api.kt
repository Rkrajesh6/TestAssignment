package com.example.companyassignment.network

import com.example.companyassignment.model.Todo
import com.example.pagingexample.model.RepoSearchResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/todos")
    suspend fun getTodo(): Response<List<Todo>>

    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): RepoSearchResponse
}