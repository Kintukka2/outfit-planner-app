package com.kintukka.hanger.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class ColorToolsTest {

    @Test
    fun namesObviousColours() {
        assertEquals(FashionColor.BLACK, ColorTools.nameFor(0x000000))
        assertEquals(FashionColor.WHITE, ColorTools.nameFor(0xFFFFFF))
        assertEquals(FashionColor.NAVY, ColorTools.nameFor(0x1B2440))
        assertEquals(FashionColor.NAVY, ColorTools.nameFor(0x000080)) // CSS "navy"
        assertEquals(FashionColor.RED, ColorTools.nameFor(0xFF0000))
        assertEquals(FashionColor.RED, ColorTools.nameFor(0xCC0000))
        assertEquals(FashionColor.DENIM, ColorTools.nameFor(0x4F6D8F))
        assertEquals(FashionColor.GREY, ColorTools.nameFor(0x808080))
        assertEquals(FashionColor.ORANGE, ColorTools.nameFor(0xFF8000))
        assertEquals(FashionColor.PINK, ColorTools.nameFor(0xFFC0CB))
    }

    @Test
    fun ignoresAlphaChannel() {
        assertEquals(FashionColor.BLACK, ColorTools.nameFor(0xFF000000.toInt()))
        assertEquals(FashionColor.WHITE, ColorTools.nameFor(0x00FFFFFF))
        assertEquals(FashionColor.NAVY, ColorTools.nameFor(0x801B2440.toInt()))
    }

    @Test
    fun everyPaletteColourMapsToItself() {
        for (c in FashionColor.entries) assertEquals(c, ColorTools.nameFor(c.rgb))
    }

    /** 100x100 image: 10px transparent border, inside 70% navy rows then 30% white rows. */
    private fun navyWhiteImage(noise: Int = 0, seed: Int = 1): IntArray {
        val rnd = Random(seed)
        val size = 100
        val inner = 80
        val navyRows = (inner * 0.7).toInt()
        fun jitter(rgb: Int): Int {
            if (noise == 0) return rgb
            fun ch(v: Int) = (v + rnd.nextInt(-noise, noise + 1)).coerceIn(0, 255)
            return (ch(rgb shr 16 and 0xFF) shl 16) or (ch(rgb shr 8 and 0xFF) shl 8) or ch(rgb and 0xFF)
        }
        return IntArray(size * size) { i ->
            val x = i % size
            val y = i / size
            if (x < 10 || y < 10 || x >= 90 || y >= 90) {
                0x00FF0000 // fully transparent red: must be ignored
            } else {
                val rgb = if (y - 10 < navyRows) 0x1C2541 else 0xF8F8F8
                (0xFF shl 24) or jitter(rgb)
            }
        }
    }

    @Test
    fun dominantColoursOfNavyAndWhiteGarment() {
        val result = ColorTools.dominantColors(navyWhiteImage())
        assertEquals(listOf(FashionColor.NAVY, FashionColor.WHITE), result.map { it.first })
        assertEquals(0.7, result[0].second, 0.02)
        assertEquals(0.3, result[1].second, 0.02)
    }

    @Test
    fun dominantColoursAreRobustToSensorNoise() {
        val result = ColorTools.dominantColors(navyWhiteImage(noise = 8))
        assertEquals(listOf(FashionColor.NAVY, FashionColor.WHITE), result.map { it.first })
        assertEquals(1.0, result.sumOf { it.second }, 0.02)
    }

    @Test
    fun dominantColoursRespectMaxColours() {
        val result = ColorTools.dominantColors(navyWhiteImage(), maxColors = 1)
        assertEquals(listOf(FashionColor.NAVY), result.map { it.first })
        assertTrue(ColorTools.dominantColors(navyWhiteImage(), maxColors = 0).isEmpty())
    }

    @Test
    fun dropsSmallShares() {
        val pixels = IntArray(10_000) { i -> if (i < 9_500) 0xFF1C2541.toInt() else 0xFFC62828.toInt() }
        val result = ColorTools.dominantColors(pixels)
        assertEquals(listOf(FashionColor.NAVY), result.map { it.first })
    }

    @Test
    fun mergesClustersThatNameTheSameColour() {
        // Two slightly different blacks plus a red: the blacks must merge into one entry.
        val pixels = IntArray(9_000) { i ->
            when {
                i < 3_000 -> 0xFF0A0A0A.toInt()
                i < 6_000 -> 0xFF1E1E1E.toInt()
                else -> 0xFFC62828.toInt()
            }
        }
        val result = ColorTools.dominantColors(pixels)
        assertEquals(listOf(FashionColor.BLACK, FashionColor.RED), result.map { it.first })
        assertEquals(0.667, result[0].second, 0.02)
    }

    @Test
    fun handlesEmptyAndTransparentInput() {
        assertTrue(ColorTools.dominantColors(IntArray(0)).isEmpty())
        assertTrue(ColorTools.dominantColors(IntArray(5_000) { 0x7FFFFFFF }).isEmpty())
        val single = ColorTools.dominantColors(intArrayOf(0xFFC62828.toInt()))
        assertEquals(listOf(FashionColor.RED to 1.0), single)
    }

    @Test
    fun dominantColoursIsDeterministic() {
        val rnd = Random(7)
        val pixels = IntArray(30_000) { (0xFF shl 24) or rnd.nextInt(0x1000000) }
        assertEquals(ColorTools.dominantColors(pixels), ColorTools.dominantColors(pixels))
    }

    @Test
    fun dominantColoursIsFastOn65kPixels() {
        val rnd = Random(3)
        val pixels = IntArray(256 * 256) { (0xFF shl 24) or rnd.nextInt(0x1000000) }
        repeat(3) { ColorTools.dominantColors(pixels) } // JIT warm-up
        val times = (1..5).map {
            val start = System.nanoTime()
            ColorTools.dominantColors(pixels)
            (System.nanoTime() - start) / 1_000_000.0
        }.sorted()
        val median = times[times.size / 2]
        assertTrue("dominantColors took ${median}ms", median < 150.0)
    }

    @Test
    fun harmonyOrdering() {
        val allNeutral = ColorTools.harmony(listOf(FashionColor.BLACK, FashionColor.WHITE, FashionColor.GREY))
        val complementary = ColorTools.harmony(listOf(FashionColor.BLUE, FashionColor.ORANGE))
        val fourClashing = ColorTools.harmony(listOf(FashionColor.RED, FashionColor.GREEN, FashionColor.PURPLE, FashionColor.YELLOW))
        assertTrue("$allNeutral > $complementary", allNeutral > complementary)
        assertTrue("$complementary > $fourClashing", complementary > fourClashing)
        assertTrue(allNeutral >= 0.9)
        assertTrue(fourClashing <= 0.4)
    }

    @Test
    fun harmonyRewardsNeutralAnchorsAndTonalOutfits() {
        val withAnchor = ColorTools.harmony(listOf(FashionColor.BLUE, FashionColor.ORANGE, FashionColor.WHITE))
        val withoutAnchor = ColorTools.harmony(listOf(FashionColor.BLUE, FashionColor.ORANGE))
        assertTrue(withAnchor > withoutAnchor)

        val tonal = ColorTools.harmony(listOf(FashionColor.BLUE, FashionColor.LIGHT_BLUE, FashionColor.WHITE))
        assertTrue("tonal $tonal", tonal >= 0.9)

        val analogous = ColorTools.harmony(listOf(FashionColor.ORANGE, FashionColor.YELLOW))
        val awkward = ColorTools.harmony(listOf(FashionColor.GREEN, FashionColor.ORANGE)) // 130° apart
        assertTrue(analogous > awkward)

        val threeFamilies = ColorTools.harmony(listOf(FashionColor.RED, FashionColor.BLUE, FashionColor.YELLOW, FashionColor.WHITE))
        assertTrue(threeFamilies < withAnchor)
        assertTrue(threeFamilies > ColorTools.harmony(listOf(FashionColor.RED, FashionColor.BLUE, FashionColor.YELLOW, FashionColor.GREEN)))
    }

    @Test
    fun harmonyPenalisesDebatedClashesSlightly() = TestGuide.use {
        val navyWhite = ColorTools.harmony(listOf(FashionColor.NAVY, FashionColor.WHITE))
        val navyBlack = ColorTools.harmony(listOf(FashionColor.NAVY, FashionColor.BLACK))
        val brownBlack = ColorTools.harmony(listOf(FashionColor.BROWN, FashionColor.BLACK))
        assertTrue(navyBlack < navyWhite)
        assertTrue(brownBlack > navyBlack)
        assertTrue("still a fine outfit", navyBlack > 0.85)
    }

    @Test
    fun harmonyStaysInRange() {
        val rnd = Random(11)
        repeat(500) {
            val colors = List(rnd.nextInt(0, 8)) { FashionColor.entries.random(rnd) }
            val h = ColorTools.harmony(colors)
            assertTrue(h in 0.0..1.0)
        }
        assertEquals(1.0, ColorTools.harmony(emptyList()), 0.0)
        assertEquals(ColorTools.harmony(listOf(FashionColor.NAVY)), ColorTools.harmony(listOf(FashionColor.NAVY, FashionColor.NAVY)), 0.0)
    }
}
