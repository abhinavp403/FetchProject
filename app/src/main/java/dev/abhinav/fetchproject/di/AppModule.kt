package dev.abhinav.fetchproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.abhinav.fetchproject.api.FetchApi
import dev.abhinav.fetchproject.repository.FetchRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Base URL
    private const val URL = "https://fetch-hiring.s3.amazonaws.com/"

    // Logging Interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Provides dependency of repository class
    @Singleton
    @Provides
    fun provideRepository(
        api: FetchApi
    ) = FetchRepository(api)

    // Provides retrofit instance of api call
    @Singleton
    @Provides
    fun provideApi() : FetchApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .client(client)
            .build()
            .create(FetchApi::class.java)
    }
}