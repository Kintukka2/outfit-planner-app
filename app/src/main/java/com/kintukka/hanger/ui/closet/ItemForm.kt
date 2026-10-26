package com.kintukka.hanger.ui.closet

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kintukka.hanger.data.db.ItemEntity
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.Formality
import com.kintukka.hanger.engine.LaundryRules
import com.kintukka.hanger.engine.Pattern
import com.kintukka.hanger.engine.Season
import com.kintukka.hanger.engine.SubcategoryPreset
import com.kintukka.hanger.engine.Subcategories
import com.kintukka.hanger.engine.Warmth
import java.time.LocalDate

/** Editable fields shared by the add flow and the edit screen. */
data class ItemForm(
    val name: String = "",
    val nameEdited: Boolean = false,
    val category: Category = Category.TOP,
    val subcategory: String = "",
    val colors: List<FashionColor> = emptyList(),
    val pattern: Pattern = Pattern.SOLID,
    val formality: Int = Formality.CASUAL,
    val warmth: Int = 2,
    val seasons: Set<Season> = emptySet(),
    val sporty: Boolean = false,
    val waterproof: Boolean = false,
    val wearsBeforeWash: Int = 1,
    val brand: String = "",
    val size: String = "",
    val material: String = "",
    val price: String = "",
    val purchaseDate: LocalDate? = null,
    val notes: String = "",
    val tags: String = "",
    val wishlist: Boolean = false,
) {
    /** "Navy jeans", "Cream knit sweater", "Shoes"… */
    fun suggestedName(): String {
        val noun = subcategory.ifBlank { category.label.removeSuffix("s").ifBlank { category.label } }
        val color = colors.firstOrNull()?.label
        return listOfNotNull(color, noun.lowercase()).joinToString(" ").replaceFirstChar(Char::uppercase)
    }

    fun displayName(): String = if (nameEdited && name.isNotBlank()) name.trim() else suggestedName()

    fun withPreset(preset: SubcategoryPreset): ItemForm = copy(
        category = preset.category,
        subcategory = preset.name,
        formality = preset.formality,
        warmth = preset.warmth,
        wearsBeforeWash = preset.wearsBeforeWash,
        sporty = preset.sporty,
        waterproof = preset.waterproof,
        seasons = preset.seasons.ifEmpty { seasons },
    )

    fun withCategory(category: Category): ItemForm =
        if (category == this.category) this
        else copy(
            category = category,
            subcategory = "",
            wearsBeforeWash = runCatching { LaundryRules.defaultWearsBeforeWash(category) }.getOrDefault(wearsBeforeWash),
        )

    fun applyTo(entity: ItemEntity): ItemEntity = entity.copy(
        name = displayName(),
        category = category,
        subcategory = subcategory.trim(),
        colors = colors,
        pattern = pattern,
        formality = formality,
        warmth = warmth,
        seasons = seasons,
        sporty = sporty,
        waterproof = waterproof,
        wearsBeforeWash = wearsBeforeWash,
        brand = brand.trim(),
        size = size.trim(),
        material = material.trim(),
        price = price.replace(",", ".").toDoubleOrNull(),
        purchaseDate = purchaseDate,
        notes = notes.trim(),
        tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        wishlist = wishlist,
    )

    companion object {
        fun from(e: ItemEntity) = ItemForm(
            name = e.name,
            nameEdited = true,
            category = e.category,
            subcategory = e.subcategory,
            colors = e.colors,
            pattern = e.pattern,
            formality = e.formality,
            warmth = e.warmth,
            seasons = e.seasons,
            sporty = e.sporty,
            waterproof = e.waterproof,
            wearsBeforeWash = e.wearsBeforeWash,
            brand = e.brand,
            size = e.size,
            material = e.material,
            price = e.price?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() }.orEmpty(),
            purchaseDate = e.purchaseDate,
            notes = e.notes,
            tags = e.tags.joinToString(", "),
            wishlist = e.wishlist,
        )
    }
}

val pickerCategories = listOf(
    Category.TOP, Category.BOTTOM, Category.ONE_PIECE, Category.LAYER, Category.OUTERWEAR,
    Category.SHOES, Category.BAG, Category.ACCESSORY,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryPicker(selected: Category, onSelect: (Category) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        pickerCategories.forEach { c ->
            FilterChip(selected = c == selected, onClick = { onSelect(c) }, label = { Text(c.label) })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubcategoryPicker(category: Category, selected: String, onSelect: (SubcategoryPreset) -> Unit, modifier: Modifier = Modifier) {
    val presets = runCatching { Subcategories.forCategory(category) }.getOrDefault(emptyList())
    if (presets.isEmpty()) return
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        presets.forEach { p ->
            FilterChip(
                selected = p.name.equals(selected, ignoreCase = true),
                onClick = { onSelect(p) },
                label = { Text(p.name) },
            )
        }
    }
}

/** Multi-select swatch grid; selection order is shown (1 = main colour). Max 3. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(selected: List<FashionColor>, onChange: (List<FashionColor>) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FashionColor.entries.forEach { color ->
            val index = selected.indexOf(color)
            val swatch = Color(color.rgb or 0xFF000000.toInt())
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(swatch)
                    .border(
                        width = if (index >= 0) 3.dp else 1.dp,
                        color = if (index >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape,
                    )
                    .clickable {
                        onChange(
                            when {
                                index >= 0 -> selected - color
                                selected.size >= 3 -> selected.dropLast(1) + color
                                else -> selected + color
                            },
                        )
                    }
                    .semantics {
                        role = Role.Checkbox
                        this.selected = index >= 0
                        contentDescription = color.label + if (index == 0) ", main colour" else ""
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (index >= 0) {
                    Text(
                        "${index + 1}",
                        color = if (swatch.luminance() > 0.5f) Color.Black else Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatternPicker(selected: Pattern, onSelect: (Pattern) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Pattern.entries.forEach { p -> FilterChip(selected = p == selected, onClick = { onSelect(p) }, label = { Text(p.label) }) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeasonPicker(selected: Set<Season>, onChange: (Set<Season>) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = selected.isEmpty(), onClick = { onChange(emptySet()) }, label = { Text("All year") })
        Season.entries.forEach { s ->
            FilterChip(
                selected = s in selected,
                onClick = { onChange(if (s in selected) selected - s else selected + s) },
                label = { Text(s.label) },
            )
        }
    }
}

@Composable
fun LevelSlider(title: String, value: Int, label: (Int) -> String, onChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
            Text(label(value), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.toInt().coerceIn(1, 5)) },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.semantics { contentDescription = "$title: ${label(value)}" },
        )
    }
}

@Composable
fun FormalitySlider(value: Int, onChange: (Int) -> Unit) =
    LevelSlider("Formality", value, Formality::label, onChange)

@Composable
fun WarmthSlider(value: Int, onChange: (Int) -> Unit) =
    LevelSlider("Warmth", value, Warmth::label, onChange)

@Composable
fun FieldLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 14.dp, bottom = 8.dp))
}
