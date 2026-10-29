package com.kintukka.hanger.engine

import com.kintukka.hanger.engine.TestCloset.item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

/** How the built-in style guide drives scoring, notes, formulas, trends and closet checks (on a fixed test guide). */
class StyleGuideTest {
    private val today = LocalDate.of(2026, 10, 29)

    private val whiteTee = item(1, "T-shirt", FashionColor.WHITE)
    private val navyTee = item(2, "T-shirt", FashionColor.NAVY)
    private val jeans = item(3, "Jeans", FashionColor.DENIM)
    private val whiteSneakers = item(4, "Sneakers", FashionColor.WHITE)
    private val blazer = item(5, "Blazer", FashionColor.NAVY)
    private val shirt = item(6, "Shirt", FashionColor.WHITE)
    private val trousers = item(7, "Trousers", FashionColor.GREY)
    private val loafers = item(8, "Loafers", FashionColor.BROWN)
    private val chinos = item(9, "Chinos", FashionColor.CAMEL)
    private val runningShoes = item(10, "Running shoes", FashionColor.GREY)
    private val flipFlops = item(11, "Flip-flops", FashionColor.BLACK)

    @Test
    fun recognisesProvenFormulasAndExplainsThem() = TestGuide.use {
        assertEquals("f1", StyleGuide.formulaFor(listOf(whiteTee, jeans, whiteSneakers))?.id)
        // Colour-constrained slot: a navy tee isn't the white-tee formula.
        assertNull(StyleGuide.formulaFor(listOf(navyTee, jeans, whiteSneakers)))
        // Optional slots (the belt) aren't required.
        assertEquals("f2", StyleGuide.formulaFor(listOf(blazer, shirt, trousers, loafers))?.id)
        val e = OutfitEvaluator.evaluate(listOf(whiteTee, jeans, whiteSneakers))
        assertTrue(e.notes.toString(), "Proven formula: White tee, jeans, white sneakers" in e.notes)
    }

    @Test
    fun coverageListsReadyFormulasAndTheMissingPiece() = TestGuide.use {
        val closet = listOf(whiteTee, jeans, whiteSneakers, blazer, shirt, trousers, chinos)
        val coverage = StyleGuide.formulaCoverage(closet)
        assertEquals(listOf("f1"), coverage.ready.map { it.formula.id })
        assertEquals(setOf(1L, 3L, 4L), coverage.ready[0].itemIds.toSet())
        val gap = coverage.oneAway.single()
        assertEquals("f2", gap.formula.id)
        assertEquals(listOf("Loafers"), gap.missing.types)
        assertEquals(setOf(5L, 6L, 7L), gap.itemIds.toSet())
    }

    @Test
    fun garmentPairingsLiftClassicsAndFlagClashes() {
        val good = listOf(shirt, chinos, loafers)
        val bad = listOf(shirt, trousers, runningShoes)
        val plainGood = OutfitEvaluator.evaluate(good).score
        val plainBad = OutfitEvaluator.evaluate(bad).score
        TestGuide.use {
            val g = OutfitEvaluator.evaluate(good)
            assertTrue(g.notes.toString(), "Loafers sharpen chinos" in g.notes)
            assertTrue(g.score >= plainGood || g.score == 1.0)
            val b = OutfitEvaluator.evaluate(bad)
            assertTrue(b.warnings.toString(), "Running shoes undercut tailored trousers" in b.warnings)
            assertTrue("${b.score} vs $plainBad", b.score < plainBad - 0.1)
        }
    }

    @Test
    fun patternPairsComeFromTheGuide() = TestGuide.use {
        val stripes = item(20, "Shirt", FashionColor.NAVY, FashionColor.WHITE, pattern = Pattern.STRIPE)
        val floral = item(21, "Skirt", FashionColor.PINK, pattern = Pattern.FLORAL)
        val leopard = item(22, "Shirt", FashionColor.CAMEL, pattern = Pattern.ANIMAL)
        val ok = OutfitEvaluator.evaluate(listOf(stripes, floral, whiteSneakers))
        assertTrue(ok.notes.toString(), "Stripes steady a floral" in ok.notes)
        val clash = OutfitEvaluator.evaluate(listOf(leopard, floral, whiteSneakers))
        assertTrue(clash.warnings.toString(), "Floral and animal print shout over each other" in clash.warnings)
        assertTrue(ok.score > clash.score)
    }

    @Test
    fun occasionAvoidListsCostAndExplain() = TestGuide.use {
        val e = OutfitEvaluator.evaluate(listOf(shirt, trousers, flipFlops), occasion = Occasion.WORK)
        assertTrue(e.warnings.toString(), e.warnings.any { it.contains("aren't right for work") })
        val fine = OutfitEvaluator.evaluate(listOf(shirt, trousers, loafers), occasion = Occasion.WORK)
        assertTrue(fine.score > e.score)
    }

    @Test
    fun weatherRulesFlagPiecesForTheDay() = TestGuide.use {
        val wet = WeatherContext(8.0, 12.0, feelsLikeC = 9.0, precipitationProbability = 80, condition = WeatherCondition.RAIN)
        assertEquals(setOf("Rain", "Cold"), StyleGuide.knowledge.weatherRulesFor(wet).map { it.title }.toSet())
        val e = OutfitEvaluator.evaluate(listOf(shirt, chinos, loafers), weather = wet)
        assertTrue(e.warnings.toString(), e.warnings.any { it.startsWith("Rain: swap the") && it.contains("loafers") })
        val freezing = WeatherContext(-6.0, 1.0, feelsLikeC = -4.0)
        assertEquals(listOf("Freezing"), StyleGuide.knowledge.weatherRulesFor(freezing).map { it.title })
    }

    @Test
    fun trendsRunOnTheirDatesAndNoteMatchingOutfits() = TestGuide.use {
        val k = StyleGuide.knowledge
        assertEquals(listOf("t1", "t2"), k.liveTrends(today).map { it.id })
        assertEquals(listOf("t3"), k.upcomingTrends(today).map { it.id })
        assertEquals(listOf("t3"), k.liveTrends(LocalDate.of(2027, 5, 1)).map { it.id })

        val burgundyKnit = item(30, "Knit sweater", FashionColor.BURGUNDY)
        val e = OutfitEvaluator.evaluate(listOf(burgundyKnit, jeans, whiteSneakers), date = today)
        assertTrue(e.notes.toString(), "On trend: burgundy everything" in e.notes)
        assertTrue(OutfitEvaluator.evaluate(listOf(burgundyKnit, jeans, whiteSneakers)).notes.none { it.startsWith("On trend") })

        // "all" trends need every colour.
        val brown = item(31, "Chinos", FashionColor.BROWN)
        val blue = item(32, "Shirt", FashionColor.LIGHT_BLUE)
        assertFalse(k.trends.trends.first { it.id == "t2" }.matchesOutfit(listOf(brown, whiteSneakers)))
        assertTrue(k.trends.trends.first { it.id == "t2" }.matchesOutfit(listOf(brown, blue, whiteSneakers)))

        val pieces = StyleGuide.trendPieces(listOf(burgundyKnit, brown, jeans), today).associate { (t, g) -> t.id to g.map { it.id } }
        assertEquals(listOf(30L), pieces["t1"])
        assertEquals(listOf(31L), pieces["t2"])
    }

    @Test
    fun closetChecksFireWithFormattedMessages() = TestGuide.use {
        val neutrals = listOf(whiteTee, jeans, whiteSneakers, shirt, trousers)
        val fired = StyleGuide.closetInsights(neutrals, emptyList(), today)
        assertEquals(listOf("k1", "k2"), fired.map { it.check.id })
        assertEquals("100% of your closet is neutral", fired[0].message)
        // Small closets stay quiet.
        assertTrue(StyleGuide.closetInsights(neutrals.take(3), emptyList(), today).isEmpty())
        val withRaincoat = neutrals + item(40, "Raincoat", FashionColor.OLIVE)
        assertEquals(listOf("k1"), StyleGuide.closetInsights(withRaincoat, emptyList(), today).map { it.check.id })
    }

    @Test
    fun closetMetricsCountWhatTheChecksNeed() = TestGuide.use {
        val closet = listOf(
            whiteTee, navyTee, jeans, whiteSneakers, loafers,
            item(41, "T-shirt", FashionColor.WHITE, wearCount = 3, price = 30.0),
            item(42, "Coat", FashionColor.CAMEL),
        )
        val wornYesterday = listOf(WearRecord(today.minusDays(1), listOf(1L, 3L)))
        val m = StyleGuide.closetMetrics(closet, wornYesterday, today, wishlist = 2)
        assertEquals(7.0, m["items"]!!, 0.0)
        assertEquals(2.0, m["wishlist"]!!, 0.0)
        assertEquals(3.0, m["tops"]!!, 0.0)
        assertEquals(3.0, m["topsPerBottom"]!!, 0.0)
        assertEquals(1.0, m["smartShoes"]!!, 0.0)
        assertEquals(2.0 / 7, m["wornShare30"]!!, 1e-9)
        assertEquals(10.0, m["avgCostPerWear"]!!, 1e-9)
        assertEquals(2.0, m["duplicates"]!!, 0.0) // two white T-shirts
    }

    @Test
    fun essentialsTipsAndFabrics() = TestGuide.use {
        val k = StyleGuide.knowledge
        val coat = k.wardrobe.essentials.single()
        assertFalse(coat.ownedBy(listOf(whiteTee)))
        assertTrue(coat.ownedBy(listOf(whiteTee, item(43, "Trench coat", FashionColor.BEIGE))))
        assertEquals("Tip A", k.tipOfTheDay(LocalDate.ofEpochDay(0))?.text)
        assertEquals("Tip B", k.tipOfTheDay(LocalDate.ofEpochDay(1))?.text)
        assertEquals("Merino", k.fabricFor("Merino wool blend")?.material)
        assertEquals("Wool", k.fabricFor("100% Wool")?.material)
        assertNull(k.fabricFor(""))
        assertNull(k.fabricFor("Polyester"))
        assertEquals("Cloud Dancer", k.trends.colourOfTheYear.single().name)
    }

    @Test
    fun colourGuideAdjustsHarmonyAndExplains() = TestGuide.use {
        val navyWhite = ColorTools.harmony(listOf(FashionColor.NAVY, FashionColor.WHITE))
        val greyWhite = ColorTools.harmony(listOf(FashionColor.GREY, FashionColor.WHITE))
        assertTrue(navyWhite > greyWhite)
        // The guide's blessing lifts a pairing the colour wheel alone would call a bold mix.
        val pinkRed = ColorTools.harmony(listOf(FashionColor.PINK, FashionColor.RED))
        val hotPinkRed = ColorTools.harmony(listOf(FashionColor.HOT_PINK, FashionColor.RED))
        assertTrue("$pinkRed vs $hotPinkRed", pinkRed > hotPinkRed + 0.1)
        val palette = OutfitEvaluator.evaluate(listOf(navyTee, item(50, "Shorts", FashionColor.WHITE), item(51, "Sneakers", FashionColor.DENIM)))
        assertTrue(palette.notes.toString(), palette.notes.any { it.startsWith("Coastal palette") })
    }

    @Test
    fun generatorReachesForFormulas() = TestGuide.use {
        val closet = listOf(
            whiteTee, navyTee, item(60, "T-shirt", FashionColor.OLIVE), item(61, "Sweatshirt", FashionColor.GREY),
            jeans, item(62, "Chinos", FashionColor.KHAKI), item(63, "Shorts", FashionColor.NAVY),
            whiteSneakers, item(64, "Boots", FashionColor.BROWN), loafers,
        )
        val ideas = OutfitGenerator.suggest(closet, StylingRequest(date = today, occasion = Occasion.CASUAL, count = 3))
        assertTrue(ideas.toString(), ideas.any { s -> s.itemIds.containsAll(listOf(1L, 3L, 4L)) })
        assertTrue(ideas.flatMap { it.reasons }.toString(), ideas.flatMap { it.reasons }.any { it.startsWith("Proven formula") })
    }

    @Test
    fun fastOutfitCountMatchesTheEvaluatorUnderAnyGuide() {
        // A dense random guide over every preset type and colour, so pairing lifts and costs both bite.
        val rnd = Random(7)
        val types = Subcategories.all().map { it.name }
        val pairings = ArrayList<TypePairing>()
        for (i in types.indices) for (j in i + 1 until types.size) {
            if (rnd.nextInt(4) == 0) pairings += TypePairing(types[i], types[j], listOf(-2, -1, 1, 2).random(rnd), "why")
        }
        val colorPairs = ArrayList<ColorPair>()
        val colors = FashionColor.entries
        for (i in colors.indices) for (j in i + 1 until colors.size) {
            if (rnd.nextInt(3) == 0) colorPairs += ColorPair(colors[i], colors[j], listOf(-2, -1, 1, 2).random(rnd), "why")
        }
        val guide = StyleKnowledge(version = 1, updated = "test", colors = ColorKnowledge(pairs = colorPairs), pairings = pairings)
        val previous = StyleKnowledge.override
        StyleKnowledge.override = guide
        try {
            val closet = TestCloset.synthetic(80, seed = 5)
            fun valid(items: List<Garment>): Boolean {
                val r = Evaluator.run(items, null, null, AffinityModel(), explain = false)
                return r.score >= 0.55 && r.formalitySpread <= 2
            }
            val by = closet.groupBy { it.category }
            var expected = 0
            for (t in by[Category.TOP].orEmpty()) for (b in by[Category.BOTTOM].orEmpty()) for (s in by[Category.SHOES].orEmpty()) {
                if (valid(listOf(t, b, s))) expected++
            }
            for (d in by[Category.ONE_PIECE].orEmpty()) for (s in by[Category.SHOES].orEmpty()) if (valid(listOf(d, s))) expected++
            assertTrue(expected > 0)
            assertEquals(expected, CapsuleAnalyzer.outfitCount(closet))
        } finally {
            StyleKnowledge.override = previous
        }
    }
}
