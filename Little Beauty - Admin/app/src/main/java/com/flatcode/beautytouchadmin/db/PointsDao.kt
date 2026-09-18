package com.flatcode.beautytouchadmin.db

import androidx.room.*
import com.flatcode.beautytouchadmin.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {
    @Query("SELECT * FROM points")
    fun getAll(): Flow<List<Points>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(points: Points)
}