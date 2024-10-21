package dev.abhinav.fetchproject.repository

import dev.abhinav.fetchproject.api.FetchApi
import dev.abhinav.fetchproject.api.remote.FetchResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchRepository @Inject constructor(
    private val api: FetchApi
) {
    // Calls api to get response
    suspend fun getResponse() : FetchResponse {
        return api.getResponse()
    }
}