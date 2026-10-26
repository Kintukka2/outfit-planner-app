package com.kintukka.hanger.demo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import com.kintukka.hanger.engine.Category
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.Pattern

/**
 * Draws simple flat-lay garment illustrations with transparent backgrounds.
 * Used for the demo closet and for items added without a photo.
 */
object DemoArt {

    fun render(
        category: Category,
        subcategory: String,
        colors: List<FashionColor>,
        pattern: Pattern,
        size: Int = 600,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val s = size.toFloat()
        val main = colors.firstOrNull()?.rgb?.let { it or 0xFF000000.toInt() } ?: Color.LTGRAY
        val accent = colors.getOrNull(1)?.rgb?.let { it or 0xFF000000.toInt() } ?: shade(main, 0.75f)
        val sub = subcategory.lowercase()

        val path = when (category) {
            Category.TOP -> top(s, longSleeve = "long" in sub || "sweatshirt" in sub || "hoodie" in sub || "shirt" == sub || "blouse" in sub, tank = "tank" in sub)
            Category.LAYER -> if ("blazer" in sub || "vest" in sub || "gilet" in sub) coat(s, long = false) else top(s, longSleeve = true, tank = false)
            Category.OUTERWEAR -> coat(s, long = "coat" in sub || "trench" in sub || "puffer" in sub || "rain" in sub)
            Category.BOTTOM -> when {
                "skirt" in sub -> skirt(s)
                "short" in sub -> trousers(s, legLength = 0.52f)
                else -> trousers(s, legLength = 0.94f)
            }
            Category.ONE_PIECE -> dress(s)
            Category.SHOES -> shoe(s, boot = "boot" in sub)
            Category.BAG -> bag(s)
            Category.ACCESSORY, Category.OTHER -> accessory(s, sub)
        }

        val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = main; style = Paint.Style.FILL }
        canvas.drawPath(path, fill)

        canvas.save()
        canvas.clipPath(path)
        drawPattern(canvas, s, pattern, accent, main)
        // Soft light from the top left, shadow towards the bottom.
        val light = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, 0f, s, s, Color.argb(46, 255, 255, 255), Color.argb(40, 0, 0, 0), Shader.TileMode.CLAMP)
        }
        canvas.drawRect(0f, 0f, s, s, light)
        canvas.restore()

        val outline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = shade(main, 0.62f)
            style = Paint.Style.STROKE
            strokeWidth = s * 0.008f
            strokeJoin = Paint.Join.ROUND
        }
        canvas.drawPath(path, outline)
        drawDetails(canvas, s, category, sub, shade(main, 0.6f))
        return bitmap
    }

    private fun shade(color: Int, factor: Float): Int {
        val lum = (Color.red(color) * 0.3f + Color.green(color) * 0.59f + Color.blue(color) * 0.11f) / 255f
        // Very dark colours get a lighter outline so edges stay visible.
        val f = if (lum < 0.2f) 1.8f else factor
        return Color.rgb(
            (Color.red(color) * f).toInt().coerceIn(0, 255).coerceAtLeast(if (lum < 0.2f) 60 else 0),
            (Color.green(color) * f).toInt().coerceIn(0, 255).coerceAtLeast(if (lum < 0.2f) 60 else 0),
            (Color.blue(color) * f).toInt().coerceIn(0, 255).coerceAtLeast(if (lum < 0.2f) 60 else 0),
        )
    }

    private fun drawPattern(canvas: Canvas, s: Float, pattern: Pattern, accent: Int, main: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = accent }
        when (pattern) {
            Pattern.STRIPE -> {
                var y = 0f
                while (y < s) { canvas.drawRect(0f, y, s, y + s * 0.035f, p); y += s * 0.08f }
            }
            Pattern.CHECK -> {
                p.alpha = 120
                var v = 0f
                while (v < s) {
                    canvas.drawRect(v, 0f, v + s * 0.03f, s, p)
                    canvas.drawRect(0f, v, s, v + s * 0.03f, p)
                    v += s * 0.09f
                }
            }
            Pattern.DOT -> {
                var y = s * 0.04f
                var row = 0
                while (y < s) {
                    var x = if (row % 2 == 0) s * 0.04f else s * 0.08f
                    while (x < s) { canvas.drawCircle(x, y, s * 0.014f, p); x += s * 0.08f }
                    y += s * 0.07f; row++
                }
            }
            Pattern.FLORAL, Pattern.GRAPHIC, Pattern.ANIMAL, Pattern.OTHER -> {
                p.alpha = 170
                var y = s * 0.08f
                var row = 0
                while (y < s) {
                    var x = if (row % 2 == 0) s * 0.08f else s * 0.16f
                    while (x < s) {
                        for (k in 0 until 5) {
                            val a = Math.toRadians(k * 72.0)
                            canvas.drawCircle(x + (Math.cos(a) * s * 0.018).toFloat(), y + (Math.sin(a) * s * 0.018).toFloat(), s * 0.013f, p)
                        }
                        x += s * 0.16f
                    }
                    y += s * 0.13f; row++
                }
            }
            Pattern.TEXTURE -> {
                p.color = shade(main, 0.85f); p.alpha = 90
                var y = 0f
                while (y < s) { canvas.drawRect(0f, y, s, y + s * 0.006f, p); y += s * 0.022f }
            }
            Pattern.SOLID -> Unit
        }
    }

    private fun drawDetails(canvas: Canvas, s: Float, category: Category, sub: String, color: Int) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; style = Paint.Style.STROKE; strokeWidth = s * 0.006f }
        when {
            category == Category.TOP && ("shirt" == sub || "blouse" in sub) -> {
                canvas.drawLine(s * 0.5f, s * 0.2f, s * 0.5f, s * 0.86f, p)
                var y = s * 0.3f
                val dot = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }
                while (y < s * 0.85f) { canvas.drawCircle(s * 0.53f, y, s * 0.008f, dot); y += s * 0.1f }
            }
            category == Category.BOTTOM && "jean" in sub -> {
                canvas.drawLine(s * 0.3f, s * 0.16f, s * 0.7f, s * 0.16f, p)
                canvas.drawArc(RectF(s * 0.3f, s * 0.1f, s * 0.42f, s * 0.26f), 0f, 90f, false, p)
                canvas.drawArc(RectF(s * 0.58f, s * 0.1f, s * 0.7f, s * 0.26f), 90f, 90f, false, p)
            }
            category == Category.OUTERWEAR || (category == Category.LAYER && ("blazer" in sub || "cardigan" in sub)) -> {
                canvas.drawLine(s * 0.5f, s * 0.2f, s * 0.5f, s * 0.9f, p)
                canvas.drawLine(s * 0.42f, s * 0.12f, s * 0.5f, s * 0.36f, p)
                canvas.drawLine(s * 0.58f, s * 0.12f, s * 0.5f, s * 0.36f, p)
            }
            category == Category.LAYER || "sweat" in sub || "hoodie" in sub -> {
                canvas.drawLine(s * 0.22f, s * 0.84f, s * 0.78f, s * 0.84f, p)
            }
            category == Category.SHOES -> {
                canvas.drawLine(s * 0.1f, s * 0.68f, s * 0.92f, s * 0.68f, p)
            }
        }
    }

    private fun top(s: Float, longSleeve: Boolean, tank: Boolean): Path = Path().apply {
        if (tank) {
            moveTo(s * 0.36f, s * 0.08f); lineTo(s * 0.42f, s * 0.08f)
            quadTo(s * 0.5f, s * 0.22f, s * 0.58f, s * 0.08f); lineTo(s * 0.64f, s * 0.08f)
            quadTo(s * 0.68f, s * 0.3f, s * 0.76f, s * 0.36f); lineTo(s * 0.76f, s * 0.9f)
            lineTo(s * 0.24f, s * 0.9f); lineTo(s * 0.24f, s * 0.36f)
            quadTo(s * 0.32f, s * 0.3f, s * 0.36f, s * 0.08f); close()
            return@apply
        }
        moveTo(s * 0.40f, s * 0.10f)
        quadTo(s * 0.5f, s * 0.2f, s * 0.60f, s * 0.10f)
        lineTo(s * 0.78f, s * 0.16f)
        if (longSleeve) {
            lineTo(s * 0.96f, s * 0.78f); lineTo(s * 0.86f, s * 0.81f); lineTo(s * 0.76f, s * 0.42f)
        } else {
            lineTo(s * 0.95f, s * 0.36f); lineTo(s * 0.82f, s * 0.45f); lineTo(s * 0.76f, s * 0.38f)
        }
        lineTo(s * 0.76f, s * 0.9f); lineTo(s * 0.24f, s * 0.9f); lineTo(s * 0.24f, if (longSleeve) s * 0.42f else s * 0.38f)
        if (longSleeve) {
            lineTo(s * 0.14f, s * 0.81f); lineTo(s * 0.04f, s * 0.78f)
        } else {
            lineTo(s * 0.18f, s * 0.45f); lineTo(s * 0.05f, s * 0.36f)
        }
        lineTo(s * 0.22f, s * 0.16f); close()
    }

    private fun coat(s: Float, long: Boolean): Path = Path().apply {
        val hem = if (long) 0.97f else 0.84f
        moveTo(s * 0.40f, s * 0.08f); lineTo(s * 0.60f, s * 0.08f)
        lineTo(s * 0.80f, s * 0.14f); lineTo(s * 0.97f, s * 0.80f); lineTo(s * 0.87f, s * 0.83f)
        lineTo(s * 0.77f, s * 0.44f); lineTo(s * 0.78f, s * hem); lineTo(s * 0.22f, s * hem)
        lineTo(s * 0.23f, s * 0.44f); lineTo(s * 0.13f, s * 0.83f); lineTo(s * 0.03f, s * 0.80f)
        lineTo(s * 0.20f, s * 0.14f); close()
    }

    private fun trousers(s: Float, legLength: Float): Path = Path().apply {
        moveTo(s * 0.28f, s * 0.06f); lineTo(s * 0.72f, s * 0.06f)
        lineTo(s * 0.78f, s * legLength); lineTo(s * 0.55f, s * legLength)
        lineTo(s * 0.5f, s * 0.34f); lineTo(s * 0.45f, s * legLength)
        lineTo(s * 0.22f, s * legLength); close()
    }

    private fun skirt(s: Float): Path = Path().apply {
        moveTo(s * 0.33f, s * 0.12f); lineTo(s * 0.67f, s * 0.12f)
        lineTo(s * 0.84f, s * 0.86f); quadTo(s * 0.5f, s * 0.9f, s * 0.16f, s * 0.86f); close()
    }

    private fun dress(s: Float): Path = Path().apply {
        moveTo(s * 0.38f, s * 0.04f); lineTo(s * 0.42f, s * 0.04f); quadTo(s * 0.5f, s * 0.14f, s * 0.58f, s * 0.04f)
        lineTo(s * 0.62f, s * 0.04f); lineTo(s * 0.65f, s * 0.38f); lineTo(s * 0.86f, s * 0.96f)
        quadTo(s * 0.5f, s * 0.99f, s * 0.14f, s * 0.96f); lineTo(s * 0.35f, s * 0.38f); close()
    }

    private fun shoe(s: Float, boot: Boolean): Path = Path().apply {
        if (boot) {
            moveTo(s * 0.16f, s * 0.16f); lineTo(s * 0.44f, s * 0.16f); lineTo(s * 0.46f, s * 0.48f)
            quadTo(s * 0.6f, s * 0.5f, s * 0.86f, s * 0.6f); quadTo(s * 0.95f, s * 0.64f, s * 0.92f, s * 0.74f)
            lineTo(s * 0.1f, s * 0.74f); lineTo(s * 0.12f, s * 0.5f); close()
        } else {
            moveTo(s * 0.10f, s * 0.42f); quadTo(s * 0.2f, s * 0.36f, s * 0.38f, s * 0.38f)
            quadTo(s * 0.5f, s * 0.5f, s * 0.78f, s * 0.54f); quadTo(s * 0.95f, s * 0.58f, s * 0.93f, s * 0.74f)
            lineTo(s * 0.08f, s * 0.74f); close()
        }
    }

    private fun bag(s: Float): Path = Path().apply {
        fillType = Path.FillType.EVEN_ODD
        moveTo(s * 0.18f, s * 0.38f); lineTo(s * 0.82f, s * 0.38f); lineTo(s * 0.88f, s * 0.9f); lineTo(s * 0.12f, s * 0.9f); close()
        // Handle (a thin arc band).
        addArc(RectF(s * 0.32f, s * 0.12f, s * 0.68f, s * 0.6f), 180f, 180f)
        addArc(RectF(s * 0.36f, s * 0.17f, s * 0.64f, s * 0.6f), 0f, -180f)
    }

    private fun accessory(s: Float, sub: String): Path = Path().apply {
        when {
            "scarf" in sub -> {
                moveTo(s * 0.3f, s * 0.06f); lineTo(s * 0.7f, s * 0.06f); lineTo(s * 0.62f, s * 0.95f)
                lineTo(s * 0.46f, s * 0.95f); lineTo(s * 0.5f, s * 0.3f); lineTo(s * 0.36f, s * 0.9f)
                lineTo(s * 0.22f, s * 0.9f); close()
            }
            "beanie" in sub || "hat" in sub || "cap" in sub -> {
                moveTo(s * 0.14f, s * 0.7f); quadTo(s * 0.14f, s * 0.2f, s * 0.5f, s * 0.2f)
                quadTo(s * 0.86f, s * 0.2f, s * 0.86f, s * 0.7f); lineTo(s * 0.86f, s * 0.8f); lineTo(s * 0.14f, s * 0.8f); close()
            }
            "sunglass" in sub -> {
                addOval(RectF(s * 0.08f, s * 0.36f, s * 0.46f, s * 0.66f), Path.Direction.CW)
                addOval(RectF(s * 0.54f, s * 0.36f, s * 0.92f, s * 0.66f), Path.Direction.CW)
                addRect(RectF(s * 0.44f, s * 0.42f, s * 0.56f, s * 0.46f), Path.Direction.CW)
            }
            "belt" in sub -> {
                addRoundRect(RectF(s * 0.04f, s * 0.44f, s * 0.96f, s * 0.56f), s * 0.02f, s * 0.02f, Path.Direction.CW)
                addRect(RectF(s * 0.66f, s * 0.4f, s * 0.78f, s * 0.6f), Path.Direction.CW)
            }
            "necklace" in sub || "bracelet" in sub || "ring" in sub || "earring" in sub || "watch" in sub -> {
                addOval(RectF(s * 0.2f, s * 0.15f, s * 0.8f, s * 0.75f), Path.Direction.CW)
                addOval(RectF(s * 0.25f, s * 0.2f, s * 0.75f, s * 0.7f), Path.Direction.CCW)
                addCircle(s * 0.5f, s * 0.8f, s * 0.07f, Path.Direction.CW)
            }
            else -> addCircle(s * 0.5f, s * 0.5f, s * 0.32f, Path.Direction.CW)
        }
    }
}
