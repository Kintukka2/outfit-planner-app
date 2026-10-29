package com.kintukka.hanger.ui.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.engine.ClosetInsight
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.StyleGuide
import com.kintukka.hanger.engine.Tip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.kintukka.hanger.engine.WeatherContext
import com.kintukka.hanger.engine.WeatherRule
import com.kintukka.hanger.ui.components.ColorDot
import com.kintukka.hanger.ui.components.SectionHeader
import java.time.LocalDate

/** Today's rotating tip from the built-in guide, with a way into the full guide. */
@Composable
fun TipOfTheDayCard(date: LocalDate, onOpenGuide: () -> Unit, modifier: Modifier = Modifier) {
    val tip by produceState<Tip?>(null, date) { value = withContext(Dispatchers.Default) { runCatching { StyleGuide.knowledge.tipOfTheDay(date) }.getOrNull() } }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Style tip", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp))
            }
            Text(
                tip?.text ?: "Colour pairings, outfit formulas, dress codes and this season's trends, all on your phone.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
            )
            TextButton(onClick = onOpenGuide) { Text("Open style guide") }
        }
    }
}

/** The guide's weather rules for a day, special conditions (rain, snow, wind, sun) before temperature. */
fun weatherAdvice(weather: WeatherContext, limit: Int = 2): List<WeatherRule> =
    runCatching { StyleGuide.knowledge.weatherRulesFor(weather) }.getOrDefault(emptyList())
        .sortedByDescending { r -> with(r.condition) { (if (rain || snow) 2 else 0) + (if (windKphMin != null || uvMin != null || minHighC != null) 1 else 0) } }
        .take(limit)

/** The day's top weather advice from the guide, worked out off the main thread. */
@Composable
fun WeatherAdviceLine(weather: WeatherContext, modifier: Modifier = Modifier) {
    val advice by produceState<String?>(null, weather) {
        value = withContext(Dispatchers.Default) { weatherAdvice(weather, limit = 1).firstOrNull()?.advice }
    }
    advice?.let { Text(it, style = MaterialTheme.typography.bodySmall, modifier = modifier) }
}

/** Closet check results for Insights, with formula counts and a link to the guide. */
@Composable
fun StyleCheckCard(
    insights: List<ClosetInsight>,
    formulasReady: Int?,
    formulasOneAway: Int,
    onOpenGuide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Style check", style = MaterialTheme.typography.titleLarge)
            formulasReady?.let { n ->
                Text(
                    buildString {
                        append(if (n == 1) "Your closet makes 1 proven outfit formula" else "Your closet makes $n proven outfit formulas")
                        if (formulasOneAway > 0) append(", and $formulasOneAway more are one piece away")
                        append('.')
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
            insights.take(3).forEach { i ->
                Column(Modifier.padding(end = 8.dp)) {
                    Text(i.check.title, style = MaterialTheme.typography.titleSmall)
                    Text(i.message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            TextButton(onClick = onOpenGuide) { Text("Open style guide") }
        }
    }
}

/** What the guide says about one piece: what it pairs with, its colour and its fabric. */
@Composable
fun StyleNotes(e: ItemEntity, modifier: Modifier = Modifier) {
    val notes by produceState(emptyList<StyleNote>(), e.subcategory, e.colors, e.material) {
        value = withContext(Dispatchers.Default) { notesFor(e) }
    }
    if (notes.isEmpty()) return
    Column(modifier) {
        SectionHeader("Style notes")
        notes.forEach { (title, text, colors) ->
            Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(end = 8.dp))
                    colors.forEach { ColorDot(it, 14.dp, Modifier.padding(end = 4.dp)) }
                }
                Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private data class StyleNote(val title: String, val text: String, val colors: List<FashionColor> = emptyList())

private fun notesFor(e: ItemEntity): List<StyleNote> = runCatching {
    val k = StyleGuide.knowledge
    val out = ArrayList<StyleNote>()
    if (e.subcategory.isNotBlank()) {
        val mine = k.pairings.filter { it.a.equals(e.subcategory, true) || it.b.equals(e.subcategory, true) }
        fun other(p: com.kintukka.hanger.engine.TypePairing) = if (p.a.equals(e.subcategory, true)) p.b else p.a
        mine.filter { it.score > 0 }.sortedByDescending { it.score }.take(3).forEach { out += StyleNote("With ${other(it).lowercase()}", it.why) }
        mine.filter { it.score < 0 }.sortedBy { it.score }.take(1).forEach { out += StyleNote("Not with ${other(it).lowercase()}", it.why) }
    }
    e.colors.firstOrNull()?.let { c ->
        k.profile(c)?.let { p -> out += StyleNote(c.label, p.summary, p.bestWith.take(5)) }
    }
    k.fabricFor(e.material)?.let { f ->
        out += StyleNote(f.material, f.tip + if (f.care.isNotBlank()) " " + f.care else "")
    }
    out
}.getOrDefault(emptyList())
