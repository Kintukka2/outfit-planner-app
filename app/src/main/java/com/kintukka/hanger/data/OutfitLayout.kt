package com.kintukka.hanger.data

import com.kintukka.hanger.engine.Category

/**
 * Flat-lay auto arrangement for an outfit canvas with a 4:5 aspect ratio.
 * x/y are the item centre as fractions of canvas width/height; scale 1 = 42% of canvas width.
 */
object OutfitLayout {
    const val ASPECT = 0.8f // width / height
    const val BASE_SIZE = 0.42f

    fun arrange(items: List<Pair<Long, Category>>): List<Placement> {
        val hasOuter = items.any { it.second == Category.OUTERWEAR }
        val hasLayer = items.any { it.second == Category.LAYER }
        val hasOnePiece = items.any { it.second == Category.ONE_PIECE }
        var accessoryIndex = 0
        var shoeIndex = 0
        val placed = items.map { (id, category) ->
            when (category) {
                Category.OUTERWEAR -> Placement(id, 0.30f, 0.30f, 1.12f, z = 0)
                Category.LAYER -> if (hasOuter) Placement(id, 0.70f, 0.24f, 0.95f, z = 1)
                else Placement(id, 0.31f, 0.29f, 1.02f, z = 1)
                Category.TOP -> when {
                    hasOuter && hasLayer -> Placement(id, 0.52f, 0.36f, 0.92f, z = 2)
                    hasOuter || hasLayer -> Placement(id, 0.68f, 0.28f, 0.98f, z = 2)
                    else -> Placement(id, 0.50f, 0.25f, 1.05f, z = 2)
                }
                Category.ONE_PIECE -> Placement(id, if (hasOuter || hasLayer) 0.64f else 0.5f, 0.42f, 1.55f, z = 2)
                Category.BOTTOM -> Placement(id, if (hasOuter || hasLayer) 0.60f else 0.50f, 0.64f, 1.15f, z = 3)
                Category.SHOES -> Placement(id, if (shoeIndex++ == 0) (if (hasOnePiece) 0.28f else 0.24f) else 0.50f, 0.88f, 0.70f, z = 4)
                Category.BAG -> Placement(id, 0.82f, 0.82f, 0.62f, z = 5)
                Category.ACCESSORY -> when (accessoryIndex++) {
                    0 -> Placement(id, 0.84f, 0.10f, 0.45f, z = 6)
                    1 -> Placement(id, 0.16f, 0.62f, 0.45f, z = 6)
                    else -> Placement(id, 0.84f, 0.56f, 0.40f, z = 6)
                }
                Category.OTHER -> Placement(id, 0.18f, 0.66f, 0.5f, z = 6)
            }
        }
        return placed.sortedBy { it.z }
    }
}
