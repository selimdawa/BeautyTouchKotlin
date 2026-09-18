package com.flatcode.beautytouchadmin.db

import androidx.room.*
import com.flatcode.beautytouchadmin.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ADsDao {
    @Query("SELECT * FROM ads")
    fun getAll(): Flow<List<ADs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ads: ADs)
}