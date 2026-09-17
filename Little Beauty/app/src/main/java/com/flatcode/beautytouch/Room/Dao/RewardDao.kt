package com.flatcode.beautytouch.Room.Dao

import androidx.room.*
import com.flatcode.beautytouch.Model.Reward
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards")
    fun getRewards(): Flow<List<Reward>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewards(rewards: List<Reward>)

    @Query("DELETE FROM rewards")
    suspend fun deleteAllRewards()
}