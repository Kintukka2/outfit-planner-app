package com.kintukka.hanger

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kintukka.hanger.backup.BackupManager
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.db.HangerDatabase
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.data.repo.WardrobeRepository
import com.kintukka.hanger.demo.DemoCloset
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.GarmentStatus
import com.kintukka.hanger.images.ImageStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class RepositoryTest {
    private lateinit var context: Context
    private lateinit var db: HangerDatabase
    private lateinit var images: ImageStore
    private lateinit var repo: WardrobeRepository
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = HangerDatabase.inMemory(context)
        images = ImageStore(context)
        repo = WardrobeRepository(db, images, SettingsRepository(context), scope)
    }

    @After
    fun tearDown() {
        scope.cancel()
        db.close()
    }

    @Test
    fun wearingMovesItemsToLaundryAtThreshold() = runBlocking {
        val tee = repo.addItem(ItemEntity(name = "Tee", category = Category.TOP, colors = listOf(FashionColor.WHITE), wearsBeforeWash = 1))
        val jeans = repo.addItem(ItemEntity(name = "Jeans", category = Category.BOTTOM, colors = listOf(FashionColor.DENIM), wearsBeforeWash = 3))
        val shoes = repo.addItem(ItemEntity(name = "Sneakers", category = Category.SHOES, wearsBeforeWash = 0))

        val entry = repo.logWorn(LocalDate.now(), listOf(tee, jeans, shoes))

        assertEquals(GarmentStatus.DIRTY, db.items().get(tee)!!.status)
        assertEquals(GarmentStatus.CLEAN, db.items().get(jeans)!!.status)
        assertEquals(1, db.items().get(jeans)!!.wearsSinceWash)
        assertEquals(GarmentStatus.CLEAN, db.items().get(shoes)!!.status)

        val stats = db.items().getWearStats().associateBy { it.itemId }
        assertEquals(1, stats[tee]!!.wears)
        assertEquals(LocalDate.now(), stats[tee]!!.lastWorn)

        // Undoing the log restores laundry state.
        repo.deleteEntry(entry)
        assertEquals(GarmentStatus.CLEAN, db.items().get(tee)!!.status)
        assertEquals(0, db.items().get(jeans)!!.wearsSinceWash)
        assertTrue(db.items().getWearStats().isEmpty())

        repo.logWorn(LocalDate.now(), listOf(tee))
        repo.washAll()
        assertEquals(GarmentStatus.CLEAN, db.items().get(tee)!!.status)
    }

    @Test
    fun savedOutfitsKeepPlacementsAndCountWears() = runBlocking {
        val a = repo.addItem(ItemEntity(name = "Shirt", category = Category.TOP))
        val b = repo.addItem(ItemEntity(name = "Chinos", category = Category.BOTTOM))
        val id = repo.saveOutfit(null, "Office", null, OutfitLayout.arrange(listOf(a to Category.TOP, b to Category.BOTTOM)))
        repo.logWorn(LocalDate.now(), listOf(a, b), outfitId = id)
        val outfit = repo.outfits.first { list -> list.any { it.id == id && it.timesWorn == 1 } }.first { it.id == id }
        assertEquals(listOf(a, b).toSet(), outfit.itemIds.toSet())
        assertEquals(1, outfit.timesWorn)
    }

    @Test
    fun backupRoundTripRestoresEverything() = runBlocking {
        DemoCloset.load(db, images)
        val before = db.items().getAll().size
        val outfitsBefore = db.outfits().getAll().size
        val daysBefore = db.days().getAll().size
        assertTrue(before > 20)

        val backup = BackupManager(context, db, images)
        val zip = File(context.cacheDir, "backup-test.zip")
        val exported = backup.export(Uri.fromFile(zip))
        assertEquals(before, exported.items)
        assertTrue(zip.length() > 10_000)

        // Wipe and restore.
        repo.clearDemo()
        assertEquals(0, db.items().getAll().size)
        val restored = backup.restore(Uri.fromFile(zip))
        assertEquals(before, restored.items)
        assertEquals(before, db.items().getAll().size)
        assertEquals(outfitsBefore, db.outfits().getAll().size)
        assertEquals(daysBefore, db.days().getAll().size)
        db.items().getAll().forEach { assertTrue("image restored for ${it.name}", images.file(it.imageFile) != null) }
    }
}
