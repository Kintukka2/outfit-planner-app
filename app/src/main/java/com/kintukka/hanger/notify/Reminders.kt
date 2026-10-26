package com.kintukka.hanger.notify

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kintukka.hanger.HangerApp
import com.kintukka.hanger.MainActivity
import com.kintukka.hanger.R
import com.kintukka.hanger.ui.formatTemp
import com.kintukka.hanger.widget.TodayWidget
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object Reminders {
    private const val CHANNEL_ID = "daily_outfit"
    private const val WORK_NAME = "daily_outfit"
    private const val NOTIFICATION_ID = 1001

    fun createChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, "Outfit of the day", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "A morning nudge with today's planned or suggested outfit"
        }
        context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    fun schedule(context: Context, enabled: Boolean, minutes: Int) {
        val wm = runCatching { WorkManager.getInstance(context) }.getOrNull() ?: return
        if (!enabled) {
            wm.cancelUniqueWork(WORK_NAME)
            return
        }
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atStartOfDay().plusMinutes(minutes.toLong())
        if (!next.isAfter(now)) next = next.plusDays(1)
        val delay = Duration.between(now, next).toMinutes()
        val request = PeriodicWorkRequestBuilder<DailyOutfitWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .build()
        wm.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun canNotify(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    internal fun post(context: Context, title: String, text: String) {
        if (!canNotify(context)) return
        val intent = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_hanger)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification) }
    }
}

/** Morning job: notify with today's look and refresh the home-screen widget. */
class DailyOutfitWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val container = (applicationContext as HangerApp).container
        val today = LocalDate.now()
        val settings = container.settings.current()
        val items = container.repo.allItems.value.ifEmpty { container.repo.snapshotItems() }.associateBy { it.id }
        val planned = container.repo.dayEntries.value.firstOrNull { it.date == today && !it.worn }
        val weather = runCatching { container.stylist.weatherFor(today) }.getOrNull()
        val weatherText = weather?.let { "${formatTemp(it.tempMinC, settings.tempUnit)}–${formatTemp(it.tempMaxC, settings.tempUnit)}, ${it.condition.label.lowercase()}" }

        val (title, names) = if (planned != null) {
            "Today's planned outfit" to planned.itemIds.mapNotNull { items[it]?.name }
        } else {
            val suggestion = container.stylist.suggest(today, settings.defaultOccasion, seed = today.toEpochDay(), count = 1).firstOrNull()
            "Your outfit for today" to suggestion?.itemIds.orEmpty().mapNotNull { items[it]?.name }
        }
        if (names.isNotEmpty()) {
            val text = buildString {
                append(names.joinToString(" · "))
                weatherText?.let { append("\n").append(it.replaceFirstChar(Char::uppercase)) }
            }
            Reminders.post(applicationContext, title, text)
        }
        runCatching { TodayWidget.refresh(applicationContext) }
        return Result.success()
    }
}
