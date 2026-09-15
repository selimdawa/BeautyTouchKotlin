package com.flatcode.beautytouchadmin.database

import androidx.room.*
import com.flatcode.beautytouchadmin.Model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): Flow<User?>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Delete
    suspend fun delete(user: User)
}

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts WHERE postid = :id")
    fun getPostById(id: String): Flow<Post?>

    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<Post>>

    @Delete
    suspend fun delete(post: Post)
}

@Dao
interface ADsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ads: ADs)

    @Query("SELECT * FROM ads")
    fun getAllADs(): Flow<List<ADs>>

    @Delete
    suspend fun delete(ads: ADs)
}

@Dao
interface ToolsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tools: Tools)

    @Query("SELECT * FROM tools LIMIT 1")
    fun getTools(): Flow<Tools?>

    @Delete
    suspend fun delete(tools: Tools)
}

@Dao
interface ShoppingCenterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingCenter: ShoppingCenter)

    @Query("SELECT * FROM shopping_centers")
    fun getAllShoppingCenters(): Flow<List<ShoppingCenter>>

    @Delete
    suspend fun delete(shoppingCenter: ShoppingCenter)
}

@Dao
interface RewardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reward: Reward)

    @Query("SELECT * FROM rewards")
    fun getAllRewards(): Flow<List<Reward>>

    @Delete
    suspend fun delete(reward: Reward)
}

@Dao
interface PointsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(points: Points)

    @Query("SELECT * FROM points LIMIT 1")
    fun getPoints(): Flow<Points?>

    @Delete
    suspend fun delete(points: Points)
}
