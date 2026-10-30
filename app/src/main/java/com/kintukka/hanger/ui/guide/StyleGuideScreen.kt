package com.kintukka.hanger.ui.guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.Formula
import com.kintukka.hanger.engine.FormulaSlot
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.Palette
import com.kintukka.hanger.engine.StyleGuide
import com.kintukka.hanger.engine.Trend
import com.kintukka.hanger.engine.TrendNote
import com.kintukka.hanger.ui.components.ColorDot
import com.kintukka.hanger.ui.components.ItemImage
import com.kintukka.hanger.ui.components.ItemsCollage
import com.kintukka.hanger.ui.components.SectionHeader
import com.kintukka.hanger.ui.hangerViewModel
import com.kintukka.hanger.ui.navigation.BuilderRoute
import com.kintukka.hanger.ui.navigation.ItemRoute
import com.kintukka.hanger.ui.plural
import com.kintukka.hanger.ui.theme.HangerTokens
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val Page = 20.dp
private val TABS = listOf("For you", "Colours", "Formulas", "Trends", "Guide")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleGuideScreen(navController: NavHostController) {
    val vm = hangerViewModel { c -> StyleGuideViewModel(c.repo, c.settings) }
    val state by vm.state.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableStateOf(0) }
    var occasion by rememberSaveable { mutableStateOf<Occasion?>(null) }
    var showAllFormulas by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Style guide") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back") }
                    },
                )
                PrimaryScrollableTabRow(selectedTabIndex = tab, edgePadding = 12.dp) {
                    TABS.forEachIndexed { i, label ->
                        Tab(selected = tab == i, onClick = { tab = i }, text = { Text(label) }, modifier = Modifier.testTag("guide-tab-$label"))
                    }
                }
            }
        },
    ) { padding ->
        if (state.loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }
        val open: (List<Long>) -> Unit = { ids -> navController.navigate(BuilderRoute(itemIds = ids.joinToString(","))) }
        val openItem: (Long) -> Unit = { id -> navController.navigate(ItemRoute(id)) }
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            when (tab) {
                0 -> forYou(state, open, openItem, onTab = { tab = it })
                1 -> colours(state)
                2 -> formulas(state, occasion, { occasion = it }, showAllFormulas, { showAllFormulas = !showAllFormulas }, open)
                3 -> trends(state, openItem)
                else -> guide(state)
            }
        }
    }
}

// --- For you ------------------------------------------------------------------------------------------------------

private fun LazyListScope.forYou(state: GuideState, open: (List<Long>) -> Unit, openItem: (Long) -> Unit, onTab: (Int) -> Unit) {
    state.tip?.let { tip ->
        item(key = "tip") {
            GuideCard(Modifier.padding(horizontal = Page)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "Tip of the day · ${tip.topic.replaceFirstChar(Char::uppercase)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                Text(tip.text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
    if (state.insights.isNotEmpty()) {
        item(key = "check-h") { SectionHeader("Closet check", Modifier.padding(horizontal = Page)) }
        items(state.insights, key = { "check-" + it.check.id }) { insight ->
            GuideCard(Modifier.padding(horizontal = Page)) {
                Text(insight.check.title, style = MaterialTheme.typography.titleMedium)
                Text(insight.message, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                if (insight.check.action.isNotBlank()) {
                    Text(
                        insight.check.action,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
    }
    item(key = "ready-h") {
        SectionHeader(
            "Ready to wear · ${state.ready.size}",
            Modifier.padding(horizontal = Page),
            action = "All formulas".takeIf { state.ready.isNotEmpty() || state.oneAway.isNotEmpty() },
            onAction = { onTab(2) },
        )
    }
    if (state.ready.isEmpty()) {
        item(key = "ready-empty") {
            Hint("Add a few tops, bottoms and shoes and proven outfit formulas from your own closet will show up here.")
        }
    }
    items(state.ready.take(3), key = { "ready-" + it.formula.id }) { m ->
        FormulaCard(m.formula, m.itemIds, state.items, Modifier.padding(horizontal = Page), action = "Try it" to { open(m.itemIds) })
    }
    val wearable = StyleGuide.knowledge.liveTrends(state.today).filter { (state.trendPieces[it.id]?.size ?: 0) > 0 }
    if (wearable.isNotEmpty()) {
        item(key = "trend-h") {
            SectionHeader("Trends you can wear now", Modifier.padding(horizontal = Page), action = "All trends", onAction = { onTab(3) })
        }
        items(wearable.take(3), key = { "trend-" + it.id }) { t ->
            TrendCard(t, state.trendPieces[t.id].orEmpty(), state.items, openItem, Modifier.padding(horizontal = Page))
        }
    }
    val essentials = StyleGuide.knowledge.wardrobe.essentials
    if (essentials.isNotEmpty()) {
        item(key = "ess") {
            GuideCard(Modifier.padding(horizontal = Page).clickable { onTab(4) }) {
                Text("Essentials", style = MaterialTheme.typography.titleMedium)
                Text(
                    "You have ${state.essentialsOwned.size} of ${essentials.size} wardrobe essentials.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp),
                )
                val missing = essentials.filter { it.name !in state.essentialsOwned }.take(4)
                if (missing.isNotEmpty()) {
                    Text(
                        "Worth adding: " + missing.joinToString { it.name.replaceFirstChar(Char::lowercase) },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

// --- Colours ------------------------------------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.colours(state: GuideState) {
    item(key = "swatches") {
        var selected by rememberSaveable { mutableStateOf(state.startColor) }
        Column(Modifier.padding(horizontal = Page)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FashionColor.entries.forEach { c ->
                    Swatch(c, selected = c == selected, size = 40) { selected = c }
                }
            }
            Spacer(Modifier.height(18.dp))
            ColourDetail(selected, state)
        }
    }
    val rules = StyleGuide.knowledge.colors.rules
    if (rules.isNotEmpty()) {
        item(key = "colour-rules") { Expandable("Colour rules", "${rules.size} rules stylists use", Modifier.padding(horizontal = Page)) { rules.forEach { RuleRow(it.title, it.text) } } }
    }
}

@Composable
private fun ColourDetail(color: FashionColor, state: GuideState) {
    val k = StyleGuide.knowledge
    val profile = k.profile(color)
    Row(verticalAlignment = Alignment.CenterVertically) {
        ColorDot(color, size = 44.dp)
        Column(Modifier.padding(start = 14.dp)) {
            Text(color.label, style = MaterialTheme.typography.headlineSmall)
            val owned = state.colorCounts[color] ?: 0
            Text(
                listOfNotNull(profile?.role?.replaceFirstChar(Char::uppercase), if (owned > 0) "${plural(owned, "piece")} in your closet" else "None in your closet")
                    .joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    profile?.let { Text(it.summary, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp)) }

    val pairs = FashionColor.entries.filter { it != color }.mapNotNull { other -> k.colorPair(color, other)?.let { other to it } }
    val good = pairs.filter { it.second.score > 0 }.sortedWith(compareByDescending<Pair<FashionColor, com.kintukka.hanger.engine.ColorPair>> { it.second.score }.thenByDescending { state.colorCounts[it.first] ?: 0 })
    val bad = pairs.filter { it.second.score < 0 }.sortedBy { it.second.score }
    if (good.isNotEmpty()) {
        SubHeader("Goes with")
        good.forEach { (other, p) -> PairRow(color, other, p.why, if (p.score == 2) "Classic" else "Good", state.colorCounts[other] ?: 0) }
    }
    if (bad.isNotEmpty()) {
        SubHeader("Handle with care")
        bad.forEach { (other, p) -> PairRow(color, other, p.why, if (p.score == -2) "Clash" else "Tricky", state.colorCounts[other] ?: 0) }
    }
    val palettes = k.colors.palettes.filter { color in it.colors }
    if (palettes.isNotEmpty()) {
        SubHeader("Palettes")
        palettes.forEach { PaletteRow(it) }
    }
}

@Composable
private fun PairRow(a: FashionColor, b: FashionColor, why: String, verdict: String, owned: Int) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Box(Modifier.width(44.dp)) {
            ColorDot(b, size = 26.dp, modifier = Modifier.padding(start = 14.dp))
            ColorDot(a, size = 26.dp)
        }
        Column(Modifier.weight(1f).padding(start = 10.dp)) {
            Text("${b.label} · $verdict", style = MaterialTheme.typography.titleSmall)
            Text(why, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (owned > 0) Text("You own ${plural(owned, "piece")} in ${b.label.lowercase()}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun PaletteRow(p: Palette) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Row(Modifier.width(76.dp)) { p.colors.take(4).forEach { ColorDot(it, size = 18.dp, modifier = Modifier.padding(end = 2.dp)) } }
        Column(Modifier.weight(1f).padding(start = 8.dp)) {
            Text(p.name, style = MaterialTheme.typography.titleSmall)
            Text(p.why, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Swatch(color: FashionColor, selected: Boolean, size: Int, onClick: () -> Unit) {
    val fill = Color(color.rgb or 0xFF000000.toInt())
    Box(
        Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(fill)
            .border(if (selected) 3.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, CircleShape)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.RadioButton
                this.selected = selected
                contentDescription = color.label
            },
        contentAlignment = Alignment.Center,
    ) {
        if (selected) Box(Modifier.size(10.dp).clip(CircleShape).background(if (fill.luminance() > 0.5f) Color.Black else Color.White))
    }
}

// --- Formulas -----------------------------------------------------------------------------------------------------

private fun LazyListScope.formulas(
    state: GuideState,
    occasion: Occasion?,
    onOccasion: (Occasion?) -> Unit,
    showAll: Boolean,
    onToggleAll: () -> Unit,
    open: (List<Long>) -> Unit,
) {
    fun fits(f: Formula) = occasion == null || occasion in f.occasions
    val ready = state.ready.filter { fits(it.formula) }
    val gaps = state.oneAway.filter { fits(it.formula) }
    val all = StyleGuide.knowledge.formulas.filter(::fits)
    item(key = "occ-filter") {
        LazyRow(contentPadding = PaddingValues(horizontal = Page), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { FilterChip(selected = occasion == null, onClick = { onOccasion(null) }, label = { Text("All") }) }
            items(Occasion.entries) { o -> FilterChip(selected = occasion == o, onClick = { onOccasion(if (occasion == o) null else o) }, label = { Text(o.label) }) }
        }
    }
    item(key = "ready-h") { SectionHeader("Ready to wear · ${ready.size}", Modifier.padding(horizontal = Page)) }
    if (ready.isEmpty()) item(key = "ready-none") { Hint("Nothing ready for this yet. The formulas one piece away show what would unlock one.") }
    items(ready, key = { "r-" + it.formula.id }) { m ->
        FormulaCard(m.formula, m.itemIds, state.items, Modifier.padding(horizontal = Page), action = "Try it" to { open(m.itemIds) })
    }
    if (gaps.isNotEmpty()) {
        item(key = "gap-h") { SectionHeader("One piece away · ${gaps.size}", Modifier.padding(horizontal = Page)) }
        items(gaps, key = { "g-" + it.formula.id }) { g ->
            FormulaCard(g.formula, g.itemIds, state.items, Modifier.padding(horizontal = Page), missing = describe(g.missing))
        }
    }
    item(key = "all-h") {
        SectionHeader("Every formula · ${all.size}", Modifier.padding(horizontal = Page), action = if (showAll) "Hide" else "Show", onAction = onToggleAll)
    }
    if (showAll) items(all, key = { "a-" + it.id }) { f -> FormulaRow(f, Modifier.padding(horizontal = Page)) }
}

@Composable
private fun FormulaCard(
    formula: Formula,
    itemIds: List<Long>,
    items: Map<Long, Item>,
    modifier: Modifier = Modifier,
    action: Pair<String, () -> Unit>? = null,
    missing: String? = null,
) {
    GuideCard(modifier) {
        Row {
            ItemsCollage(itemIds, items, modifier = Modifier.size(92.dp))
            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                Text(formula.name, style = MaterialTheme.typography.titleMedium)
                Text(formula.why, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                if (formula.occasions.isNotEmpty()) {
                    Text(formula.occasions.joinToString(" · ") { it.label }, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
        missing?.let {
            Text("Missing: $it", style = MaterialTheme.typography.titleSmall, color = HangerTokens.extras.warning, modifier = Modifier.padding(top = 10.dp))
        }
        action?.let { (label, onClick) ->
            FilledTonalButton(onClick = onClick, modifier = Modifier.padding(top = 10.dp)) { Text(label) }
        }
    }
}

@Composable
private fun FormulaRow(f: Formula, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(f.name, style = MaterialTheme.typography.titleSmall)
        Text(
            f.slots.joinToString(" + ") { describe(it) + if (it.optional) " (optional)" else "" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}

/** "navy or grey trousers", "any shoes", "a brown belt"... */
internal fun describe(slot: FormulaSlot): String {
    val colours = slot.colors.joinToString(" or ") { it.label.lowercase() }
    val noun = if (slot.types.isEmpty()) categoryNoun(slot.category) else slot.types.joinToString(" or ") { it.lowercase() }
    return listOf(colours, noun).filter { it.isNotBlank() }.joinToString(" ")
}

private fun categoryNoun(c: Category): String = when (c) {
    Category.TOP -> "any top"
    Category.BOTTOM -> "any bottoms"
    Category.ONE_PIECE -> "any dress or jumpsuit"
    Category.LAYER -> "any layer"
    Category.OUTERWEAR -> "any coat or jacket"
    Category.SHOES -> "any shoes"
    Category.BAG -> "any bag"
    Category.ACCESSORY -> "an accessory"
    Category.OTHER -> "anything"
}

// --- Trends -------------------------------------------------------------------------------------------------------

private fun LazyListScope.trends(state: GuideState, openItem: (Long) -> Unit) {
    val k = StyleGuide.knowledge
    val live = k.liveTrends(state.today).sortedByDescending { state.trendPieces[it.id]?.size ?: 0 }
    val next = k.upcomingTrends(state.today)
    k.trends.colourOfTheYear.sortedByDescending { it.year }.forEach { c ->
        item(key = "coty-${c.by}-${c.year}") {
            GuideCard(Modifier.padding(horizontal = Page)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    c.nearest?.let { ColorDot(it, size = 36.dp) }
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("${c.by} colour of the year ${c.year}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(c.name, style = MaterialTheme.typography.titleLarge)
                    }
                }
                Text(c.summary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
    item(key = "live-h") { SectionHeader("Live now · ${live.size}", Modifier.padding(horizontal = Page)) }
    if (live.isEmpty()) item(key = "live-empty") { Hint("This season's trends have run their course. An app update brings the next season's.") }
    items(live, key = { "live-" + it.id }) { t -> TrendCard(t, state.trendPieces[t.id].orEmpty(), state.items, openItem, Modifier.padding(horizontal = Page)) }
    if (next.isNotEmpty()) {
        item(key = "next-h") { SectionHeader("Coming next", Modifier.padding(horizontal = Page)) }
        items(next, key = { "next-" + it.id }) { t -> TrendCard(t, emptyList(), state.items, openItem, Modifier.padding(horizontal = Page), upcoming = true) }
    }
    if (k.trends.fading.isNotEmpty()) {
        item(key = "fading") { Expandable("On the way out", "${k.trends.fading.size} looks losing steam", Modifier.padding(horizontal = Page)) { k.trends.fading.forEach { NoteRow(it) } } }
    }
    if (k.trends.classics.isNotEmpty()) {
        item(key = "classics") { Expandable("Trend-proof classics", "Pieces that outlast every season", Modifier.padding(horizontal = Page)) { k.trends.classics.forEach { NoteRow(it) } } }
    }
    item(key = "trend-foot") {
        val updated = runCatching { LocalDate.parse(k.trends.updated).format(DateTimeFormatter.ofPattern("d MMMM yyyy")) }.getOrNull()
        Hint("Trends researched ${updated ?: "recently"} and retired automatically when their season ends.")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrendCard(
    t: Trend,
    pieces: List<Long>,
    items: Map<Long, Item>,
    openItem: (Long) -> Unit,
    modifier: Modifier = Modifier,
    upcoming: Boolean = false,
) {
    GuideCard(modifier) {
        Text(
            listOf(t.season, t.kind.replaceFirstChar(Char::uppercase), if (upcoming) "Coming" else t.status.replaceFirstChar(Char::uppercase))
                .filter { it.isNotBlank() }.joinToString(" · "),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(t.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 2.dp))
        Text(t.summary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        if (t.howToWear.isNotBlank()) {
            Text(t.howToWear, style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic), color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
        }
        if (t.colors.isNotEmpty() || t.types.isNotEmpty()) {
            FlowRow(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                t.colors.forEach { ColorDot(it, size = 18.dp) }
                if (t.types.isNotEmpty()) Text(t.types.joinToString(", "), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (pieces.isNotEmpty()) {
            Text("From your closet", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 10.dp, bottom = 6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(pieces.mapNotNull(items::get), key = { it.id }) { item ->
                    Box(
                        Modifier.size(64.dp).clip(MaterialTheme.shapes.medium).background(HangerTokens.extras.tile).clickable { openItem(item.id) },
                    ) { ItemImage(item, Modifier.fillMaxSize().padding(6.dp)) }
                }
            }
        }
    }
}

@Composable
private fun NoteRow(n: TrendNote) {
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(n.name, style = MaterialTheme.typography.titleSmall)
        Text(n.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// --- Guide (rules, dress codes, weather, fabrics, essentials, sources) --------------------------------------------

private fun LazyListScope.guide(state: GuideState) {
    val k = StyleGuide.knowledge
    k.styleRules.groupBy { it.topic }.toSortedMap().forEach { (topic, rules) ->
        item(key = "rules-$topic") {
            Expandable(topic.replaceFirstChar(Char::uppercase), plural(rules.size, "rule"), Modifier.padding(horizontal = Page)) {
                rules.forEach { RuleRow(it.title, it.text) }
            }
        }
    }
    if (k.dressCodes.isNotEmpty()) {
        item(key = "codes") {
            Expandable("Dress codes", "What the invitation really means", Modifier.padding(horizontal = Page)) {
                k.dressCodes.forEach { d ->
                    RuleRow(d.name, d.summary + if (d.examples.isNotEmpty()) "\nThink: " + d.examples.joinToString(", ").lowercase() else "")
                }
            }
        }
    }
    if (k.occasions.isNotEmpty()) {
        item(key = "occasions") {
            Expandable("Occasions", "How Hanger dresses each one", Modifier.padding(horizontal = Page)) {
                Occasion.entries.forEach { o ->
                    val g = k.occasions[o] ?: return@forEach
                    RuleRow(o.label, g.summary + if (g.avoid.isNotEmpty()) "\nSkip: " + g.avoid.joinToString(", ").lowercase() else "")
                }
            }
        }
    }
    if (k.weather.isNotEmpty()) {
        item(key = "weather") {
            Expandable("Dressing for the weather", plural(k.weather.size, "rule"), Modifier.padding(horizontal = Page)) {
                k.weather.forEach { w -> RuleRow(w.title, w.advice) }
            }
        }
    }
    if (k.fabrics.isNotEmpty()) {
        item(key = "fabrics") {
            Expandable("Fabrics", "${k.fabrics.size} materials and how to wear them", Modifier.padding(horizontal = Page)) {
                k.fabrics.forEach { f -> RuleRow(f.material, f.tip + if (f.care.isNotBlank()) "\nCare: " + f.care else "") }
            }
        }
    }
    val essentials = k.wardrobe.essentials
    if (essentials.isNotEmpty()) {
        item(key = "essentials") {
            Expandable("Essentials", "${state.essentialsOwned.size} of ${essentials.size} in your closet", Modifier.padding(horizontal = Page), startOpen = true) {
                essentials.forEach { e ->
                    val owned = e.name in state.essentialsOwned
                    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            if (owned) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = if (owned) "In your closet" else "Not in your closet",
                            tint = if (owned) HangerTokens.extras.success else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp),
                        )
                        Column(Modifier.padding(start = 10.dp)) {
                            Text(e.name, style = MaterialTheme.typography.titleSmall)
                            Text(e.why, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
    k.wardrobe.ratios?.let { r ->
        item(key = "ratios") { GuideCard(Modifier.padding(horizontal = Page)) { Text("Closet balance", style = MaterialTheme.typography.titleMedium); Text(r.summary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp)) } }
    }
    item(key = "sources") {
        val uri = LocalUriHandler.current
        Expandable("Sources", "Built from ${plural(k.sources.size, "source")}, stored on your phone", Modifier.padding(horizontal = Page)) {
            k.sources.forEach { s ->
                Column(
                    Modifier.fillMaxWidth()
                        .clickable(enabled = s.url.isNotBlank()) { runCatching { uri.openUri(s.url) } }
                        .padding(vertical = 6.dp),
                ) {
                    Text(s.title, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(listOf(s.publisher, s.date).filter { it.isNotBlank() }.joinToString(" · "), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// --- Shared bits --------------------------------------------------------------------------------------------------

@Composable
private fun GuideCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth(),
    ) { Column(Modifier.padding(16.dp)) { content() } }
}

@Composable
private fun Expandable(title: String, subtitle: String, modifier: Modifier = Modifier, startOpen: Boolean = false, content: @Composable () -> Unit) {
    var open by rememberSaveable(title) { mutableStateOf(startOpen) }
    GuideCard(modifier) {
        Row(Modifier.fillMaxWidth().clickable { open = !open }, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(if (open) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, contentDescription = if (open) "Collapse" else "Expand")
        }
        AnimatedVisibility(open) { Column(Modifier.padding(top = 8.dp)) { content() } }
    }
}

@Composable
private fun RuleRow(title: String, text: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SubHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 20.dp, bottom = 4.dp))
}

@Composable
private fun Hint(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = Page),
    )
}
