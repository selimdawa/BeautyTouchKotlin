package com.flatcode.beautytouch.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flatcode.beautytouch.Model.*
import com.flatcode.beautytouch.Room.Dao.*

@Database(
    entities = [User::class, Post::class, ADs::class, ShoppingCenter::class, Tools::class, Reward::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun adsDao(): ADsDao
    abstract fun shoppingCenterDao(): ShoppingCenterDao
    abstract fun toolsDao(): ToolsDao
    abstract fun rewardDao(): RewardDao
}