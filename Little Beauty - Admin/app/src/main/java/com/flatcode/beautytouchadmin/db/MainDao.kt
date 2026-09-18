package com.flatcode.beautytouchadmin.db

import androidx.room.*
import com.flatcode.beautytouchadmin.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {
    @Query("SELECT * FROM main_menu")
    fun getAll(): Flow<List<Main>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(main: Main)
}