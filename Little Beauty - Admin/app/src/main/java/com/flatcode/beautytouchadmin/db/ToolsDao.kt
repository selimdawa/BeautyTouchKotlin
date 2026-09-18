package com.flatcode.beautytouchadmin.db

import androidx.room.*
import com.flatcode.beautytouchadmin.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolsDao {
    @Query("SELECT * FROM tools")
    fun getAll(): Flow<List<Tools>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tools: Tools)
}