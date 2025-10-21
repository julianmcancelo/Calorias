package com.calorias.ai.data.repository

import com.calorias.ai.data.remote.OpenFoodFactsService
import com.calorias.ai.data.remote.model.OffProduct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val off: OpenFoodFactsService
) {
    suspend fun getProductByBarcode(barcode: String): OffProduct? {
        return try {
            val res = off.getProduct(barcode)
            if (res.status == 1) res.product else null
        } catch (_: Exception) {
            null
        }
    }
}
