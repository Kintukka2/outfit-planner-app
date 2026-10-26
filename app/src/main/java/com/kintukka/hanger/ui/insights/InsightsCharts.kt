package com.kintukka.hanger.ui.insights

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.ui.components.ColorDot
import com.kintukka.hanger.ui.plural
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.min

private fun FashionColor.composeColor() = Color(rgb or 0xFF000000.toInt())

private fun MonthBar.longName(): String =
    month.month.getDisplayName(TextStyle.FULL, Locale.UK) + " " + month.year

/** Spoken summary of the monthly chart, since the bars themselves carry no text for screen readers. */
internal fun monthlySummary(months: List<MonthBar>): String = buildString {
    append("Outfits logged per month over the last ${months.size} months. ")
    append(months.joinToString("; ") { m -> "${m.longName()}${if (m.current) " (this month)" else ""}: ${m.count}" })
    months.maxByOrNull { it.count }?.takeIf { it.count > 0 }?.let { append(". Busiest month: ${it.longName()} with ${it.count}.") }
}

/**
 * 12-month bar chart. Rounded bars in primary; the current month is drawn at full strength, the rest muted.
 * Month initials sit under each bar, and the count is printed above the tallest bar and the current month.
 */
@Composable
internal fun MonthlyBarChart(months: List<MonthBar>, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val muted = primary.copy(alpha = 0.35f)
    val empty = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strongColor = MaterialTheme.colorScheme.onSurface
    val labelStyle = MaterialTheme.typography.labelSmall
    val valueStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
    val measurer = rememberTextMeasurer()
    val description = remember(months) { monthlySummary(months) }
    val initials = remember(months) {
        months.map { it.month.month.getDisplayName(TextStyle.NARROW, Locale.UK) }
    }

    Canvas(
        modifier
            .fillMaxWidth()
            .height(176.dp)
            .clearAndSetSemantics { contentDescription = description },
    ) {
        if (months.isEmpty()) return@Canvas
        val labelBand = 22.dp.toPx()
        val valueBand = 22.dp.toPx()
        val chartBottom = size.height - labelBand
        val chartHeight = chartBottom - valueBand
        val slot = size.width / months.size
        val barWidth = min(slot * 0.58f, 26.dp.toPx())
        val radius = min(barWidth / 2f, 8.dp.toPx())
        val max = months.maxOf { it.count }.coerceAtLeast(1)
        // Prefer the most recent month when several share the top value.
        val tallest = months.indexOfLast { it.count == max }.takeIf { months[it].count > 0 }

        months.forEachIndexed { i, m ->
            val centre = slot * i + slot / 2f
            val height = if (m.count == 0) 3.dp.toPx() else (chartHeight * m.count / max).coerceAtLeast(2 * radius)
            val top = chartBottom - height
            drawRoundRect(
                color = when {
                    m.count == 0 -> empty
                    m.current -> primary
                    else -> muted
                },
                topLeft = Offset(centre - barWidth / 2f, top),
                size = Size(barWidth, height),
                cornerRadius = CornerRadius(if (m.count == 0) 1.5.dp.toPx() else radius),
            )

            val initial = measurer.measure(initials[i], labelStyle.copy(fontWeight = if (m.current) FontWeight.Bold else FontWeight.Medium))
            drawText(
                initial,
                color = if (m.current) strongColor else labelColor,
                topLeft = Offset(centre - initial.size.width / 2f, chartBottom + 6.dp.toPx()),
            )

            if (i == tallest || (m.current && m.count > 0)) {
                val value = measurer.measure(m.count.toString(), valueStyle)
                drawText(
                    value,
                    color = if (m.current) primary else strongColor,
                    topLeft = Offset(centre - value.size.width / 2f, top - value.size.height - 3.dp.toPx()),
                )
            }
        }
    }
}

/** Horizontal proportional bars: one per category, with the label and count always in text. */
@Composable
internal fun CategoryBars(categories: List<Pair<Category, Int>>, total: Int, modifier: Modifier = Modifier) {
    val max = categories.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.forEach { (category, count) ->
            val share = if (total > 0) (count * 100f / total).toInt() else 0
            Column(
                Modifier
                    .fillMaxWidth()
                    .clearAndSetSemantics { contentDescription = "${category.label}: ${plural(count, "item")}, $share percent" },
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(category.label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text(
                        count.toString(),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        "  $share%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(count.toFloat() / max)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                    )
                }
            }
        }
    }
}

/**
 * The closet's colour mix as one strip of segments sized by count, followed by labelled legend chips
 * for the top colours (the strip alone would rely on colour to carry meaning).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ColorPalette(colors: List<Pair<FashionColor, Int>>, modifier: Modifier = Modifier, legendCount: Int = 6) {
    val description = remember(colors) {
        "Colour mix by main colour: " + colors.joinToString(", ") { (c, n) -> "${c.label} $n" }
    }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(MaterialTheme.shapes.small)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.small)
                .semantics { contentDescription = description },
        ) {
            colors.forEach { (color, count) ->
                Box(
                    Modifier
                        .weight(count.toFloat())
                        .fillMaxHeight()
                        .background(color.composeColor()),
                )
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            colors.take(legendCount).forEach { (color, count) ->
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.clearAndSetSemantics { contentDescription = "${color.label}, $count" },
                ) {
                    Row(
                        Modifier.padding(start = 8.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ColorDot(color, size = 16.dp)
                        Spacer(Modifier.width(6.dp))
                        Text(color.label, style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.width(6.dp))
                        Text(count.toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            val rest = colors.size - legendCount
            if (rest > 0) {
                Text(
                    "+ ${plural(rest, "more colour")}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 4.dp, vertical = 6.dp),
                )
            }
        }
    }
}
