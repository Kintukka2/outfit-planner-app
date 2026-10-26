package com.kintukka.hanger.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.kintukka.hanger.engine.GarmentStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items")
    suspend fun getAll(): List<ItemEntity>

    @Query("SELECT * FROM items WHERE id = :id")
    fun observe(id: Long): Flow<ItemEntity?>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun get(id: Long): ItemEntity?

    @Query("SELECT * FROM items WHERE id IN (:ids)")
    suspend fun getMany(ids: Collection<Long>): List<ItemEntity>

    @Insert
    suspend fun insert(item: ItemEntity): Long

    @Insert
    suspend fun insertAll(items: List<ItemEntity>): List<Long>

    @Update
    suspend fun update(item: ItemEntity)

    @Update
    suspend fun updateAll(items: List<ItemEntity>)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE items SET status = :status, wearsSinceWash = 0, updatedAt = :now WHERE status = 'DIRTY'")
    suspend fun washAll(status: GarmentStatus = GarmentStatus.CLEAN, now: Long = System.currentTimeMillis())

    @Query(
        """
        SELECT dei.itemId AS itemId, COUNT(*) AS wears, MAX(de.date) AS lastWorn
        FROM day_entry_items dei JOIN day_entries de ON de.id = dei.entryId
        WHERE de.worn = 1
        GROUP BY dei.itemId
        """
    )
    fun observeWearStats(): Flow<List<ItemWearStats>>

    @Query(
        """
        SELECT dei.itemId AS itemId, COUNT(*) AS wears, MAX(de.date) AS lastWorn
        FROM day_entry_items dei JOIN day_entries de ON de.id = dei.entryId
        WHERE de.worn = 1
        GROUP BY dei.itemId
        """
    )
    suspend fun getWearStats(): List<ItemWearStats>

    @Query("DELETE FROM items WHERE isDemo = 1")
    suspend fun deleteDemo()

    @Query("SELECT COUNT(*) FROM items WHERE isDemo = 1")
    fun observeDemoCount(): Flow<Int>
}

@Dao
interface OutfitDao {
    @Query("SELECT * FROM outfits ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<OutfitEntity>>

    @Query("SELECT * FROM outfits")
    suspend fun getAll(): List<OutfitEntity>

    @Query("SELECT * FROM outfit_items")
    fun observeAllPlacements(): Flow<List<OutfitItemEntity>>

    @Query("SELECT * FROM outfit_items")
    suspend fun getAllPlacements(): List<OutfitItemEntity>

    @Query("SELECT * FROM outfits WHERE id = :id")
    suspend fun get(id: Long): OutfitEntity?

    @Query("SELECT * FROM outfit_items WHERE outfitId = :id ORDER BY z")
    suspend fun placements(id: Long): List<OutfitItemEntity>

    @Insert
    suspend fun insert(outfit: OutfitEntity): Long

    @Update
    suspend fun update(outfit: OutfitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacements(items: List<OutfitItemEntity>)

    @Query("DELETE FROM outfit_items WHERE outfitId = :id")
    suspend fun clearPlacements(id: Long)

    @Query("DELETE FROM outfits WHERE id = :id")
    suspend fun delete(id: Long)

    @Transaction
    suspend fun replacePlacements(id: Long, items: List<OutfitItemEntity>) {
        clearPlacements(id)
        insertPlacements(items)
    }

    @Query("DELETE FROM outfits WHERE isDemo = 1")
    suspend fun deleteDemo()
}

@Dao
interface DayDao {
    @Query("SELECT * FROM day_entries ORDER BY date, createdAt")
    fun observeAll(): Flow<List<DayEntryEntity>>

    @Query("SELECT * FROM day_entries")
    suspend fun getAll(): List<DayEntryEntity>

    @Query("SELECT entryId, itemId FROM day_entry_items")
    fun observeAllItems(): Flow<List<DayEntryItemRow>>

    @Query("SELECT * FROM day_entry_items")
    suspend fun getAllItems(): List<DayEntryItemEntity>

    @Query("SELECT * FROM day_entries WHERE date = :date ORDER BY createdAt")
    suspend fun forDate(date: LocalDate): List<DayEntryEntity>

    @Query("SELECT itemId FROM day_entry_items WHERE entryId = :entryId")
    suspend fun itemIds(entryId: Long): List<Long>

    @Query("SELECT * FROM day_entries WHERE id = :id")
    suspend fun get(id: Long): DayEntryEntity?

    @Insert
    suspend fun insert(entry: DayEntryEntity): Long

    @Update
    suspend fun update(entry: DayEntryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItems(items: List<DayEntryItemEntity>)

    @Query("DELETE FROM day_entry_items WHERE entryId = :entryId")
    suspend fun clearItems(entryId: Long)

    @Query("DELETE FROM day_entries WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM day_entries WHERE tripId = :tripId AND worn = 0")
    suspend fun deletePlannedForTrip(tripId: Long)

    @Query("DELETE FROM day_entries WHERE isDemo = 1")
    suspend fun deleteDemo()
}

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY startDate DESC")
    fun observeAll(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips")
    suspend fun getAll(): List<TripEntity>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun observe(id: Long): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun get(id: Long): TripEntity?

    @Insert
    suspend fun insert(trip: TripEntity): Long

    @Update
    suspend fun update(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM packing_items WHERE tripId = :tripId ORDER BY sortOrder, id")
    fun observePacking(tripId: Long): Flow<List<PackingItemEntity>>

    @Query("SELECT * FROM packing_items")
    suspend fun getAllPacking(): List<PackingItemEntity>

    @Query("SELECT * FROM packing_items")
    fun observeAllPacking(): Flow<List<PackingItemEntity>>

    @Insert
    suspend fun insertPacking(items: List<PackingItemEntity>)

    @Update
    suspend fun updatePacking(item: PackingItemEntity)

    @Delete
    suspend fun deletePacking(item: PackingItemEntity)

    @Query("DELETE FROM packing_items WHERE tripId = :tripId")
    suspend fun clearPacking(tripId: Long)
}

@Dao
interface CapsuleDao {
    @Query("SELECT * FROM capsules ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<CapsuleEntity>>

    @Query("SELECT * FROM capsules")
    suspend fun getAll(): List<CapsuleEntity>

    @Query("SELECT * FROM capsule_items")
    fun observeAllItems(): Flow<List<CapsuleItemEntity>>

    @Query("SELECT * FROM capsule_items")
    suspend fun getAllItems(): List<CapsuleItemEntity>

    @Insert
    suspend fun insert(capsule: CapsuleEntity): Long

    @Update
    suspend fun update(capsule: CapsuleEntity)

    @Query("DELETE FROM capsules WHERE id = :id")
    suspend fun delete(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItems(items: List<CapsuleItemEntity>)

    @Query("DELETE FROM capsule_items WHERE capsuleId = :capsuleId AND itemId = :itemId")
    suspend fun removeItem(capsuleId: Long, itemId: Long)
}

/** Raw access used by backup/restore. */
@Dao
interface BackupDao {
    @Upsert suspend fun upsertItems(items: List<ItemEntity>)
    @Upsert suspend fun upsertOutfits(items: List<OutfitEntity>)
    @Upsert suspend fun upsertOutfitItems(items: List<OutfitItemEntity>)
    @Upsert suspend fun upsertTrips(items: List<TripEntity>)
    @Upsert suspend fun upsertDayEntries(items: List<DayEntryEntity>)
    @Upsert suspend fun upsertDayEntryItems(items: List<DayEntryItemEntity>)
    @Upsert suspend fun upsertPacking(items: List<PackingItemEntity>)
    @Upsert suspend fun upsertCapsules(items: List<CapsuleEntity>)
    @Upsert suspend fun upsertCapsuleItems(items: List<CapsuleItemEntity>)

    @Query("DELETE FROM capsule_items") suspend fun clearCapsuleItems()
    @Query("DELETE FROM capsules") suspend fun clearCapsules()
    @Query("DELETE FROM packing_items") suspend fun clearPacking()
    @Query("DELETE FROM day_entry_items") suspend fun clearDayEntryItems()
    @Query("DELETE FROM day_entries") suspend fun clearDayEntries()
    @Query("DELETE FROM trips") suspend fun clearTrips()
    @Query("DELETE FROM outfit_items") suspend fun clearOutfitItems()
    @Query("DELETE FROM outfits") suspend fun clearOutfits()
    @Query("DELETE FROM items") suspend fun clearItems()

    @Transaction
    suspend fun clearEverything() {
        clearCapsuleItems(); clearCapsules(); clearPacking(); clearDayEntryItems(); clearDayEntries()
        clearTrips(); clearOutfitItems(); clearOutfits(); clearItems()
    }
}
