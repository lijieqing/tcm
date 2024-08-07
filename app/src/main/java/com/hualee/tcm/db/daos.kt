package com.hualee.tcm.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HerbDao {

    @Insert
    suspend fun insertHerb(herb: HerbEntity)

    @Delete
    suspend fun deleteHerb(herb: HerbEntity)

    @Update
    suspend fun updateHerb(herb: HerbEntity)

    @Query("SELECT * FROM herbs WHERE name = :herbName")
    suspend fun queryHerbByName(herbName: String): List<HerbEntity>

    @Query("SELECT * FROM herbs WHERE name LIKE '%' || :herbName || '%'")
    suspend fun queryHerbByNameLike(herbName: String): List<HerbEntity>

    @Query("SELECT * FROM herbs WHERE name LIKE '%' || :herbName || '%'")
    fun queryHerbByNameLikeWithPaging(herbName: String): PagingSource<Int, HerbEntity>

    @Query("SELECT * FROM herbs WHERE effect LIKE '%' || :herbEffect || '%'")
    fun queryHerbByEffectLikeWithPaging(herbEffect: String): PagingSource<Int, HerbEntity>

    @Query("SELECT * FROM herbs WHERE feature LIKE '%' || :herbFeature || '%'")
    fun queryHerbByFeatureLikeWithPaging(herbFeature: String): PagingSource<Int, HerbEntity>

    @Query("SELECT * FROM herbs")
    fun queryAllHerbWithPaging(): PagingSource<Int, HerbEntity>
}

@Dao
interface VersionDao {

    @Insert
    suspend fun insertVersion(ver: VersionEntity)

    @Update
    suspend fun updateVersion(ver: VersionEntity)

    @Query("SELECT * FROM version")
    suspend fun queryVersion(): List<VersionEntity>
}