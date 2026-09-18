package com.flatcode.beautytouch.db

import androidx.room.*
import com.flatcode.beautytouch.model.ADs
import kotlinx.coroutines.flow.Flow

@Dao
interface ADsDao {
    @Query("SELECT * FROM ads")
    fun getAllADs(): Flow<List<ADs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertADs(ads: List<ADs>)

    @Query("DELETE FROM ads")
    suspend fun deleteAllADs()
}