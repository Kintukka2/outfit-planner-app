package com.kintukka.hanger.widget

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.kintukka.hanger.HangerApp
import com.kintukka.hanger.MainActivity
import com.kintukka.hanger.data.db.displayImageFile
import com.kintukka.hanger.ui.formatTemp
import java.time.LocalDate

private data class WidgetState(
    val title: String,
    val subtitle: String,
    val images: List<Bitmap>,
)

/** Home-screen widget with today's planned (or suggested) outfit. */
class TodayWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = load(context)
        provideContent { WidgetContent(state) }
    }

    private suspend fun load(context: Context): WidgetState {
        val container = (context.applicationContext as HangerApp).container
        val today = LocalDate.now()
        val settings = container.settings.current()
        val items = container.repo.snapshotItems().associateBy { it.id }
        val entry = container.repo.snapshotDayEntries().firstOrNull { it.date == today }
        val ids = entry?.itemIds ?: container.stylist
            .suggest(today, settings.defaultOccasion, seed = today.toEpochDay(), count = 1)
            .firstOrNull()?.itemIds.orEmpty()
        val weather = runCatching { container.stylist.weatherFor(today) }.getOrNull()
        val bitmaps = ids.mapNotNull { items[it]?.entity }
            .mapNotNull { e -> e.displayImageFile }
            .take(4)
            .mapNotNull { container.images.decodeFile(it, 160) }
        val title = when {
            ids.isEmpty() -> "Add clothes to get outfits"
            entry?.worn == true -> "Wearing today"
            entry != null -> "Planned for today"
            else -> "Today's pick"
        }
        val subtitle = weather?.let {
            "${formatTemp(it.tempMinC, settings.tempUnit)}–${formatTemp(it.tempMaxC, settings.tempUnit)} · ${it.condition.label}"
        } ?: "Hanger"
        return WidgetState(title, subtitle, bitmaps)
    }

    companion object {
        suspend fun refresh(context: Context) = TodayWidget().updateAll(context)
    }
}

@Composable
private fun WidgetContent(state: WidgetState) {
    val ink = ColorProvider(Color(0xFF1A1614))
    val muted = ColorProvider(Color(0xFF6B625C))
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F4))
            .cornerRadius(20.dp)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>()),
    ) {
        Text(state.title, style = TextStyle(color = ink, fontSize = 15.sp, fontWeight = FontWeight.Bold))
        Text(state.subtitle, style = TextStyle(color = muted, fontSize = 12.sp))
        Spacer(GlanceModifier.height(8.dp))
        Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            state.images.forEachIndexed { index, bitmap ->
                if (index > 0) Spacer(GlanceModifier.width(6.dp))
                Image(
                    provider = ImageProvider(bitmap),
                    contentDescription = null,
                    modifier = GlanceModifier.size(56.dp).background(Color(0xFFF1ECE6)).cornerRadius(12.dp).padding(4.dp),
                )
            }
        }
    }
}

class TodayWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodayWidget()
}
