package com.calorias.ai.data.local.dao

import androidx.room.*
import com.calorias.ai.data.local.entity.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM custom_foods WHERE userId = :userId")
    fun getFoodsByUser(userId: String): Flow<List<Food>>

    @Query("SELECT * FROM custom_foods WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<Food>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: Food)

    @Update
    suspend fun update(food: Food)

    @Delete
    suspend fun delete(food: Food)
}
