package com.kintukka.hanger.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SubcategoriesTest {

    @Test
    fun everyCategoryHasPresets() {
        for (c in Category.entries) assertTrue("$c has presets", Subcategories.forCategory(c).isNotEmpty())
        assertEquals(Subcategories.all().size, Category.entries.sumOf { Subcategories.forCategory(it).size })
    }

    @Test
    fun presetsAreValid() {
        val all = Subcategories.all()
        assertEquals("names are unique", all.size, all.map { it.name.lowercase() }.toSet().size)
        for (p in all) {
            assertTrue(p.name, p.formality in Formality.range)
            assertTrue(p.name, p.warmth in Warmth.range)
            assertTrue(p.name, p.wearsBeforeWash >= 0)
        }
        for (c in listOf(Category.SHOES, Category.BAG)) {
            assertTrue(Subcategories.forCategory(c).all { it.wearsBeforeWash == 0 })
        }
        for (name in listOf("Watch", "Necklace", "Earrings", "Bracelet", "Ring", "Belt", "Sunglasses")) {
            assertEquals(name, 0, Subcategories.find(name)!!.wearsBeforeWash)
        }
    }

    private fun assertPreset(name: String, category: Category, formality: Int, warmth: Int, wash: Int) {
        val p = Subcategories.find(name)
        assertNotNull(name, p)
        assertEquals(category, p!!.category)
        assertEquals("$name formality", formality, p.formality)
        assertEquals("$name warmth", warmth, p.warmth)
        assertEquals("$name wearsBeforeWash", wash, p.wearsBeforeWash)
    }

    @Test
    fun curatedValues() {
        assertPreset("T-shirt", Category.TOP, 2, 2, 1)
        assertPreset("Tank top", Category.TOP, 2, 1, 1)
        assertPreset("Shirt", Category.TOP, 3, 2, 2)
        assertPreset("Hoodie", Category.TOP, 1, 3, 3)
        assertPreset("Jeans", Category.BOTTOM, 2, 3, 4)
        assertPreset("Trousers", Category.BOTTOM, 4, 3, 3)
        assertPreset("Shorts", Category.BOTTOM, 2, 1, 2)
        assertPreset("Dress", Category.ONE_PIECE, 3, 2, 2)
        assertPreset("Knit sweater", Category.LAYER, 2, 4, 4)
        assertPreset("Blazer", Category.LAYER, 4, 3, 8)
        assertPreset("Coat", Category.OUTERWEAR, 4, 5, 15)
        assertPreset("Puffer jacket", Category.OUTERWEAR, 2, 5, 20)
        assertPreset("Dress shoes", Category.SHOES, 5, 2, 0)
        assertPreset("Clutch", Category.BAG, 4, 2, 0)
        assertPreset("Tie", Category.ACCESSORY, 4, 1, 0)

        assertTrue(Subcategories.find("Trench coat")!!.waterproof)
        assertTrue(Subcategories.find("Raincoat")!!.waterproof)
        assertTrue(Subcategories.find("Rain boots")!!.waterproof)
        assertTrue(Subcategories.find("Running shoes")!!.sporty)
        assertTrue(Subcategories.find("Leggings")!!.sporty)
        assertTrue(Subcategories.find("Windbreaker")!!.sporty)
        assertFalse(Subcategories.find("Sneakers")!!.sporty)
        assertEquals(setOf(Season.SUMMER), Subcategories.find("Shorts")!!.seasons)
        assertEquals(setOf(Season.SUMMER), Subcategories.find("Sandals")!!.seasons)
        assertEquals(setOf(Season.SPRING, Season.SUMMER), Subcategories.find("Tank top")!!.seasons)
        val scarf = Subcategories.find("Scarf")!!
        assertEquals(4, scarf.warmth)
        assertTrue(Season.WINTER in scarf.seasons && Season.AUTUMN in scarf.seasons)
        assertTrue(Subcategories.find("Jeans")!!.seasons.isEmpty())
    }

    @Test
    fun findIsCaseInsensitive() {
        assertEquals("T-shirt", Subcategories.find("t-SHIRT")?.name)
        assertEquals("Jeans", Subcategories.find("  jeans ")?.name)
        assertEquals("Chelsea boots", Subcategories.find("CHELSEA BOOTS")?.name)
        assertEquals("Knit sweater", Subcategories.find("Jumper")?.name) // common alias
        assertEquals("Sneakers", Subcategories.find("trainers")?.name)
        assertNull(Subcategories.find("Spacesuit"))
        assertNull(Subcategories.find(""))
    }
}

class CategoryGuesserTest {
    private fun guess(vararg labels: Pair<String, Float>) = CategoryGuesser.guess(labels.toList())?.name

    @Test
    fun recognisesSpecificGarments() {
        assertEquals("Jeans", guess("Jeans" to 0.9f, "Clothing" to 0.95f))
        assertEquals("Dress", guess("DRESS" to 0.8f))
        assertEquals("Shorts", guess("Shorts" to 0.7f, "Textile" to 0.9f))
        assertEquals("Skirt", guess("Skirt" to 0.6f))
        assertEquals("Knit sweater", guess("Sweater" to 0.8f))
        assertEquals("Coat", guess("Coat" to 0.8f))
        assertEquals("Sunglasses", guess("Sunglasses" to 0.9f))
        assertEquals("Scarf", guess("Scarf" to 0.7f))
        assertEquals("Watch", guess("Watch" to 0.8f))
        assertEquals("Tie", guess("Tie" to 0.8f))
        assertEquals("Socks", guess("Sock" to 0.6f))
        assertEquals("Swimwear", guess("Swimwear" to 0.7f))
        assertEquals("Blazer", guess("Suit" to 0.8f))
        assertEquals("Hat", guess("Hat" to 0.8f))
        assertEquals("Necklace", guess("Jewellery" to 0.7f))
        assertEquals(Category.ACCESSORY, CategoryGuesser.guess(listOf("Jewelry" to 0.7f))?.category)
    }

    @Test
    fun prefersSpecificOverGenericLabels() {
        assertEquals("Sneakers", guess("Shoe" to 0.9f, "Sneakers" to 0.6f))
        assertEquals("Boots", guess("Shoe" to 0.9f, "Boot" to 0.7f))
        assertEquals("T-shirt", guess("Top" to 0.9f, "T-shirt" to 0.6f))
        assertEquals("Shirt", guess("Top" to 0.9f, "Shirt" to 0.6f))
        assertEquals("Backpack", guess("Bag" to 0.8f, "Backpack" to 0.7f))
        assertEquals("Handbag", guess("Handbag" to 0.8f, "Bag" to 0.9f))
        assertEquals("Denim jacket", guess("Jacket" to 0.85f, "Denim" to 0.7f))
        assertEquals("Leather jacket", guess("Jacket" to 0.85f, "Leather" to 0.6f))
        assertEquals("Jacket", guess("Jacket" to 0.85f))
    }

    @Test
    fun genericOnlyLabelsGiveNull() {
        assertNull(guess("Clothing" to 0.95f, "Textile" to 0.9f))
        assertNull(guess("Fashion" to 0.9f, "Pattern" to 0.7f))
        assertNull(guess())
        assertNull(guess("Hat" to 0.1f)) // too unsure
        assertNull(guess("Dog" to 0.99f, "Grass" to 0.8f))
    }
}

class LaundryRulesTest {

    @Test
    fun defaultsComeFromPresetsThenCategory() {
        assertEquals(1, LaundryRules.defaultWearsBeforeWash(Category.TOP, "T-shirt"))
        assertEquals(4, LaundryRules.defaultWearsBeforeWash(Category.BOTTOM, "jeans"))
        assertEquals(8, LaundryRules.defaultWearsBeforeWash(Category.LAYER, "Blazer"))
        assertEquals(0, LaundryRules.defaultWearsBeforeWash(Category.SHOES, "Sneakers"))
        assertEquals(1, LaundryRules.defaultWearsBeforeWash(Category.TOP))
        assertEquals(3, LaundryRules.defaultWearsBeforeWash(Category.BOTTOM, "Mystery bottoms"))
        assertEquals(2, LaundryRules.defaultWearsBeforeWash(Category.ONE_PIECE))
        assertEquals(4, LaundryRules.defaultWearsBeforeWash(Category.LAYER))
        assertEquals(15, LaundryRules.defaultWearsBeforeWash(Category.OUTERWEAR))
        assertEquals(0, LaundryRules.defaultWearsBeforeWash(Category.SHOES))
        assertEquals(0, LaundryRules.defaultWearsBeforeWash(Category.BAG))
        assertEquals(0, LaundryRules.defaultWearsBeforeWash(Category.ACCESSORY))
        assertEquals(1, LaundryRules.defaultWearsBeforeWash(Category.OTHER))
    }

    @Test
    fun jeansGoDirtyOnTheFourthWear() {
        var jeans = TestCloset.item(1, "Jeans", FashionColor.DENIM)
        val day = TestCloset.TODAY
        repeat(3) { i ->
            jeans = LaundryRules.afterWear(jeans, day.plusDays(i.toLong()))
            assertEquals(GarmentStatus.CLEAN, jeans.status)
        }
        assertEquals(3, jeans.wearsSinceWash)
        jeans = LaundryRules.afterWear(jeans, day.plusDays(3))
        assertEquals(GarmentStatus.DIRTY, jeans.status)
        assertEquals(4, jeans.wearCount)
        assertEquals(4, jeans.wearsSinceWash)
        assertEquals(day.plusDays(3), jeans.lastWorn)
    }

    @Test
    fun teeIsDirtyAfterOneWear() {
        val tee = LaundryRules.afterWear(TestCloset.item(1, "T-shirt", FashionColor.WHITE), TestCloset.TODAY)
        assertEquals(GarmentStatus.DIRTY, tee.status)
        assertEquals(1, tee.wearCount)
        assertEquals(TestCloset.TODAY, tee.lastWorn)
    }

    @Test
    fun shoesNeverGoDirty() {
        var shoes = TestCloset.item(1, "Sneakers", FashionColor.WHITE)
        repeat(50) { shoes = LaundryRules.afterWear(shoes, TestCloset.TODAY) }
        assertEquals(GarmentStatus.CLEAN, shoes.status)
        assertEquals(50, shoes.wearCount)
    }

    @Test
    fun washingResetsCounters() {
        val dirty = TestCloset.item(1, "Jeans").copy(status = GarmentStatus.DIRTY, wearsSinceWash = 4)
        val clean = LaundryRules.washed(dirty)
        assertEquals(GarmentStatus.CLEAN, clean.status)
        assertEquals(0, clean.wearsSinceWash)

        val away = TestCloset.item(2, "Coat").copy(status = GarmentStatus.AWAY, wearsSinceWash = 3)
        val washedAway = LaundryRules.washed(away)
        assertEquals(GarmentStatus.AWAY, washedAway.status)
        assertEquals(0, washedAway.wearsSinceWash)
    }
}
