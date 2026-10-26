package com.kintukka.hanger.data.db

import androidx.room.TypeConverter
import com.kintukka.hanger.engine.FashionColor
import com.kintukka.hanger.engine.Occasion
import com.kintukka.hanger.engine.Season
import java.time.LocalDate

class Converters {
    @TypeConverter fun dateToLong(date: LocalDate?): Long? = date?.toEpochDay()
    @TypeConverter fun longToDate(value: Long?): LocalDate? = value?.let(LocalDate::ofEpochDay)

    @TypeConverter fun colorsToString(colors: List<FashionColor>): String = colors.joinToString(",") { it.name }
    @TypeConverter fun stringToColors(value: String): List<FashionColor> = value.splitNames { FashionColor.valueOf(it) }

    @TypeConverter fun seasonsToString(seasons: Set<Season>): String = seasons.sorted().joinToString(",") { it.name }
    @TypeConverter fun stringToSeasons(value: String): Set<Season> = value.splitNames { Season.valueOf(it) }.toSet()

    @TypeConverter fun occasionsToString(occasions: Set<Occasion>): String = occasions.sorted().joinToString(",") { it.name }
    @TypeConverter fun stringToOccasions(value: String): Set<Occasion> = value.splitNames { Occasion.valueOf(it) }.toSet()

    @TypeConverter fun tagsToString(tags: List<String>): String = tags.joinToString(SEPARATOR)
    @TypeConverter fun stringToTags(value: String): List<String> =
        if (value.isEmpty()) emptyList() else value.split(SEPARATOR)

    private inline fun <T> String.splitNames(parse: (String) -> T): List<T> =
        if (isEmpty()) emptyList() else split(",").mapNotNull { runCatching { parse(it) }.getOrNull() }

    private companion object {
        const val SEPARATOR = "\u001F"
    }
}
