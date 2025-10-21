package com.calorias.ai.data.remote.model

import com.squareup.moshi.Json

data class OffProductResponse(
    @Json(name = "status") val status: Int?,
    @Json(name = "product") val product: OffProduct?
)

data class OffProduct(
    @Json(name = "product_name") val productName: String?,
    @Json(name = "brands") val brands: String?,
    @Json(name = "nutriments") val nutriments: Nutriments?
)

data class Nutriments(
    @Json(name = "energy-kcal_100g") val energyKcal100g: Double?,
    @Json(name = "energy-kcal_serving") val energyKcalServing: Double?,
    @Json(name = "serving_size") val servingSize: String?
)
