package com.hualee.tcm.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hualee.tcm.global.AppSettings

@Database(
    entities = [
        HerbEntity::class,
        VersionEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun herbsDao(): HerbDao
    abstract fun verDao(): VersionDao
}

object DBUtils {
    private val db by lazy {
        Room.databaseBuilder(
            context = AppSettings.appContext,
            klass = AppDatabase::class.java,
            name = "herbs_db",
        ).build()
    }

    suspend fun insertHerb(herb: HerbEntity) = db.herbsDao().insertHerb(herb)

    suspend fun deleteHerb(herb: HerbEntity) = db.herbsDao().deleteHerb(herb)

    suspend fun updateHerb(herb: HerbEntity) = db.herbsDao().updateHerb(herb)

    suspend fun queryHerbByName(name: String) = db.herbsDao().queryHerbByName(name)

    suspend fun insertVer(ver: Long) = db.verDao().insertVersion(VersionEntity(version = ver))

    suspend fun updateVer(ver: VersionEntity) = db.verDao().updateVersion(ver)

    suspend fun queryVer() = db.verDao().queryVersion()
}