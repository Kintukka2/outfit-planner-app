package com.kintukka.hanger.ui.guide

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.data.repo.WardrobeRepository
import com.kintukka.hanger.engine.ClosetInsight
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.FormulaGap
import com.kintukka.hanger.engine.FormulaMatch
import com.kintukka.hanger.engine.StyleGuide
import com.kintukka.hanger.engine.Tip
import com.kintukka.hanger.engine.WearRecord
import com.kintukka.hanger.engine.headline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

@Immutable
data class GuideState(
    val loading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val items: Map<Long, Item> = emptyMap(),
    /** Closet pieces per main colour. */
    val colorCounts: Map<FashionColor, Int> = emptyMap(),
    val ready: List<FormulaMatch> = emptyList(),
    val oneAway: List<FormulaGap> = emptyList(),
    val insights: List<ClosetInsight> = emptyList(),
    /** Live trend id -> closet pieces that already speak to it. */
    val trendPieces: Map<String, List<Long>> = emptyMap(),
    /** Names of the guide's essentials the closet already covers. */
    val essentialsOwned: Set<String> = emptySet(),
    val tip: Tip? = null,
) {
    /** The colour to open the colour explorer on: the closet's most common accent, else its most common colour. */
    val startColor: FashionColor
        get() = colorCounts.entries.filter { !it.key.neutral }.maxByOrNull { it.value }?.key
            ?: colorCounts.maxByOrNull { it.value }?.key ?: FashionColor.NAVY
}

@OptIn(ExperimentalCoroutinesApi::class)
class StyleGuideViewModel(repo: WardrobeRepository, settings: SettingsRepository) : ViewModel() {

    val state: StateFlow<GuideState> = combine(
        repo.closet,
        repo.allItems,
        repo.dayEntries,
        settings.settings.map { it.currency }.distinctUntilChanged(),
    ) { closet, all, days, currency -> Inputs(closet, all.count { it.entity.wishlist && !it.entity.archived }, days.filter { it.worn && it.itemIds.isNotEmpty() }.map { WearRecord(it.date, it.itemIds) }, currency) }
        .conflate()
        .mapLatest(::compute)
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GuideState())

    private data class Inputs(val closet: List<Item>, val wishlist: Int, val wearLog: List<WearRecord>, val currency: String)

    private fun compute(input: Inputs): GuideState {
        val today = LocalDate.now()
        val garments = input.closet.map { it.toGarment() }
        val coverage = runCatching { StyleGuide.formulaCoverage(garments) }.getOrNull()
        val knowledge = StyleGuide.knowledge
        return GuideState(
            loading = false,
            today = today,
            items = input.closet.associateBy { it.id },
            colorCounts = input.closet.mapNotNull { it.entity.colors.firstOrNull() }.groupingBy { it }.eachCount(),
            ready = coverage?.ready.orEmpty(),
            oneAway = coverage?.oneAway.orEmpty(),
            insights = runCatching { StyleGuide.closetInsights(garments, input.wearLog, today, input.wishlist, input.currency).headline(5) }.getOrDefault(emptyList()),
            trendPieces = runCatching { StyleGuide.trendPieces(garments, today) }.getOrDefault(emptyList())
                .associate { (t, pieces) -> t.id to pieces.map { it.id } },
            essentialsOwned = knowledge.wardrobe.essentials.filter { it.ownedBy(garments) }.map { it.name }.toSet(),
            tip = knowledge.tipOfTheDay(today),
        )
    }
}
