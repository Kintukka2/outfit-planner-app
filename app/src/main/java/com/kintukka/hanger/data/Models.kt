package com.kintukka.hanger.data

import com.kintukka.hanger.data.db.CapsuleEntity
import com.kintukka.hanger.data.db.DayEntryEntity
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.data.db.OutfitItemEntity
import com.kintukka.hanger.data.db.PackingItemEntity
import com.kintukka.hanger.data.db.TripEntity
import com.kintukka.hanger.engine.Garment
import com.kintukka.hanger.engine.StatsCalculator
import java.io.File
import java.time.LocalDate

/** A wardrobe item as the UI sees it: the stored entity plus derived wear stats and image file. */
data class Item(
    val entity: ItemEntity,
    val wears: Int,
    val lastWorn: LocalDate?,
    val image: File?,
) {
    val id: Long get() = entity.id
    val name: String get() = entity.name
    val category get() = entity.category
    val status get() = entity.status

    val costPerWear: Double? get() = StatsCalculator.costPerWear(toGarment())

    fun toGarment(): Garment = Garment(
        id = entity.id,
        category = entity.category,
        subcategory = entity.subcategory,
        colors = entity.colors,
        pattern = entity.pattern,
        formality = entity.formality,
        warmth = entity.warmth,
        seasons = entity.seasons,
        sporty = entity.sporty,
        waterproof = entity.waterproof,
        status = entity.status,
        favorite = entity.favorite,
        wearCount = wears,
        lastWorn = lastWorn,
        price = entity.price,
        added = java.time.Instant.ofEpochMilli(entity.createdAt).atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
        wearsSinceWash = entity.wearsSinceWash,
        wearsBeforeWash = entity.wearsBeforeWash,
    )
}

/** Where an item sits on an outfit canvas (fractions of canvas size). */
data class Placement(
    val itemId: Long,
    val x: Float,
    val y: Float,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val z: Int = 0,
) {
    fun toEntity(outfitId: Long) = OutfitItemEntity(outfitId, itemId, x, y, scale, rotation, z)

    companion object {
        fun from(e: OutfitItemEntity) = Placement(e.itemId, e.x, e.y, e.scale, e.rotation, e.z)
    }
}

data class Outfit(
    val entity: OutfitEntity,
    val placements: List<Placement>,
    val items: List<Item>,
    val timesWorn: Int,
    val lastWorn: LocalDate?,
) {
    val id: Long get() = entity.id
    val name: String get() = entity.name
    val itemIds: List<Long> get() = placements.map { it.itemId }
}

data class DayEntry(
    val entity: DayEntryEntity,
    val itemIds: List<Long>,
) {
    val id: Long get() = entity.id
    val date: LocalDate get() = entity.date
    val worn: Boolean get() = entity.worn
}

data class Trip(
    val entity: TripEntity,
    val packing: List<PackingItemEntity>,
) {
    val id: Long get() = entity.id
    val days: Int get() = (entity.endDate.toEpochDay() - entity.startDate.toEpochDay() + 1).toInt().coerceAtLeast(1)
    val packedCount: Int get() = packing.count { it.packed }
}

data class Capsule(
    val entity: CapsuleEntity,
    val itemIds: List<Long>,
)
