package com.flatcode.beautytouchadmin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flatcode.beautytouchadmin.model.*

@Database(
    entities = [
        User::class, Post::class, ADs::class, Tools::class,
        ShoppingCenter::class, Reward::class, Points::class, Main::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun adsDao(): ADsDao
    abstract fun toolsDao(): ToolsDao
    abstract fun shoppingCenterDao(): ShoppingCenterDao
    abstract fun rewardDao(): RewardDao
    abstract fun pointsDao(): PointsDao
    abstract fun mainDao(): MainDao
}
