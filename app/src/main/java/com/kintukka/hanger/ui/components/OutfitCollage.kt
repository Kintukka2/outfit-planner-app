package com.kintukka.hanger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.kintukka.hanger.data.Item
import com.kintukka.hanger.data.OutfitLayout
import com.kintukka.hanger.data.Placement
import com.kintukka.hanger.ui.theme.HangerTokens

/** Read-only flat-lay of an outfit. */
@Composable
fun OutfitCollage(
    placements: List<Placement>,
    items: Map<Long, Item>,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    background: Color = HangerTokens.extras.tile,
) {
    val description = placements.mapNotNull { items[it.itemId]?.name }.joinToString(", ")
    BoxWithConstraints(
        modifier
            .aspectRatio(OutfitLayout.ASPECT)
            .clip(shape)
            .background(background)
            .semantics { contentDescription = "Outfit: $description" },
    ) {
        val w = maxWidth
        val h = maxHeight
        placements.sortedBy { it.z }.forEach { p ->
            val item = items[p.itemId] ?: return@forEach
            val side = w * OutfitLayout.BASE_SIZE * p.scale
            ItemImage(
                item,
                Modifier
                    .offset(x = w * p.x - side / 2, y = h * p.y - side / 2)
                    .size(side)
                    .graphicsLayer { rotationZ = p.rotation },
            )
        }
    }
}

/** Collage for a plain list of item ids (suggestions, calendar entries) using the auto flat-lay. */
@Composable
fun ItemsCollage(
    itemIds: List<Long>,
    items: Map<Long, Item>,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    background: Color = HangerTokens.extras.tile,
) {
    val placements = remember(itemIds, items.keys) {
        OutfitLayout.arrange(itemIds.mapNotNull { id -> items[id]?.let { id to it.category } })
    }
    OutfitCollage(placements, items, modifier, shape, background)
}
