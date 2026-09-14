package com.flatcode.beautytouchadmin.di

import com.flatcode.beautytouchadmin.Repository.ADsRepository
import com.flatcode.beautytouchadmin.Repository.AuthRepository
import com.flatcode.beautytouchadmin.Repository.HotProductRepository
import com.flatcode.beautytouchadmin.Repository.MainRepository
import com.flatcode.beautytouchadmin.Repository.PostRepository
import com.flatcode.beautytouchadmin.Repository.ShoppingRepository
import com.flatcode.beautytouchadmin.Repository.SliderRepository
import com.flatcode.beautytouchadmin.Repository.ToolsRepository
import com.flatcode.beautytouchadmin.Repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(database: FirebaseDatabase, storage: FirebaseStorage): UserRepository {
        return UserRepository(database, storage)
    }

    @Provides
    @Singleton
    fun provideMainRepository(database: FirebaseDatabase): MainRepository {
        return MainRepository(database)
    }

    @Provides
    @Singleton
    fun providePostRepository(database: FirebaseDatabase, storage: FirebaseStorage): PostRepository {
        return PostRepository(database, storage)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepository(auth)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(database: FirebaseDatabase, storage: FirebaseStorage): ShoppingRepository {
        return ShoppingRepository(database, storage)
    }

    @Provides
    @Singleton
    fun provideToolsRepository(database: FirebaseDatabase, storage: FirebaseStorage): ToolsRepository {
        return ToolsRepository(database, storage)
    }

    @Provides
    @Singleton
    fun provideADsRepository(database: FirebaseDatabase): ADsRepository {
        return ADsRepository(database)
    }

    @Provides
    @Singleton
    fun provideHotProductRepository(database: FirebaseDatabase): HotProductRepository {
        return HotProductRepository(database)
    }

    @Provides
    @Singleton
    fun provideSliderRepository(database: FirebaseDatabase, storage: FirebaseStorage): SliderRepository {
        return SliderRepository(database, storage)
    }
}