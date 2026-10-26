package com.kintukka.hanger.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import com.kintukka.hanger.engine.CategoryGuesser
import com.kintukka.hanger.engine.ColorTools
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.SubcategoryPreset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class ProcessedGarment(
    val originalFile: String,
    val cutoutFile: String?,
    val colors: List<FashionColor>,
    val preset: SubcategoryPreset?,
    val labels: List<String>,
)

/**
 * Turns a photo into a wardrobe-ready item, entirely on device:
 * ML Kit subject segmentation for the cut-out, ML Kit image labelling for a category guess,
 * and k-means colour extraction (engine) for named colours.
 */
class GarmentProcessor(private val context: Context, private val images: ImageStore) {

    private val segmenter by lazy {
        SubjectSegmentation.getClient(SubjectSegmenterOptions.Builder().enableForegroundBitmap().build())
    }
    private val labeler by lazy { ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS) }

    /** Ask Play services to fetch the segmentation model ahead of first use. Safe to call repeatedly. */
    fun prefetchModels() {
        runCatching {
            ModuleInstall.getClient(context)
                .installModules(ModuleInstallRequest.newBuilder().addApi(segmenter).build())
        }.onFailure { Log.w(TAG, "Model prefetch unavailable", it) }
    }

    suspend fun process(uri: Uri): ProcessedGarment = withContext(Dispatchers.Default) {
        val photo = images.decode(uri, maxSide = 1600)
        val originalFile = images.savePhoto(photo)
        processBitmap(photo, originalFile)
    }

    /** Redo the cut-out and analysis from a stored original photo. */
    suspend fun reprocess(originalFile: String): ProcessedGarment? = withContext(Dispatchers.Default) {
        val photo = images.decodeFile(originalFile, 1600) ?: return@withContext null
        processBitmap(photo, originalFile)
    }

    private suspend fun processBitmap(photo: Bitmap, originalFile: String): ProcessedGarment {
        val working = photo.scaledToMax(1024)
        val cutout = cutOut(working)
        val cutoutFile = cutout?.let(images::saveCutout)
        val colors = extractColors(cutout ?: working.centerCrop(0.6f))
        val labels = label(working)
        return ProcessedGarment(
            originalFile = originalFile,
            cutoutFile = cutoutFile,
            colors = colors,
            preset = CategoryGuesser.guess(labels),
            labels = labels.map { it.first },
        )
    }

    private suspend fun cutOut(bitmap: Bitmap): Bitmap? {
        val foreground = withTimeoutOrNull(20_000) {
            runCatching { segmenter.process(InputImage.fromBitmap(bitmap, 0)).await().foregroundBitmap }
                .onFailure { Log.w(TAG, "Segmentation failed", it) }
                .getOrNull()
        } ?: return null
        return foreground.cropToContent(paddingFraction = 0.04f)
    }

    private suspend fun label(bitmap: Bitmap): List<Pair<String, Float>> = withTimeoutOrNull(10_000) {
        runCatching {
            labeler.process(InputImage.fromBitmap(bitmap.scaledToMax(640), 0)).await()
                .map { it.text to it.confidence }
        }.onFailure { Log.w(TAG, "Labelling failed", it) }.getOrNull()
    }.orEmpty()

    private fun extractColors(bitmap: Bitmap): List<FashionColor> {
        val small = bitmap.scaledToMax(128)
        val pixels = IntArray(small.width * small.height)
        small.getPixels(pixels, 0, small.width, 0, 0, small.width, small.height)
        return runCatching { ColorTools.dominantColors(pixels, maxColors = 3).map { it.first } }.getOrDefault(emptyList())
    }

    companion object {
        private const val TAG = "GarmentProcessor"
    }
}

internal fun Bitmap.scaledToMax(maxSide: Int): Bitmap {
    val longest = max(width, height)
    if (longest <= maxSide) return this
    val ratio = maxSide.toFloat() / longest
    return Bitmap.createScaledBitmap(this, (width * ratio).roundToInt().coerceAtLeast(1), (height * ratio).roundToInt().coerceAtLeast(1), true)
}

internal fun Bitmap.centerCrop(fraction: Float): Bitmap {
    val w = (width * fraction).roundToInt().coerceAtLeast(1)
    val h = (height * fraction).roundToInt().coerceAtLeast(1)
    return Bitmap.createBitmap(this, (width - w) / 2, (height - h) / 2, w, h)
}

/** Crop away fully transparent borders; null when there is (almost) nothing left. */
internal fun Bitmap.cropToContent(paddingFraction: Float): Bitmap? {
    val pixels = IntArray(width * height)
    getPixels(pixels, 0, width, 0, 0, width, height)
    var minX = width; var minY = height; var maxX = -1; var maxY = -1; var opaque = 0
    for (y in 0 until height) {
        val row = y * width
        for (x in 0 until width) {
            if (Color.alpha(pixels[row + x]) > 40) {
                opaque++
                if (x < minX) minX = x
                if (x > maxX) maxX = x
                if (y < minY) minY = y
                if (y > maxY) maxY = y
            }
        }
    }
    if (maxX < 0 || opaque < width * height * 0.02) return null
    val pad = (max(maxX - minX, maxY - minY) * paddingFraction).roundToInt()
    val left = max(0, minX - pad)
    val top = max(0, minY - pad)
    val right = min(width - 1, maxX + pad)
    val bottom = min(height - 1, maxY + pad)
    return Bitmap.createBitmap(this, left, top, right - left + 1, bottom - top + 1)
}
