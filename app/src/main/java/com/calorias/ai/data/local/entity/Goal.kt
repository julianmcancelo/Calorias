package com.calorias.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey
    val id: String,
    val userId: String,
    val dailyKcalTarget: Double,
    val proteinTarget: Double? = null,
    val carbsTarget: Double? = null,
    val fatTarget: Double? = null,
    val startDate: Long,
    val endDate: Long? = null,
    val isActive: Boolean = true,
    val pendingSync: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
