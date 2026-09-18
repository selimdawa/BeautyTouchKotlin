package com.flatcode.beautytouchadmin.db

import androidx.room.*
import com.flatcode.beautytouchadmin.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCenterDao {
    @Query("SELECT * FROM shopping_centers")
    fun getAll(): Flow<List<ShoppingCenter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(center: ShoppingCenter)
}