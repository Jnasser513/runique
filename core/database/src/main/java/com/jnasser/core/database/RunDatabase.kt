package com.jnasser.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jnasser.core.database.dao.RunDao
import com.jnasser.core.database.dao.RunPendingSyncDao
import com.jnasser.core.database.entity.DeletedRunSyncEntity
import com.jnasser.core.database.entity.RunEntity
import com.jnasser.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
}