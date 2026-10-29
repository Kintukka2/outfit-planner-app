package com.kintukka.hanger.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/** The researched guide that ships in the app: loads, is complete, and only uses what the engine understands. */
class BundledGuideTest {
    private val k = StyleKnowledge.bundled
    private val types = Subcategories.all().map { it.name.lowercase() }.toSet()

    @Test
    fun loadsWithEverySection() {
        assertTrue(k.sources.size >= 150)
        assertTrue(k.colors.pairs.size >= 150)
        assertEquals(FashionColor.entries.toSet(), k.colors.profiles.keys)
        assertTrue(k.colors.palettes.size >= 30)
        assertTrue(k.formulas.size >= 100)
        assertTrue(k.pairings.size >= 150)
        assertEquals("every pattern pair is rated", 45, k.patterns.size)
        assertEquals(Occasion.entries.toSet(), k.occasions.keys)
        assertTrue(k.dressCodes.size >= 14)
        assertTrue(k.weather.size >= 16)
        assertTrue(k.fabrics.size >= 28)
        assertTrue(k.trends.trends.size >= 30)
        assertTrue(k.wardrobe.tips.size >= 60)
        assertTrue(k.wardrobe.checks.size >= 28)
        assertTrue(k.wardrobe.essentials.size >= 15)
    }

    @Test
    fun everyReferenceResolves() {
        val cited = k.colors.pairs.flatMap { it.src } + k.colors.palettes.flatMap { it.src } + k.formulas.flatMap { it.src } +
            k.pairings.flatMap { it.src } + k.patterns.flatMap { it.src } + k.styleRules.flatMap { it.src } +
            k.trends.trends.flatMap { it.src } + k.wardrobe.tips.flatMap { it.src } + k.wardrobe.checks.flatMap { it.src }
        val missing = cited.filter { k.source(it) == null }.distinct()
        assertTrue("unknown sources $missing", missing.isEmpty())
    }

    @Test
    fun formulasAreWearable() {
        for (f in k.formulas) {
            val req = f.required.map { it.category }
            assertTrue(f.id, (Category.TOP in req && Category.BOTTOM in req) || Category.ONE_PIECE in req)
            assertTrue(f.id, Category.SHOES in req)
            for (s in f.slots) for (t in s.types) assertTrue("${f.id}: $t", t.lowercase() in types)
            assertTrue(f.id, f.name.isNotBlank() && f.why.isNotBlank())
        }
        assertEquals("formula ids are unique", k.formulas.size, k.formulas.map { it.id }.toSet().size)
    }

    @Test
    fun pairingsUseKnownTypesAndScores() {
        for (p in k.pairings) {
            assertTrue(p.a, p.a.lowercase() in types)
            assertTrue(p.b, p.b.lowercase() in types)
            assertTrue(p.score in setOf(-2, -1, 1, 2))
            assertTrue(k.typeIndex(p.a) >= 0)
            assertEquals(p.score, k.typeScore(p.b, p.a))
        }
        for (p in k.colors.pairs) assertEquals(p.score, k.colorScore(p.b, p.a))
        for (a in Pattern.entries) for (b in Pattern.entries) assertNotNull("$a/$b", k.patternScore(a, b))
    }

    @Test
    fun trendsHaveRealWindowsAndSomeAreLiveNow() {
        for (t in k.trends.trends) {
            val start = t.startDate
            val end = t.endDate
            assertTrue(t.id, start != null && end != null && !end.isBefore(start))
            assertTrue(t.id, t.match == "all" || t.match == "any")
        }
        assertTrue(k.liveTrends(LocalDate.of(2026, 10, 29)).size >= 15)
    }

    @Test
    fun checksOnlyUseMetricsTheEngineComputes() {
        val today = LocalDate.of(2026, 10, 29)
        val closet = TestCloset.synthetic(60).mapIndexed { i, g -> g.copy(added = today.minusDays(100L + i), wearCount = i % 4, price = 20.0 + i) }
        val log = listOf(WearRecord(today.minusDays(3), closet.take(10).map { it.id }))
        val metrics = StyleGuide.closetMetrics(closet, log, today, wishlist = 3)
        val unknown = k.wardrobe.checks.map { it.metric }.filter { it !in metrics }.distinct()
        assertTrue("metrics the engine never computes: $unknown", unknown.isEmpty())
        for (c in k.wardrobe.checks) assertTrue(c.id, c.op in setOf("<", "<=", ">", ">=", "=="))
        // Nothing nags a brand-new closet about wear it hasn't had a chance to log.
        val fresh = closet.map { it.copy(added = today, wearCount = 0) }
        val freshMetrics = StyleGuide.closetMetrics(fresh, emptyList(), today)
        assertTrue(freshMetrics.keys.none { it.startsWith("wornShare") || it == "neverWornShare" })
    }

    @Test
    fun weatherBandsCoverEveryTemperature() {
        for (t in -15..40) {
            val w = WeatherContext(t - 1.0, t + 1.0, feelsLikeC = t.toDouble())
            assertTrue("no rule for $t C", k.weatherRulesFor(w).isNotEmpty())
        }
    }

    @Test
    fun fabricsMatchCommonLabels() {
        assertTrue(k.fabricFor("Merino wool")?.material.orEmpty().contains("Merino", ignoreCase = true))
        assertNotNull(k.fabricFor("100% linen"))
        assertNotNull(k.fabricFor("Organic cotton"))
        assertNotNull(k.fabricFor("Cashmere"))
    }
}
