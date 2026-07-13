package tsuki.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tsuki.model.*
import tsuki.model.ContentType.ANIME
import tsuki.model.ContentType.MANHUA
import tsuki.model.Demographic.SEINEN
import tsuki.model.search.MediaSearchQuery
import tsuki.model.search.QueryCriteria.*
import tsuki.model.search.SearchableField.*
import java.util.*

class ListFilterToSearchQueryConverterTest {

    @Test
    fun convertToMediaSearchQueryTest() {
        val tags = setOf(buildMediaTag("tag1"), buildMediaTag("tag2"))
        val excludedTags = setOf(buildMediaTag("exclude_tag"))
        val states = setOf(MediaState.ONGOING)
        val contentRatings = setOf(ContentRating.SAFE)
        val contentTypes = setOf(ANIME, MOVIE)
        val demographics = setOf(SEINEN)

        val filter = MediaListFilter(
            query = "title_name",
            tags = tags,
            tagsExclude = excludedTags,
            locale = Locale.ENGLISH,
            originalLocale = Locale.JAPANESE,
            states = states,
            contentRating = contentRatings,
            types = contentTypes,
            demographics = demographics,
            year = 2020,
            yearFrom = 1997,
            yearTo = 2024,
        )

        val searchQuery = convertToMediaSearchQuery(0, SortOrder.NEWEST, filter)

        val expectedQuery = MediaSearchQuery.Builder()
            .offset(0)
            .order(SortOrder.NEWEST)
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

        assertEquals(expectedQuery, searchQuery)
    }

    @Test
    fun convertToMediaSearchQueryWithEmptyFieldsTest() {
        val filter = MediaListFilter()

        val searchQuery = convertToMediaSearchQuery(0, SortOrder.NEWEST, filter)

        assertEquals(MediaSearchQuery.Builder().offset(0).order(SortOrder.NEWEST).build(), searchQuery)
    }

    private fun buildMediaTag(name: String): MediaTag {
        return MediaTag(
            key = "${name}Key",
            title = name,
            source = MediaParserSource.MANGADEX,
        )
    }
}
