package com.kintukka.hanger.ui.insights

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kintukka.hanger.data.DayEntry
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.repo.SettingsRepository
import com.kintukka.hanger.data.repo.WardrobeRepository
import com.kintukka.hanger.engine.CapsuleAnalyzer
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.CountedGarment
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.StatsCalculator
import com.kintukka.hanger.engine.StatsReport
import com.kintukka.hanger.engine.WearRecord
import com.kintukka.hanger.ui.formatMoney
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
import java.time.YearMonth
import java.time.temporal.ChronoUnit

/** One tile in a horizontal row of items, with its badge text already formatted. */
@Immutable
data class RankedItem(val item: Item, val badge: String?)

@Immutable
data class MonthBar(val month: YearMonth, val count: Int, val current: Boolean)

@Immutable
data class WishlistRow(val item: Item, val impact: Int?)

@Immutable
data class Streaks(val current: Int, val longest: Int, val daysLogged: Int)

@Immutable
data class InsightsUiState(
    val loading: Boolean = true,
    val currency: String = "£",
    val itemCount: Int = 0,
    /** Null when no item has a price. */
    val closetValue: Double? = null,
    val itemsWithPrice: Int = 0,
    val averageCostPerWear: Double? = null,
    /** 0..1, null when the stats engine is unavailable. */
    val utilisation90: Double? = null,
    val streaks: Streaks? = null,
    val months: List<MonthBar> = emptyList(),
    val categories: List<Pair<Category, Int>> = emptyList(),
    val colors: List<Pair<FashionColor, Int>> = emptyList(),
    val mostWorn: List<RankedItem> = emptyList(),
    val hiddenGems: List<RankedItem> = emptyList(),
    val neverWorn: List<RankedItem> = emptyList(),
    val bestCostPerWear: List<RankedItem> = emptyList(),
    val worstCostPerWear: List<RankedItem> = emptyList(),
    val forgotten: List<RankedItem> = emptyList(),
    /** Null when the capsule engine is unavailable. */
    val outfitCount: Int? = null,
    val wishlist: List<WishlistRow> = emptyList(),
) {
    val isEmpty: Boolean get() = !loading && itemCount == 0
}

@OptIn(ExperimentalCoroutinesApi::class)
class InsightsViewModel(
    repo: WardrobeRepository,
    settings: SettingsRepository,
) : ViewModel() {

    val state: StateFlow<InsightsUiState> = combine(
        repo.closet,
        repo.allItems,
        repo.dayEntries,
        settings.settings.map { it.currency }.distinctUntilChanged(),
    ) { closet, all, days, currency -> Inputs(closet, all, days, currency) }
        // closet is derived from allItems, so one write can emit twice in a row; only the latest matters.
        .conflate()
        .mapLatest { compute(it) }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InsightsUiState())

    private data class Inputs(val closet: List<Item>, val all: List<Item>, val days: List<DayEntry>, val currency: String)

    private fun compute(input: Inputs): InsightsUiState {
        val (closet, all, days, currency) = input
        val today = LocalDate.now()
        val byId = all.associateBy { it.id }
        val garments = closet.map { it.toGarment() }
        val wearLog = days.filter { it.worn && it.itemIds.isNotEmpty() }.map { WearRecord(it.date, it.itemIds) }

        // The engine may be unavailable (or throw on odd data); every engine call degrades to "section hidden".
        val stats: StatsReport? = runCatching { StatsCalculator.compute(garments, wearLog, today) }.getOrNull()
        val outfitCount: Int? = runCatching { CapsuleAnalyzer.outfitCount(garments) }.getOrNull()
        val wishlist = all.asSequence()
            .filter { it.entity.wishlist && !it.entity.archived }
            .map { candidate ->
                WishlistRow(candidate, runCatching { CapsuleAnalyzer.wishlistImpact(garments, candidate.toGarment()) }.getOrNull())
            }
            .sortedWith(compareByDescending<WishlistRow> { it.impact ?: -1 }.thenBy { it.item.name.lowercase() })
            .toList()

        // Basics that need no engine, so the top of the page still works if it isn't ready.
        val prices = closet.mapNotNull { it.entity.price?.takeIf { p -> p > 0 } }
        val categories = (stats?.byCategory ?: closet.groupingBy { it.category }.eachCount())
            .filterValues { it > 0 }.toList()
            .sortedWith(compareByDescending<Pair<Category, Int>> { it.second }.thenBy { it.first.order })
        val colors = (stats?.byColor ?: closet.mapNotNull { it.entity.colors.firstOrNull() }.groupingBy { it }.eachCount())
            .filterValues { it > 0 }.toList()
            .sortedWith(compareByDescending<Pair<FashionColor, Int>> { it.second }.thenBy { it.first.ordinal })

        fun List<CountedGarment>.ranked(badge: (CountedGarment, Item) -> String?): List<RankedItem> =
            mapNotNull { c -> byId[c.garmentId]?.let { RankedItem(it, badge(c, it)) } }.take(ROW_LIMIT)

        val wearBadge = { c: CountedGarment, _: Item -> "${c.value.toInt()}×" }
        val cpwBadge = { c: CountedGarment, _: Item -> "${formatMoney(c.value, currency)}/wear" }

        return InsightsUiState(
            loading = false,
            currency = currency,
            itemCount = stats?.totalItems ?: closet.size,
            closetValue = when {
                stats != null && stats.itemsWithPrice > 0 -> stats.closetValue
                stats == null && prices.isNotEmpty() -> prices.sum()
                else -> null
            },
            itemsWithPrice = stats?.itemsWithPrice ?: prices.size,
            averageCostPerWear = stats?.averageCostPerWear,
            utilisation90 = stats?.utilisation90,
            streaks = stats?.let { Streaks(it.currentStreak, it.longestStreak, it.daysLogged) },
            months = stats?.wearsPerMonth.orEmpty().mapNotNull { (key, count) ->
                runCatching { YearMonth.parse(key) }.getOrNull()?.let { MonthBar(it, count, it == YearMonth.from(today)) }
            },
            categories = categories,
            colors = colors,
            mostWorn = stats?.mostWorn.orEmpty().filter { it.value > 0 }.ranked(wearBadge),
            hiddenGems = stats?.leastWorn.orEmpty().ranked(wearBadge),
            neverWorn = stats?.neverWorn.orEmpty().mapNotNull(byId::get)
                .sortedByDescending { it.entity.price ?: 0.0 }
                .map { RankedItem(it, it.entity.price?.takeIf { p -> p > 0 }?.let { p -> formatMoney(p, currency) }) }
                .take(ROW_LIMIT),
            bestCostPerWear = stats?.bestCostPerWear.orEmpty().ranked(cpwBadge),
            worstCostPerWear = stats?.worstCostPerWear.orEmpty().ranked(cpwBadge),
            forgotten = stats?.forgotten.orEmpty().mapNotNull(byId::get)
                .sortedBy { it.lastWorn ?: LocalDate.MIN }
                .map { RankedItem(it, it.lastWorn?.let { d -> sinceLabel(d, today) }) }
                .take(ROW_LIMIT),
            outfitCount = outfitCount,
            wishlist = wishlist,
        )
    }

    private companion object {
        const val ROW_LIMIT = 12

        /** Compact "time since" for a small tile badge, e.g. "9 wk", "4 mo", "1 yr". */
        fun sinceLabel(date: LocalDate, today: LocalDate): String {
            val days = ChronoUnit.DAYS.between(date, today).coerceAtLeast(0)
            return when {
                days < 60 -> "${days / 7} wk"
                days < 365 -> "${days / 30} mo"
                else -> "${days / 365} yr"
            }
        }
    }
}
