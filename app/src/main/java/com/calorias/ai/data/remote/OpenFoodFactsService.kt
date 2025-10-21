package com.calorias.ai.data.remote

import com.calorias.ai.data.remote.model.OffProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsService {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): OffProductResponse
}
