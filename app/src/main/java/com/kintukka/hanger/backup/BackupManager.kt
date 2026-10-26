package com.kintukka.hanger.backup

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.kintukka.hanger.data.db.CapsuleEntity
import com.kintukka.hanger.data.db.CapsuleItemEntity
import com.kintukka.hanger.data.db.DayEntryEntity
import com.kintukka.hanger.data.db.DayEntryItemEntity
import com.kintukka.hanger.data.db.HangerDatabase
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.data.db.OutfitEntity
import com.kintukka.hanger.data.db.OutfitItemEntity
import com.kintukka.hanger.data.db.PackingItemEntity
import com.kintukka.hanger.data.db.TripEntity
import com.kintukka.hanger.images.ImageStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

@Serializable
data class BackupData(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val items: List<ItemEntity>,
    val outfits: List<OutfitEntity>,
    val outfitItems: List<OutfitItemEntity>,
    val trips: List<TripEntity>,
    val dayEntries: List<DayEntryEntity>,
    val dayEntryItems: List<DayEntryItemEntity>,
    val packing: List<PackingItemEntity>,
    val capsules: List<CapsuleEntity>,
    val capsuleItems: List<CapsuleItemEntity>,
)

data class BackupSummary(val items: Int, val outfits: Int, val days: Int, val images: Int)

/**
 * Whole-closet backup as a single .zip (data.json + images/). The user picks where it goes
 * (Drive, Downloads, ...), so nothing ever leaves the phone unless they choose to.
 */
class BackupManager(
    private val context: Context,
    private val db: HangerDatabase,
    private val images: ImageStore,
) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    suspend fun export(target: Uri): BackupSummary = withContext(Dispatchers.IO) {
        val data = BackupData(
            items = db.items().getAll(),
            outfits = db.outfits().getAll(),
            outfitItems = db.outfits().getAllPlacements(),
            trips = db.trips().getAll(),
            dayEntries = db.days().getAll(),
            dayEntryItems = db.days().getAllItems(),
            packing = db.trips().getAllPacking(),
            capsules = db.capsules().getAll(),
            capsuleItems = db.capsules().getAllItems(),
        )
        val referenced = data.items.flatMap { listOfNotNull(it.imageFile, it.originalImageFile) }.toSet()
        var imageCount = 0
        context.contentResolver.openOutputStream(target)?.use { out ->
            ZipOutputStream(out.buffered()).use { zip ->
                zip.putNextEntry(ZipEntry(DATA_ENTRY))
                zip.write(json.encodeToString(data).toByteArray())
                zip.closeEntry()
                referenced.mapNotNull(images::file).forEach { file ->
                    zip.putNextEntry(ZipEntry("$IMAGES_DIR${file.name}"))
                    file.inputStream().use { it.copyTo(zip) }
                    zip.closeEntry()
                    imageCount++
                }
            }
        } ?: error("Could not open the backup destination")
        BackupSummary(data.items.size, data.outfits.size, data.dayEntries.size, imageCount)
    }

    /** Replaces everything on this phone with the backup's contents. */
    suspend fun restore(source: Uri): BackupSummary = withContext(Dispatchers.IO) {
        val staging = File(context.cacheDir, "restore").apply { deleteRecursively(); mkdirs() }
        var data: BackupData? = null
        context.contentResolver.openInputStream(source)?.use { input ->
            ZipInputStream(input.buffered()).use { zip ->
                while (true) {
                    val entry = zip.nextEntry ?: break
                    val name = entry.name
                    when {
                        name == DATA_ENTRY -> data = json.decodeFromString<BackupData>(zip.readBytes().decodeToString())
                        name.startsWith(IMAGES_DIR) && !entry.isDirectory -> {
                            val fileName = File(name).name
                            // Only plain file names; never follow paths out of the staging folder.
                            if (fileName.isNotBlank() && !fileName.contains("..")) {
                                File(staging, fileName).outputStream().use { zip.copyTo(it) }
                            }
                        }
                    }
                    zip.closeEntry()
                }
            }
        } ?: error("Could not open the backup file")
        val backup = data ?: error("This file isn't a Hanger backup")

        db.withTransaction {
            val b = db.backup()
            b.clearEverything()
            b.upsertItems(backup.items)
            b.upsertOutfits(backup.outfits)
            b.upsertOutfitItems(backup.outfitItems)
            b.upsertTrips(backup.trips)
            b.upsertDayEntries(backup.dayEntries)
            b.upsertDayEntryItems(backup.dayEntryItems)
            b.upsertPacking(backup.packing)
            b.upsertCapsules(backup.capsules)
            b.upsertCapsuleItems(backup.capsuleItems)
        }
        val keep = backup.items.flatMap { listOfNotNull(it.imageFile, it.originalImageFile) }.toSet()
        images.allFiles().filter { it.name !in keep }.forEach { it.delete() }
        var restoredImages = 0
        staging.listFiles()?.forEach { f ->
            if (f.name in keep) { images.importFile(f, f.name); restoredImages++ }
        }
        staging.deleteRecursively()
        BackupSummary(backup.items.size, backup.outfits.size, backup.dayEntries.size, restoredImages)
    }

    private companion object {
        const val DATA_ENTRY = "data.json"
        const val IMAGES_DIR = "images/"
    }
}
