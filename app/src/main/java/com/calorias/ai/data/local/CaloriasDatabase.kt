package com.calorias.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.calorias.ai.data.local.dao.EntryDao
import com.calorias.ai.data.local.dao.FoodDao
import com.calorias.ai.data.local.dao.GoalDao
import com.calorias.ai.data.local.dao.UserDao
import com.calorias.ai.data.local.entity.Entry
import com.calorias.ai.data.local.entity.Food
import com.calorias.ai.data.local.entity.Goal
import com.calorias.ai.data.local.entity.User

@Database(
    entities = [User::class, Entry::class, Food::class, Goal::class],
    version = 1,
    exportSchema = false
)
abstract class CaloriasDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun entryDao(): EntryDao
    abstract fun foodDao(): FoodDao
    abstract fun goalDao(): GoalDao
}
