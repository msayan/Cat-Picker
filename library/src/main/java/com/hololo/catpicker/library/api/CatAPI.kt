package com.hololo.catpicker.library.api

import com.hololo.catpicker.library.api.response.ImageResponse
import com.hololo.catpicker.library.core.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CatAPI {

    @GET("images/search")
    suspend fun getImages(
        @Query("page") page: Int?,
        @Query("limit") limit: Int,
        @Query("size") size: String = "small",
        @Query("order") order: String = "ASC"
    ): List<ImageResponse>

    companion object {
        fun create(): CatAPI {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestWithUserAPIKey = originalRequest.newBuilder()
                        .header("x-api-key", "c96ef186-fb86-4468-bb77-b1aa1f0c03b1")
                        .build()
                    chain.proceed(requestWithUserAPIKey)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(CatAPI::class.java)
        }
    }
}
