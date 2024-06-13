package com.changs.routesearch.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.changs.routesearch.data.model.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface MapSearchDao {
    @Query("SELECT * FROM recent_search ORDER BY timestamp DESC LIMIT 5")
    fun getRecentSearchs(): Flow<List<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearchKeyword(search: RecentSearch)

    @Query("DELETE FROM recent_search WHERE name = :name")
    suspend fun deleteSearchByName(name: String)

    @Query("SELECT COUNT(*) FROM recent_search")
    suspend fun getItemCount(): Int

    @Query("DELETE FROM recent_search WHERE name IN (SELECT name FROM recent_search ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteOldest()
}