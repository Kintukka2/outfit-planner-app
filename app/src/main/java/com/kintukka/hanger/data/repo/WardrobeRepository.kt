package com.kintukka.hanger.data.repo

import androidx.room.withTransaction
import com.kintukka.hanger.data.Capsule
import com.kintukka.hanger.data.DayEntry
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.Outfit
import com.kintukka.hanger.data.Placement
import com.kintukka.hanger.data.Trip
import com.kintukka.hanger.data.db.CapsuleEntity
import com.kintukka.hanger.data.db.CapsuleItemEntity
import com.kintukka.hanger.data.db.DayEntryEntity
import com.kintukka.hanger.data.db.DayEntryItemEntity
import com.kintukka.hanger.data.db.HangerDatabase
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.data.db.PackingItemEntity
import com.kintukka.hanger.data.db.TripEntity
import com.kintukka.hanger.engine.FeedbackEvent
import com.kintukka.hanger.engine.FeedbackLearner
import com.kintukka.hanger.engine.FeedbackType
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.LaundryRules
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.PackingPlan
import com.kintukka.hanger.images.ImageStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

/** Single source of truth for wardrobe data. All writes go through here. */
class WardrobeRepository(
    private val db: HangerDatabase,
    private val images: ImageStore,
    private val settings: SettingsRepository,
    scope: CoroutineScope,
) {
    private val itemDao = db.items()
    private val outfitDao = db.outfits()
    private val dayDao = db.days()
    private val tripDao = db.trips()
    private val capsuleDao = db.capsules()

    /** Every item including archived and wishlist ones; screens filter as needed. */
    val allItems: StateFlow<List<Item>> = combine(itemDao.observeAll(), itemDao.observeWearStats()) { entities, stats ->
        val byId = stats.associateBy { it.itemId }
        entities.map { e ->
            val s = byId[e.id]
            Item(e, s?.wears ?: 0, s?.lastWorn, images.file(if (e.useOriginalImage) e.originalImageFile else e.imageFile ?: e.originalImageFile))
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    /** Items you own and can wear (not archived, not wishlist). */
    val closet: Flow<List<Item>> = allItems.map { list -> list.filter { !it.entity.archived && !it.entity.wishlist } }

    val dayEntries: StateFlow<List<DayEntry>> = combine(dayDao.observeAll(), dayDao.observeAllItems()) { entries, rows ->
        val byEntry = rows.groupBy({ it.entryId }, { it.itemId })
        entries.map { DayEntry(it, byEntry[it.id].orEmpty()) }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    val outfits: StateFlow<List<Outfit>> = combine(
        outfitDao.observeAll(), outfitDao.observeAllPlacements(), allItems, dayEntries,
    ) { outfits, placements, items, days ->
        val itemsById = items.associateBy { it.id }
        val byOutfit = placements.groupBy { it.outfitId }
        val wornByOutfit = days.filter { it.worn && it.entity.outfitId != null }.groupBy { it.entity.outfitId!! }
        outfits.map { o ->
            val p = byOutfit[o.id].orEmpty().sortedBy { it.z }.map(Placement::from)
            val worn = wornByOutfit[o.id].orEmpty()
            Outfit(o, p, p.mapNotNull { itemsById[it.itemId] }, worn.size, worn.maxOfOrNull { it.date })
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    val trips: Flow<List<Trip>> = combine(tripDao.observeAll(), tripDao.observeAllPacking()) { trips, packing ->
        val byTrip = packing.groupBy { it.tripId }
        trips.map { Trip(it, byTrip[it.id].orEmpty().sortedWith(compareBy({ it.sortOrder }, { it.id }))) }
    }

    val capsules: Flow<List<Capsule>> = combine(capsuleDao.observeAll(), capsuleDao.observeAllItems()) { caps, rows ->
        val byCap = rows.groupBy({ it.capsuleId }, { it.itemId })
        caps.map { Capsule(it, byCap[it.id].orEmpty()) }
    }

    fun trip(id: Long): Flow<Trip?> = trips.map { list -> list.firstOrNull { it.id == id } }

    // ---- Items ----

    suspend fun addItems(items: List<ItemEntity>): List<Long> = itemDao.insertAll(items)

    suspend fun addItem(item: ItemEntity): Long = itemDao.insert(item)

    suspend fun updateItem(item: ItemEntity) = itemDao.update(item.copy(updatedAt = System.currentTimeMillis()))

    suspend fun item(id: Long): ItemEntity? = itemDao.get(id)

    suspend fun deleteItem(id: Long) {
        val item = itemDao.get(id) ?: return
        itemDao.delete(id)
        images.delete(item.imageFile)
        images.delete(item.originalImageFile)
    }

    suspend fun setStatus(id: Long, status: GarmentStatus) {
        val item = itemDao.get(id) ?: return
        val resetCount = status == GarmentStatus.CLEAN && item.status == GarmentStatus.DIRTY
        updateItem(item.copy(status = status, wearsSinceWash = if (resetCount) 0 else item.wearsSinceWash))
    }

    suspend fun toggleFavorite(id: Long) {
        val item = itemDao.get(id) ?: return
        updateItem(item.copy(favorite = !item.favorite))
    }

    suspend fun setArchived(id: Long, archived: Boolean) {
        val item = itemDao.get(id) ?: return
        updateItem(item.copy(archived = archived))
    }

    suspend fun washAll() = itemDao.washAll()

    // ---- Outfits ----

    suspend fun saveOutfit(
        id: Long?,
        name: String,
        occasion: Occasion?,
        placements: List<Placement>,
        source: String = OutfitEntity.SOURCE_MANUAL,
        favorite: Boolean = false,
    ): Long = db.withTransaction {
        val outfitId = if (id != null && id > 0) {
            val existing = outfitDao.get(id)
            if (existing != null) {
                outfitDao.update(existing.copy(name = name, occasion = occasion, favorite = favorite || existing.favorite))
                id
            } else {
                outfitDao.insert(OutfitEntity(name = name, occasion = occasion, source = source, favorite = favorite))
            }
        } else {
            outfitDao.insert(OutfitEntity(name = name, occasion = occasion, source = source, favorite = favorite))
        }
        outfitDao.replacePlacements(outfitId, placements.mapIndexed { i, p -> p.copy(z = i).toEntity(outfitId) })
        outfitId
    }

    suspend fun deleteOutfit(id: Long) = outfitDao.delete(id)

    suspend fun toggleOutfitFavorite(id: Long) {
        val o = outfitDao.get(id) ?: return
        outfitDao.update(o.copy(favorite = !o.favorite))
    }

    suspend fun renameOutfit(id: Long, name: String, occasion: Occasion?) {
        val o = outfitDao.get(id) ?: return
        outfitDao.update(o.copy(name = name, occasion = occasion))
    }

    // ---- Calendar ----

    suspend fun plan(date: LocalDate, itemIds: List<Long>, outfitId: Long? = null, occasion: Occasion? = null, tripId: Long? = null): Long =
        db.withTransaction {
            val entryId = dayDao.insert(DayEntryEntity(date = date, outfitId = outfitId, occasion = occasion, tripId = tripId))
            dayDao.insertItems(itemIds.distinct().map { DayEntryItemEntity(entryId, it) })
            entryId
        }

    /** Log an outfit as worn on [date] and update laundry state. */
    suspend fun logWorn(date: LocalDate, itemIds: List<Long>, outfitId: Long? = null, occasion: Occasion? = null): Long {
        val entryId = plan(date, itemIds, outfitId, occasion)
        setWorn(entryId, true)
        recordFeedback(FeedbackType.WORN, itemIds)
        return entryId
    }

    suspend fun setWorn(entryId: Long, worn: Boolean) {
        val entry = dayDao.get(entryId) ?: return
        if (entry.worn == worn) return
        val autoLaundry = settings.current().autoLaundry
        db.withTransaction {
            dayDao.update(entry.copy(worn = worn))
            val items = itemDao.getMany(dayDao.itemIds(entryId))
            val updated = items.map { item ->
                if (worn) {
                    val next = LaundryRules.afterWear(itemToGarment(item), entry.date)
                    item.copy(
                        wearsSinceWash = next.wearsSinceWash,
                        status = if (autoLaundry) next.status else item.status,
                        updatedAt = System.currentTimeMillis(),
                    )
                } else {
                    val since = (item.wearsSinceWash - 1).coerceAtLeast(0)
                    val revert = item.status == GarmentStatus.DIRTY && (item.wearsBeforeWash == 0 || since < item.wearsBeforeWash)
                    item.copy(
                        wearsSinceWash = since,
                        status = if (revert) GarmentStatus.CLEAN else item.status,
                        updatedAt = System.currentTimeMillis(),
                    )
                }
            }
            itemDao.updateAll(updated)
        }
    }

    suspend fun deleteEntry(entryId: Long) {
        val entry = dayDao.get(entryId) ?: return
        if (entry.worn) setWorn(entryId, false)
        dayDao.delete(entryId)
    }

    suspend fun updateEntryNote(entryId: Long, note: String) {
        val entry = dayDao.get(entryId) ?: return
        dayDao.update(entry.copy(note = note))
    }

    // ---- Feedback ----

    suspend fun recordFeedback(type: FeedbackType, itemIds: List<Long>) {
        if (itemIds.isEmpty()) return
        runCatching { settings.updateAffinity { FeedbackLearner.apply(it, FeedbackEvent(type, itemIds)) } }
    }

    // ---- Trips ----

    suspend fun saveTrip(trip: TripEntity): Long =
        if (trip.id > 0) { tripDao.update(trip); trip.id } else tripDao.insert(trip)

    suspend fun deleteTrip(id: Long) = db.withTransaction {
        dayDao.deletePlannedForTrip(id)
        tripDao.delete(id)
    }

    suspend fun tripEntity(id: Long): TripEntity? = tripDao.get(id)

    /** Replace a trip's packing list and planned outfits with a fresh plan. Keeps "packed" ticks for lines that survive. */
    suspend fun applyPackingPlan(tripId: Long, plan: PackingPlan) = db.withTransaction {
        val trip = tripDao.get(tripId) ?: return@withTransaction
        val previouslyPacked = tripDao.getAllPacking().filter { it.tripId == tripId && it.packed }
            .map { it.itemId to it.label }.toSet()
        tripDao.clearPacking(tripId)
        tripDao.insertPacking(plan.items.mapIndexed { i, line ->
            PackingItemEntity(
                tripId = tripId,
                label = line.label,
                category = line.category,
                quantity = line.quantity,
                itemId = line.garmentId,
                reason = line.reason,
                sortOrder = i,
                packed = (line.garmentId to line.label) in previouslyPacked,
            )
        })
        dayDao.deletePlannedForTrip(tripId)
        plan.outfits.forEachIndexed { index, ids ->
            val date = trip.startDate.plusDays(index.toLong())
            if (!date.isAfter(trip.endDate) && ids.isNotEmpty()) plan(date, ids, tripId = tripId)
        }
    }

    suspend fun setPacked(item: PackingItemEntity, packed: Boolean) = tripDao.updatePacking(item.copy(packed = packed))

    suspend fun addPackingLine(tripId: Long, label: String, category: com.kintukka.hanger.engine.PackCategory) =
        tripDao.insertPacking(listOf(PackingItemEntity(tripId = tripId, label = label, category = category, sortOrder = 10_000)))

    suspend fun deletePackingLine(item: PackingItemEntity) = tripDao.deletePacking(item)

    // ---- Capsules ----

    suspend fun createCapsule(name: String, description: String, itemIds: List<Long>): Long = db.withTransaction {
        val id = capsuleDao.insert(CapsuleEntity(name = name, description = description))
        capsuleDao.insertItems(itemIds.map { CapsuleItemEntity(id, it) })
        id
    }

    suspend fun setCapsuleItems(capsuleId: Long, itemIds: Set<Long>, current: Set<Long>) = db.withTransaction {
        (current - itemIds).forEach { capsuleDao.removeItem(capsuleId, it) }
        capsuleDao.insertItems((itemIds - current).map { CapsuleItemEntity(capsuleId, it) })
    }

    suspend fun renameCapsule(capsule: CapsuleEntity) = capsuleDao.update(capsule)

    suspend fun deleteCapsule(id: Long) = capsuleDao.delete(id)

    // ---- Demo data ----

    val demoItemCount: Flow<Int> = itemDao.observeDemoCount()

    suspend fun clearDemo() = db.withTransaction {
        val demoFiles = itemDao.getAll().filter { it.isDemo }.flatMap { listOfNotNull(it.imageFile, it.originalImageFile) }
        dayDao.deleteDemo()
        outfitDao.deleteDemo()
        itemDao.deleteDemo()
        demoFiles.forEach(images::delete)
    }

    /** Fresh read straight from the database (safe on a cold start, before flows have emitted). */
    suspend fun snapshotItems(): List<Item> {
        val stats = itemDao.getWearStats().associateBy { it.itemId }
        return itemDao.getAll().map { e ->
            val s = stats[e.id]
            Item(e, s?.wears ?: 0, s?.lastWorn, images.file(if (e.useOriginalImage) e.originalImageFile else e.imageFile ?: e.originalImageFile))
        }
    }

    suspend fun snapshotCloset(): List<Item> = snapshotItems().filter { !it.entity.archived && !it.entity.wishlist }

    suspend fun snapshotDayEntries(): List<DayEntry> {
        val byEntry = dayDao.getAllItems().groupBy({ it.entryId }, { it.itemId })
        return dayDao.getAll().map { DayEntry(it, byEntry[it.id].orEmpty()) }
    }

    private fun itemToGarment(e: ItemEntity) = Item(e, 0, null, null).toGarment()
}
