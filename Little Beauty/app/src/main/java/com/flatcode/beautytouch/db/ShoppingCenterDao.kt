package com.flatcode.beautytouch.db

import androidx.room.*
import com.flatcode.beautytouch.model.ShoppingCenter
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCenterDao {
    @Query("SELECT * FROM shopping_centers WHERE publisher = :publisher AND aname = :aname")
    fun getShoppingCenters(publisher: String, aname: String): Flow<List<ShoppingCenter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingCenters(centers: List<ShoppingCenter>)

    @Query("DELETE FROM shopping_centers")
    suspend fun deleteAllShoppingCenters()
}