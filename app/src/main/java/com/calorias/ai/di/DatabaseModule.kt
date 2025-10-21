package com.calorias.ai.di

import android.content.Context
import androidx.room.Room
import com.calorias.ai.data.local.CaloriasDatabase
import com.calorias.ai.data.local.dao.EntryDao
import com.calorias.ai.data.local.dao.FoodDao
import com.calorias.ai.data.local.dao.GoalDao
import com.calorias.ai.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CaloriasDatabase =
        Room.databaseBuilder(
            context,
            CaloriasDatabase::class.java,
            "calorias_db"
        ).build()

    @Provides
    fun provideUserDao(db: CaloriasDatabase): UserDao = db.userDao()

    @Provides
    fun provideEntryDao(db: CaloriasDatabase): EntryDao = db.entryDao()

    @Provides
    fun provideFoodDao(db: CaloriasDatabase): FoodDao = db.foodDao()

    @Provides
    fun provideGoalDao(db: CaloriasDatabase): GoalDao = db.goalDao()
}
