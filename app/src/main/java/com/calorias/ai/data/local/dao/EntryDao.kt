package com.calorias.ai.data.local.dao

import androidx.room.*
import com.calorias.ai.data.local.entity.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Query("SELECT * FROM entries WHERE userId = :userId ORDER BY date DESC")
    fun getEntriesByUser(userId: String): Flow<List<Entry>>

    @Query("SELECT * FROM entries WHERE userId = :userId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getEntriesInRange(userId: String, startDate: Long, endDate: Long): Flow<List<Entry>>

    @Query("SELECT * FROM entries WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<Entry>

    @Query("SELECT SUM(kcal) FROM entries WHERE userId = :userId AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalKcalInRange(userId: String, startDate: Long, endDate: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: Entry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<Entry>)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}
