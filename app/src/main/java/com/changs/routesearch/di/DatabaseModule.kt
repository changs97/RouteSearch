package com.changs.routesearch.di

import android.content.Context
import com.changs.routesearch.data.source.local.AppDatabase
import com.changs.routesearch.data.source.local.MapSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideMapSearchDao(appDatabase: AppDatabase): MapSearchDao {
        return appDatabase.mapSearchDao()
    }
}
