package com.kintukka.hanger.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.io.File
import java.util.UUID
import kotlin.math.max
import kotlin.math.roundToInt

/** Owns every image file the app stores. All files live in app-private storage. */
class ImageStore(private val context: Context) {
    val dir: File = File(context.filesDir, "images").apply { mkdirs() }

    fun file(name: String?): File? = name?.let { File(dir, it) }?.takeIf { it.exists() }

    fun delete(name: String?) {
        name?.let { File(dir, it).delete() }
    }

    /** Decode a picked/captured image, applying EXIF rotation, downscaled so its longest side is ≤ [maxSide]. */
    fun decode(uri: Uri, maxSide: Int): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        return ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = false
            val longest = max(info.size.width, info.size.height)
            if (longest > maxSide) {
                val ratio = maxSide.toFloat() / longest
                decoder.setTargetSize((info.size.width * ratio).roundToInt(), (info.size.height * ratio).roundToInt())
            }
        }
    }

    fun decodeFile(name: String, maxSide: Int): Bitmap? {
        val f = file(name) ?: return null
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(f.absolutePath, bounds)
        var sample = 1
        while (max(bounds.outWidth, bounds.outHeight) / (sample * 2) >= maxSide) sample *= 2
        return BitmapFactory.decodeFile(f.absolutePath, BitmapFactory.Options().apply { inSampleSize = sample })
    }

    fun savePhoto(bitmap: Bitmap): String {
        val name = "${UUID.randomUUID()}.jpg"
        File(dir, name).outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 88, it) }
        return name
    }

    /** Saves a bitmap that has transparency (a cut-out). */
    fun saveCutout(bitmap: Bitmap): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val name = "${UUID.randomUUID()}.webp"
            File(dir, name).outputStream().use { bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 90, it) }
            name
        } else {
            val name = "${UUID.randomUUID()}.png"
            File(dir, name).outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            name
        }
    }

    /** Copy an existing file into the store under a fresh name (used by restore). */
    fun importFile(source: File, name: String): String {
        source.copyTo(File(dir, name), overwrite = true)
        return name
    }

    fun allFiles(): List<File> = dir.listFiles()?.toList().orEmpty()
}
