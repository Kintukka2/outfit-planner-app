package com.kintukka.hanger.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.PackCategory
import com.kintukka.hanger.engine.Pattern
import com.kintukka.hanger.engine.Season
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

/** ISO-8601 date serializer used by backups. */
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())
}

@Entity(tableName = "items", indices = [Index("category"), Index("archived")])
@Serializable
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: Category,
    val subcategory: String = "",
    val colors: List<FashionColor> = emptyList(),
    val pattern: Pattern = Pattern.SOLID,
    val formality: Int = 2,
    val warmth: Int = 3,
    val seasons: Set<Season> = emptySet(),
    val sporty: Boolean = false,
    val waterproof: Boolean = false,
    val status: GarmentStatus = GarmentStatus.CLEAN,
    val favorite: Boolean = false,
    val archived: Boolean = false,
    val wishlist: Boolean = false,
    val brand: String = "",
    val size: String = "",
    val material: String = "",
    val price: Double? = null,
    @Serializable(with = LocalDateSerializer::class) val purchaseDate: LocalDate? = null,
    val notes: String = "",
    val tags: List<String> = emptyList(),
    /** File name inside the app's image folder; the cut-out when one exists. */
    val imageFile: String? = null,
    /** Original photo file name, kept so a cut-out can be redone or reverted. */
    val originalImageFile: String? = null,
    val useOriginalImage: Boolean = false,
    val wearsSinceWash: Int = 0,
    val wearsBeforeWash: Int = 1,
    /** Day the item was last washed; wears logged before it don't count towards the next wash. */
    @Serializable(with = LocalDateSerializer::class) val lastWashed: LocalDate? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDemo: Boolean = false,
)

@Entity(tableName = "outfits")
@Serializable
data class OutfitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val occasion: Occasion? = null,
    val favorite: Boolean = false,
    val notes: String = "",
    val source: String = SOURCE_MANUAL,
    val createdAt: Long = System.currentTimeMillis(),
    val isDemo: Boolean = false,
) {
    companion object {
        const val SOURCE_MANUAL = "manual"
        const val SOURCE_SHUFFLE = "shuffle"
        const val SOURCE_SUGGESTED = "suggested"
    }
}

/** An item placed on an outfit canvas. Positions are fractions of the canvas (0..1, centre of the item). */
@Entity(
    tableName = "outfit_items",
    primaryKeys = ["outfitId", "itemId"],
    foreignKeys = [
        ForeignKey(entity = OutfitEntity::class, parentColumns = ["id"], childColumns = ["outfitId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("itemId")],
)
@Serializable
data class OutfitItemEntity(
    val outfitId: Long,
    val itemId: Long,
    val x: Float,
    val y: Float,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val z: Int = 0,
)

/** A planned or worn look on a calendar day. */
@Entity(
    tableName = "day_entries",
    foreignKeys = [
        ForeignKey(entity = OutfitEntity::class, parentColumns = ["id"], childColumns = ["outfitId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = TripEntity::class, parentColumns = ["id"], childColumns = ["tripId"], onDelete = ForeignKey.SET_NULL),
    ],
    indices = [Index("date"), Index("outfitId"), Index("tripId")],
)
@Serializable
data class DayEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate,
    val outfitId: Long? = null,
    val worn: Boolean = false,
    val occasion: Occasion? = null,
    val note: String = "",
    val tripId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isDemo: Boolean = false,
)

@Entity(
    tableName = "day_entry_items",
    primaryKeys = ["entryId", "itemId"],
    foreignKeys = [
        ForeignKey(entity = DayEntryEntity::class, parentColumns = ["id"], childColumns = ["entryId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("itemId")],
)
@Serializable
data class DayEntryItemEntity(val entryId: Long, val itemId: Long)

@Entity(tableName = "trips")
@Serializable
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val destination: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    @Serializable(with = LocalDateSerializer::class) val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class) val endDate: LocalDate,
    val occasions: Set<Occasion> = setOf(Occasion.CASUAL),
    val laundry: Boolean = false,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "packing_items",
    foreignKeys = [
        ForeignKey(entity = TripEntity::class, parentColumns = ["id"], childColumns = ["tripId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("tripId"), Index("itemId")],
)
@Serializable
data class PackingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long,
    val label: String,
    val category: PackCategory,
    val quantity: Int = 1,
    val itemId: Long? = null,
    val packed: Boolean = false,
    val reason: String? = null,
    val sortOrder: Int = 0,
)

@Entity(tableName = "capsules")
@Serializable
data class CapsuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "capsule_items",
    primaryKeys = ["capsuleId", "itemId"],
    foreignKeys = [
        ForeignKey(entity = CapsuleEntity::class, parentColumns = ["id"], childColumns = ["capsuleId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("itemId")],
)
@Serializable
data class CapsuleItemEntity(val capsuleId: Long, val itemId: Long)

/** The file the app shows for an item: the cut-out unless the user chose the original photo. */
val ItemEntity.displayImageFile: String?
    get() = if (useOriginalImage) originalImageFile ?: imageFile else imageFile ?: originalImageFile

/** Aggregated wear stats per item, computed from worn day entries. */
data class ItemWearStats(
    @ColumnInfo(name = "itemId") val itemId: Long,
    @ColumnInfo(name = "wears") val wears: Int,
    @ColumnInfo(name = "lastWorn") val lastWorn: LocalDate?,
)

/** Flat row used to rebuild day entries with their items. */
data class DayEntryItemRow(
    val entryId: Long,
    val itemId: Long,
)
