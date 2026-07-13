package tsuki.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tsuki.model.ContentRating
import tsuki.model.ContentType.ANIME
import tsuki.model.ContentType.MANHUA
import tsuki.model.Demographic.SEINEN
import tsuki.model.MediaParserSource
import tsuki.model.MediaState
import tsuki.model.MediaTag
import tsuki.model.search.MediaSearchQuery
import tsuki.model.search.QueryCriteria.*
import tsuki.model.search.SearchableField.*
import java.util.*

class ConvertToMediaListFilterTest {

    @Test
    fun convertToMediaListFilterTest() {
        val tags = setOf(buildMediaTag("tag1"), buildMediaTag("tag2"))
        val excludedTags = setOf(buildMediaTag("exclude_tag"))
        val states = setOf(MediaState.ONGOING)
        val contentRatings = setOf(ContentRating.SAFE)
        val contentTypes = setOf(ANIME, MOVIE)
        val demographics = setOf(SEINEN)

        val query = MediaSearchQuery.Builder()
            .criterion(Match(TITLE_NAME, "title_name"))
            .criterion(Include(TAG, tags))
            .criterion(Exclude(TAG, excludedTags))
            .criterion(Include(LANGUAGE, setOf(Locale.ENGLISH)))
            .criterion(Include(ORIGINAL_LANGUAGE, setOf(Locale.JAPANESE)))
            .criterion(Include(STATE, states))
            .criterion(Include(CONTENT_RATING, contentRatings))
            .criterion(Include(CONTENT_TYPE, contentTypes))
            .criterion(Include(DEMOGRAPHIC, demographics))
            .criterion(Range(PUBLICATION_YEAR, 1997, 2024))
            .criterion(Match(PUBLICATION_YEAR, 2020))
            .build()

        val listFilter = convertToMediaListFilter(query)

        assertEquals(listFilter.query, "title_name")
        assertEquals(listFilter.tags, tags)
        assertEquals(listFilter.tagsExclude, excludedTags)
        assertEquals(listFilter.locale, Locale.ENGLISH)
        assertEquals(listFilter.originalLocale, Locale.JAPANESE)
        assertEquals(listFilter.states, states)
        assertEquals(listFilter.contentRating, contentRatings)
        assertEquals(listFilter.types, contentTypes)
        assertEquals(listFilter.demographics, demographics)
        assertEquals(listFilter.year, 2020)
        assertEquals(listFilter.yearFrom, 1997)
        assertEquals(listFilter.yearTo, 2024)
    }

    @Test
    fun convertToMediaListFilterWithMultipleTagsIncludeTest() {
        val tags1 = setOf(buildMediaTag("tag1"), buildMediaTag("tag2"))
        val tags2 = setOf(buildMediaTag("tag3"), buildMediaTag("tag4"))

        val query = MediaSearchQuery.Builder()
            .criterion(Include(TAG, tags1))
            .criterion(Include(TAG, tags2))
            .build()

        val listFilter = convertToMediaListFilter(query)

        assertEquals(listFilter.tags, tags1 union tags2)
    }

    @Test
    fun convertToMediaListFilterWithUnsupportedFieldTest() {
        val query = MediaSearchQuery.Builder()
            .criterion(Include(AUTHOR, setOf(buildMediaTag("author"))))
            .build()

        val exception = assertThrows<IllegalArgumentException> {
            convertToMediaListFilter(query)
        }

        assert(exception.message!!.contains("Unsupported field for Include criterion: AUTHOR"))
    }

    private fun buildMediaTag(name: String): MediaTag {
        return MediaTag(
            key = "${name}Key",
            title = name,
            source = MediaParserSource.MANGADEX,
        )
    }
}
