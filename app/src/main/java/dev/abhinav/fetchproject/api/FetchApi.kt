package dev.abhinav.fetchproject.api

import dev.abhinav.fetchproject.api.remote.FetchResponse
import retrofit2.http.GET

interface FetchApi {

    @GET("hiring.json")
    suspend fun getResponse() : FetchResponse
}