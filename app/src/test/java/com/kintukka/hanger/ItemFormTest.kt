package com.kintukka.hanger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.ui.closet.CategoryPicker
import com.kintukka.hanger.ui.closet.ColorPicker
import com.kintukka.hanger.ui.closet.ItemForm
import com.kintukka.hanger.ui.closet.ItemNameField
import com.kintukka.hanger.ui.closet.SubcategoryPicker
import com.kintukka.hanger.ui.theme.HangerTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** The add/edit item form: name field typing and the category, type and colour dropdowns. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel7)
@OptIn(ExperimentalRoborazziApi::class)
class ItemFormTest {

    @get:Rule
    val compose = createComposeRule()

    private var form by mutableStateOf(ItemForm(category = Category.BOTTOM, subcategory = "Jeans", colors = listOf(FashionColor.CHARCOAL)))

    private fun show() = compose.setContent {
        HangerTheme(darkTheme = true) {
            Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
                Column(Modifier.padding(16.dp)) {
                    ItemNameField(form, onEdit = { form = it(form) }, modifier = Modifier.testTag("name"))
                    CategoryPicker(form.category, onSelect = { form = form.withCategory(it) }, modifier = Modifier.padding(top = 16.dp))
                    SubcategoryPicker(form.category, form.subcategory, onSelect = { form = form.withPreset(it) }, modifier = Modifier.padding(top = 10.dp))
                    ColorPicker(form.colors, onChange = { form = form.copy(colors = it) }, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }

    private fun nameFieldShows(text: String) =
        compose.onNodeWithTag("name").assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString(text)))

    @Test
    fun clearingTheNameLeavesItEmptyAndSpacesStick() {
        show()
        nameFieldShows("Charcoal jeans")

        compose.onNodeWithTag("name").performTextClearance()
        nameFieldShows("")
        assertEquals("An empty name still saves as the suggestion", "Charcoal jeans", form.displayName())

        compose.onNodeWithTag("name").performTextInput("Grey ")
        nameFieldShows("Grey ")
        compose.onNodeWithTag("name").performTextInput("tee")
        nameFieldShows("Grey tee")
        assertEquals("Grey tee", form.displayName())
    }

    @Test
    fun dropdownsPickCategoryTypeAndColours() {
        show()
        compose.onNodeWithText("Bottoms").performClick()
        compose.onNodeWithText("Tops").performClick()
        assertEquals(Category.TOP, form.category)
        compose.onAllNodesWithText("Bottoms").assertCountEquals(0)

        compose.onNodeWithText("Type").performClick()
        compose.onNodeWithText("T-shirt").performClick()
        assertEquals("T-shirt", form.subcategory)
        // The suggested name follows the new type until the user types their own.
        nameFieldShows("Charcoal t-shirt")

        compose.onNodeWithText("Charcoal").performClick()
        compose.onNodeWithText("Navy").performScrollTo().performClick()
        compose.onNodeWithText("White").performScrollTo().performClick()
        assertEquals(listOf(FashionColor.CHARCOAL, FashionColor.NAVY, FashionColor.WHITE), form.colors)
        compose.waitForIdle()
        captureScreenRoboImage("screenshots/form_colour_menu.png")

        // Tapping a picked colour removes it; the menu stays open until Done.
        compose.onNodeWithText("Charcoal").performClick()
        assertEquals(listOf(FashionColor.NAVY, FashionColor.WHITE), form.colors)
        compose.onNodeWithText("Done").performScrollTo().performClick()
        compose.onAllNodesWithText("Done").assertCountEquals(0)
        captureScreenRoboImage("screenshots/form_fields.png")
    }

    @Test
    fun categoryMenuLooksRight() {
        show()
        compose.onNodeWithText("Bottoms").performClick()
        compose.waitForIdle()
        captureScreenRoboImage("screenshots/form_category_menu.png")
    }
}
