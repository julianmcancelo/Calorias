package com.calorias.ai.data.local.dao

import androidx.room.*
import com.calorias.ai.data.local.entity.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE userId = :userId AND isActive = 1 ORDER BY createdAt DESC LIMIT 1")
    fun getActiveGoal(userId: String): Flow<Goal?>

    @Query("SELECT * FROM goals WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<Goal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: Goal)

    @Update
    suspend fun update(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)
}
