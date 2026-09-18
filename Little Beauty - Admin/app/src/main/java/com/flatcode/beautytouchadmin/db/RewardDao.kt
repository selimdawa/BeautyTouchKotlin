package com.flatcode.beautytouchadmin.db

import androidx.room.*
import com.flatcode.beautytouchadmin.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards")
    fun getAll(): Flow<List<Reward>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reward: Reward)
}