package com.flatcode.beautytouch.di

import android.content.Context
import androidx.room.Room
import com.flatcode.beautytouch.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "beauty_touch_db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun providePostDao(database: AppDatabase): PostDao = database.postDao()

    @Provides
    fun provideADsDao(database: AppDatabase): ADsDao = database.adsDao()

    @Provides
    fun provideShoppingCenterDao(database: AppDatabase): ShoppingCenterDao = database.shoppingCenterDao()

    @Provides
    fun provideToolsDao(database: AppDatabase): ToolsDao = database.toolsDao()

    @Provides
    fun provideRewardDao(database: AppDatabase): RewardDao = database.rewardDao()
}