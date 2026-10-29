package com.kintukka.hanger.engine

import com.kintukka.hanger.engine.TestCloset.item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OutfitEvaluatorTest {
    private val navyTee = item(1, "T-shirt", FashionColor.NAVY)
    private val whiteShirt = item(2, "Shirt", FashionColor.WHITE)
    private val jeans = item(3, "Jeans", FashionColor.DENIM)
    private val camelChinos = item(4, "Chinos", FashionColor.CAMEL)
    private val sneakers = item(5, "Sneakers", FashionColor.WHITE)
    private val sandals = item(6, "Sandals", FashionColor.BEIGE)
    private val camelCoat = item(7, "Coat", FashionColor.CAMEL)
    private val rustKnit = item(8, "Knit sweater", FashionColor.RUST)
    private val greyTrousers = item(9, "Trousers", FashionColor.GREY)
    private val loafers = item(10, "Loafers", FashionColor.BROWN)
    private val hoodie = item(11, "Hoodie", FashionColor.GREY)
    private val dressShoes = item(12, "Dress shoes", FashionColor.BLACK)
    private val runningShoes = item(13, "Running shoes", FashionColor.GREY)
    private val leggings = item(14, "Leggings", FashionColor.BLACK)
    private val sportsTop = item(15, "Sports top", FashionColor.BLACK)
    private val raincoat = item(16, "Raincoat", FashionColor.OLIVE)
    private val shorts = item(17, "Shorts", FashionColor.KHAKI)

    private fun weather(feels: Double, precip: Int = 0) = WeatherContext(feels - 2, feels + 2, feelsLikeC = feels, precipitationProbability = precip)

    @Test
    fun cohesiveNeutralOutfitScoresHigh() = TestGuide.use {
        val e = OutfitEvaluator.evaluate(listOf(navyTee, camelChinos, sneakers))
        assertTrue("score ${e.score}", e.score >= 0.85)
        assertTrue(e.warnings.toString(), e.warnings.isEmpty())
        assertTrue(e.notes.toString(), "Navy and camel are a classic pairing" in e.notes)
    }

    @Test
    fun neutralBaseLetsTheAccentShine() {
        val e = OutfitEvaluator.evaluate(listOf(whiteShirt, rustKnit, jeans, sneakers))
        assertTrue(e.notes.toString(), "Neutral base lets the rust knit sweater shine" in e.notes)
    }

    @Test
    fun twoBoldPatternsAreFlagged() {
        val floralSkirt = item(20, "Skirt", FashionColor.PINK, pattern = Pattern.FLORAL)
        val checkShirt = item(21, "Shirt", FashionColor.RED, FashionColor.BLACK, pattern = Pattern.CHECK)
        val clash = OutfitEvaluator.evaluate(listOf(checkShirt, floralSkirt, sneakers))
        assertTrue(clash.warnings.toString(), "Two bold patterns are competing — let one print lead" in clash.warnings)

        val calm = OutfitEvaluator.evaluate(listOf(checkShirt, item(22, "Skirt", FashionColor.BLACK), sneakers))
        assertTrue(calm.score > clash.score)
        assertTrue(calm.warnings.none { it.contains("pattern") })
    }

    @Test
    fun stripesWithAnotherPrintIsOnlyAMildWarning() {
        val breton = item(20, "Long-sleeve top", FashionColor.NAVY, FashionColor.WHITE, pattern = Pattern.STRIPE)
        val floralSkirt = item(21, "Skirt", FashionColor.PINK, pattern = Pattern.FLORAL)
        val checkSkirt = item(22, "Skirt", FashionColor.RED, pattern = Pattern.CHECK)
        val floralShirt = item(23, "Shirt", FashionColor.PINK, pattern = Pattern.FLORAL)
        val mild = OutfitEvaluator.evaluate(listOf(breton, floralSkirt, sneakers))
        assertTrue(mild.warnings.toString(), mild.warnings.any { it.startsWith("Stripes with floral can work") })
        val strong = OutfitEvaluator.evaluate(listOf(floralShirt, checkSkirt, sneakers))
        assertTrue(mild.score > strong.score)

        val three = OutfitEvaluator.evaluate(listOf(breton, floralSkirt, item(24, "Sneakers", FashionColor.WHITE, pattern = Pattern.ANIMAL)))
        assertTrue(three.warnings.toString(), three.warnings.any { it.startsWith("Three bold patterns") })
        assertTrue(three.score < mild.score)
    }

    @Test
    fun sandalsInTheColdAreFlagged() {
        val e = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sandals, camelCoat), weather = weather(9.0))
        assertTrue(e.warnings.toString(), "Sandals look chilly for 9°C" in e.warnings)
        val withSneakers = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers, camelCoat), weather = weather(9.0))
        assertTrue(withSneakers.score > e.score)
        val shortsCold = OutfitEvaluator.evaluate(listOf(navyTee, shorts, sneakers, camelCoat), weather = weather(8.0))
        assertTrue(shortsCold.warnings.toString(), "Shorts look chilly for 8°C" in shortsCold.warnings)
    }

    @Test
    fun missingCoatInTheColdIsFlaggedAndCoatIsPraised() {
        val bare = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers), weather = weather(4.0))
        assertTrue(bare.warnings.toString(), "Light for 4°C — add a coat or jacket" in bare.warnings)

        val coated = OutfitEvaluator.evaluate(listOf(whiteShirt, rustKnit, jeans, sneakers, camelCoat), weather = weather(7.0))
        assertTrue(coated.notes.toString(), "Warm enough for 7°C with the camel coat" in coated.notes)
        assertTrue(coated.warnings.none { it.contains("coat or jacket") })
        assertTrue(coated.score > OutfitEvaluator.evaluate(listOf(whiteShirt, rustKnit, jeans, sneakers), weather = weather(7.0)).score)
    }

    @Test
    fun heatAndRainNotes() {
        val hot = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers, camelCoat), weather = weather(30.0))
        assertTrue(hot.warnings.toString(), hot.warnings.any { it.startsWith("Could feel warm for 30°C") })

        val wet = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers), weather = weather(18.0, precip = 80))
        assertTrue(wet.warnings.toString(), wet.warnings.any { it.startsWith("Rain is likely") })
        val dry = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers, raincoat), weather = weather(14.0, precip = 80))
        assertTrue(dry.notes.toString(), "The olive raincoat keeps the rain off" in dry.notes)
    }

    @Test
    fun missingShoesAndCoreAreFlagged() {
        val noShoes = OutfitEvaluator.evaluate(listOf(navyTee, jeans))
        assertTrue(noShoes.warnings.toString(), "No shoes yet — add a pair to finish the look" in noShoes.warnings)
        val complete = OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers))
        assertTrue(complete.score > noShoes.score)

        val noBottoms = OutfitEvaluator.evaluate(listOf(navyTee, sneakers))
        assertTrue(noBottoms.warnings.toString(), "Add bottoms to complete the look" in noBottoms.warnings)
        assertTrue(noBottoms.score < complete.score)

        val dress = item(30, "Dress", FashionColor.NAVY)
        val dressOutfit = OutfitEvaluator.evaluate(listOf(dress, sneakers))
        assertTrue(dressOutfit.warnings.toString(), dressOutfit.warnings.isEmpty())
    }

    @Test
    fun duplicateCoreCategoriesAreFlagged() {
        val e = OutfitEvaluator.evaluate(listOf(navyTee, jeans, camelChinos, sneakers))
        assertTrue(e.warnings.toString(), "Two picks from Bottoms — choose one" in e.warnings)
        assertTrue(e.score < OutfitEvaluator.evaluate(listOf(navyTee, jeans, sneakers)).score)
    }

    @Test
    fun formalityClashesAreFlagged() {
        val e = OutfitEvaluator.evaluate(listOf(hoodie, greyTrousers, dressShoes))
        assertTrue(e.warnings.toString(), e.warnings.any { it.contains("worlds apart in formality") })
        val coherent = OutfitEvaluator.evaluate(listOf(whiteShirt, greyTrousers, loafers))
        assertTrue(coherent.score > e.score)
        assertTrue(coherent.notes.any { it.startsWith("Consistent") })
    }

    @Test
    fun smartOuterwearMaySitOneLevelAbove() {
        // Coat (4) over a casual outfit (2) is +2 → flagged mildly; a jacket at +1 is free.
        val casual = listOf(navyTee, jeans, sneakers)
        val withJacket = OutfitEvaluator.evaluate(casual + item(40, "Leather jacket", FashionColor.BLACK))
        assertTrue(withJacket.warnings.toString(), withJacket.warnings.none { it.contains("casual") })
    }

    @Test
    fun occasionFit() {
        val work = OutfitEvaluator.evaluate(listOf(hoodie, jeans, sneakers), occasion = Occasion.WORK)
        assertTrue(work.warnings.toString(), "The grey hoodie is too casual for work" in work.warnings)
        val smartWork = OutfitEvaluator.evaluate(listOf(whiteShirt, greyTrousers, loafers), occasion = Occasion.WORK)
        assertTrue(smartWork.notes.toString(), "Just right for work" in smartWork.notes)
        assertTrue(smartWork.score > work.score)

        val sportyAtWork = OutfitEvaluator.evaluate(listOf(whiteShirt, greyTrousers, runningShoes), occasion = Occasion.WORK)
        assertTrue(sportyAtWork.warnings.toString(), "The grey running shoes read too sporty for work" in sportyAtWork.warnings)

        val gym = OutfitEvaluator.evaluate(listOf(sportsTop, leggings, runningShoes), occasion = Occasion.ACTIVE)
        val gymInLoafers = OutfitEvaluator.evaluate(listOf(sportsTop, leggings, loafers), occasion = Occasion.ACTIVE)
        assertTrue(gym.score > gymInLoafers.score)
        assertTrue(gym.notes.toString(), gym.notes.any { it.startsWith("Sporty pieces throughout") })
    }

    @Test
    fun affinityNudgesScores() {
        val items = listOf(navyTee, jeans, sneakers)
        var liked = AffinityModel()
        repeat(4) { liked = FeedbackLearner.apply(liked, FeedbackEvent(FeedbackType.WORN, items.map { it.id })) }
        var disliked = AffinityModel()
        repeat(4) { disliked = FeedbackLearner.apply(disliked, FeedbackEvent(FeedbackType.REJECTED, items.map { it.id })) }
        val neutral = OutfitEvaluator.evaluate(items).score
        val up = OutfitEvaluator.evaluate(items, affinity = liked)
        val down = OutfitEvaluator.evaluate(items, affinity = disliked)
        assertTrue(up.score > neutral)
        assertTrue(down.score < neutral)
        assertTrue(up.notes.toString(), "You've enjoyed these pieces together before" in up.notes)
    }

    @Test
    fun clashingColoursScoreBelowHarmoniousOnes() {
        val red = item(50, "T-shirt", FashionColor.RED)
        val green = item(51, "Skirt", FashionColor.GREEN)
        val purple = item(52, "Sneakers", FashionColor.PURPLE)
        val yellow = item(53, "Crossbody", FashionColor.YELLOW)
        val clash = OutfitEvaluator.evaluate(listOf(red, green, purple, yellow))
        assertTrue(clash.warnings.toString(), clash.warnings.any { it.startsWith("Four colours are fighting") })
        assertTrue(clash.score < OutfitEvaluator.evaluate(listOf(navyTee, camelChinos, sneakers)).score - 0.2)
    }

    @Test
    fun emptyOutfit() {
        val e = OutfitEvaluator.evaluate(emptyList())
        assertEquals(0.0, e.score, 0.0)
        assertFalse(e.warnings.isEmpty())
    }

    @Test
    fun scoresStayInRange() {
        val closet = TestCloset.synthetic(120, seed = 9)
        val rnd = kotlin.random.Random(5)
        repeat(300) {
            val items = List(rnd.nextInt(1, 7)) { closet.random(rnd) }
            val w = WeatherContext(rnd.nextDouble(-10.0, 25.0), rnd.nextDouble(0.0, 35.0), precipitationProbability = rnd.nextInt(100))
            val e = OutfitEvaluator.evaluate(items, w, Occasion.entries.random(rnd))
            assertTrue(e.score in 0.0..1.0)
        }
    }
}
