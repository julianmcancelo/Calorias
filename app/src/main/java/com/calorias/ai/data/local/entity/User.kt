package com.calorias.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isPremium: Boolean = false,
    val dailyScanLimit: Int = 5,
    val subscriptionId: String? = null,
    val subscriptionExpiresAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastSyncAt: Long? = null
)
