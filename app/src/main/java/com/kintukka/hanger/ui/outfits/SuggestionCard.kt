package com.kintukka.hanger.ui.outfits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.engine.SuggestedOutfit
import com.kintukka.hanger.ui.components.ItemsCollage

enum class Reaction { NONE, LIKED, DISLIKED }

/** One generated outfit with the stylist's notes and quick actions. */
@Composable
fun SuggestionCard(
    suggestion: SuggestedOutfit,
    items: Map<Long, Item>,
    primaryLabel: String,
    onPrimary: () -> Unit,
    onSave: () -> Unit,
    onEdit: () -> Unit,
    reaction: Reaction,
    onReact: (Reaction) -> Unit,
    modifier: Modifier = Modifier,
    saved: Boolean = false,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(Modifier.padding(12.dp)) {
            ItemsCollage(suggestion.itemIds, items, Modifier.fillMaxWidth())
            Spacer(Modifier.size(10.dp))
            Text(
                suggestion.itemIds.mapNotNull { items[it]?.name }.joinToString(" · "),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            suggestion.reasons.take(3).forEach { reason ->
                Row(Modifier.padding(start = 4.dp, top = 6.dp), verticalAlignment = Alignment.Top) {
                    Text("—", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(6.dp))
                    Text(reason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                IconButton(onClick = { onReact(if (reaction == Reaction.LIKED) Reaction.NONE else Reaction.LIKED) }) {
                    Icon(
                        if (reaction == Reaction.LIKED) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "More like this",
                        tint = if (reaction == Reaction.LIKED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { onReact(if (reaction == Reaction.DISLIKED) Reaction.NONE else Reaction.DISLIKED) }) {
                    Icon(
                        if (reaction == Reaction.DISLIKED) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                        contentDescription = "Less like this",
                        tint = if (reaction == Reaction.DISLIKED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onSave, enabled = !saved) {
                    Icon(Icons.Outlined.BookmarkAdd, contentDescription = if (saved) "Saved" else "Save outfit")
                }
                IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, contentDescription = "Edit on canvas") }
                Spacer(Modifier.weight(1f))
                Button(onClick = onPrimary) { Text(primaryLabel) }
            }
        }
    }
}

/** A short auto name for an outfit, e.g. "Navy trousers & grey knit". */
fun autoOutfitName(items: List<Item>): String {
    val core = items.sortedBy { it.category.order }
    val pick = core.filter { it.category.name in setOf("ONE_PIECE", "LAYER", "OUTERWEAR", "TOP", "BOTTOM") }.take(2).ifEmpty { core.take(2) }
    return pick.joinToString(" & ") { it.name.replaceFirstChar(Char::lowercase) }.replaceFirstChar(Char::uppercase).ifBlank { "New outfit" }
}
