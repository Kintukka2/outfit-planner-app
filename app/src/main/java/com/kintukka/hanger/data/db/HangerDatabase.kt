package com.kintukka.hanger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ItemEntity::class,
        OutfitEntity::class,
        OutfitItemEntity::class,
        DayEntryEntity::class,
        DayEntryItemEntity::class,
        TripEntity::class,
        PackingItemEntity::class,
        CapsuleEntity::class,
        CapsuleItemEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class HangerDatabase : RoomDatabase() {
    abstract fun items(): ItemDao
    abstract fun outfits(): OutfitDao
    abstract fun days(): DayDao
    abstract fun trips(): TripDao
    abstract fun capsules(): CapsuleDao
    abstract fun backup(): BackupDao

    companion object {
        fun build(context: Context): HangerDatabase =
            Room.databaseBuilder(context, HangerDatabase::class.java, "hanger.db").build()

        fun inMemory(context: Context): HangerDatabase =
            Room.inMemoryDatabaseBuilder(context, HangerDatabase::class.java).allowMainThreadQueries().build()
    }
}
