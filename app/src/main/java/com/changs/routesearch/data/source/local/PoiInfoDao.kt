package com.changs.routesearch.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.changs.routesearch.data.model.PoiInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiInfoDao {
    @Query("SELECT * FROM poi ORDER BY timestamp DESC LIMIT 5")
    fun getPoiInfos(): Flow<List<PoiInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoiInfo(poi: PoiInfo)

    @Query("SELECT COUNT(*) FROM poi")
    suspend fun getItemCount(): Int

    @Query("DELETE FROM poi WHERE id IN (SELECT id FROM poi ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteOldest()
}