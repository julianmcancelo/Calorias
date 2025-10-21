package com.calorias.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey
    val id: String,
    val userId: String,
    val date: Long,
    val type: String, // "barcode", "ai_photo", "manual"
    val foodName: String,
    val barcode: String? = null,
    val portion: Double,
    val unit: String, // "g", "ml", "portion"
    val kcal: Double,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val photoUrl: String? = null,
    val source: String, // "openfoodfacts", "gemini", "user"
    val syncedToHealthConnect: Boolean = false,
    val pendingSync: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
