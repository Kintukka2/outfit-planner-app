package com.kintukka.hanger.ui.closet

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import com.kintukka.hanger.ui.components.ColorDot
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

    /** What the name field shows: the text exactly as typed, or the live suggestion until the user types. */
    fun fieldName(): String = if (nameEdited) name else suggestedName()

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

/**
 * Item name. Shows exactly what was typed (spaces included, and empty when cleared); until the
 * user types, it shows the live suggestion, which is also what an empty name saves as.
 */
@Composable
fun ItemNameField(form: ItemForm, onEdit: ((ItemForm) -> ItemForm) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = form.fieldName(),
        onValueChange = { v -> onEdit { it.copy(name = v, nameEdited = true) } },
        label = { Text("Name") },
        placeholder = { Text(form.suggestedName()) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun CategoryPicker(selected: Category, onSelect: (Category) -> Unit, modifier: Modifier = Modifier) {
    SelectField(
        label = "Category",
        value = selected.label,
        options = pickerCategories,
        optionLabel = { it.label },
        isSelected = { it == selected },
        onSelect = onSelect,
        modifier = modifier,
    )
}

@Composable
fun SubcategoryPicker(category: Category, selected: String, onSelect: (SubcategoryPreset) -> Unit, modifier: Modifier = Modifier) {
    val presets = runCatching { Subcategories.forCategory(category) }.getOrDefault(emptyList())
    if (presets.isEmpty()) return
    SelectField(
        label = "Type",
        value = selected,
        options = presets,
        optionLabel = { it.name },
        isSelected = { it.name.equals(selected, ignoreCase = true) },
        onSelect = onSelect,
        modifier = modifier,
    )
}

/** Read-only dropdown for a single choice. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SelectField(
    label: String,
    value: String,
    options: List<T>,
    optionLabel: (T) -> String,
    isSelected: (T) -> Boolean,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                val chosen = isSelected(option)
                DropdownMenuItem(
                    text = { Text(optionLabel(option), fontWeight = if (chosen) FontWeight.SemiBold else null) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                    trailingIcon = if (chosen) CheckIcon else null,
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.semantics { selected = chosen },
                )
            }
        }
    }
}

private val CheckIcon: @Composable () -> Unit = { Icon(Icons.Filled.Check, contentDescription = null) }

/**
 * Multi-select colour dropdown, up to 3 in order (1 = main colour). The menu stays open while
 * picking; tapping a picked colour again removes it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPicker(selected: List<FashionColor>, onChange: (List<FashionColor>) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = selected.joinToString { it.label },
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Colours") },
            leadingIcon = if (selected.isEmpty()) null else ({ SwatchStack(selected) }),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = { Text("Up to 3 · the first is the main colour") },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${selected.size} of 3 picked",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = { expanded = false }) { Text("Done") }
            }
            FashionColor.entries.forEach { color ->
                val index = selected.indexOf(color)
                DropdownMenuItem(
                    text = { Text(color.label, fontWeight = if (index >= 0) FontWeight.SemiBold else null) },
                    onClick = {
                        onChange(
                            when {
                                index >= 0 -> selected - color
                                selected.size >= 3 -> selected.dropLast(1) + color
                                else -> selected + color
                            },
                        )
                    },
                    leadingIcon = { ColorDot(color, size = 24.dp) },
                    trailingIcon = if (index >= 0) ({ OrderBadge(index + 1) }) else null,
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.semantics {
                        this.selected = index >= 0
                        if (index == 0) stateDescription = "Main colour"
                    },
                )
            }
        }
    }
}

/** Overlapping dots for the picked colours, main colour on top. */
@Composable
private fun SwatchStack(colors: List<FashionColor>) {
    Box(Modifier.padding(start = 12.dp, end = 4.dp)) {
        colors.indices.reversed().forEach { i -> ColorDot(colors[i], size = 20.dp, modifier = Modifier.padding(start = (i * 13).dp)) }
    }
}

@Composable
private fun OrderBadge(number: Int) {
    Box(
        Modifier.size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Text("$number", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
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
