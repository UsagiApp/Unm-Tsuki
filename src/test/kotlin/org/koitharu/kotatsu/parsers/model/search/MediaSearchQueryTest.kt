package tsuki.model.search

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import tsuki.model.MediaParserSource
import tsuki.model.MediaState
import tsuki.model.MediaTag
import tsuki.model.search.QueryCriteria.*
import tsuki.model.search.SearchableField.*
import java.util.*

class MediaSearchQueryCapabilitiesTest {

    private val capabilities = MediaSearchQueryCapabilities(
        capabilities = setOf(
            SearchCapability(TITLE_NAME, setOf(Match::class), isMultiple = false, isExclusive = true),
            SearchCapability(TAG, setOf(Include::class, Exclude::class), isMultiple = true, isExclusive = false),
            SearchCapability(PUBLICATION_YEAR, setOf(Range::class), isMultiple = false, isExclusive = false),
            SearchCapability(STATE, setOf(Include::class), isMultiple = false, isExclusive = false),
        ),
    )

    @Test
    fun validateValidSingleCriterionQuery() {
        val query = MediaSearchQuery.Builder()
            .criterion(Match(TITLE_NAME, "title"))
            .build()

        assertDoesNotThrow { capabilities.validate(query) }
    }

    @Test
    fun validateUnsupportedFieldThrowsException() {
        val query = MediaSearchQuery.Builder()
            .criterion(Include(ORIGINAL_LANGUAGE, setOf(Locale.ENGLISH)))
            .build()

        assertThrows(IllegalArgumentException::class.java) { capabilities.validate(query) }
    }

    @Test
    fun validateUnsupportedMultiValueThrowsException() {
        val query = MediaSearchQuery.Builder()
            .criterion(Include(STATE, setOf(MediaState.ONGOING, MediaState.FINISHED)))
            .build()

        assertThrows(IllegalArgumentException::class.java) { capabilities.validate(query) }
    }

    @Test
    fun validateMultipleCriteriaWithOtherCriteriaAllowed() {
        val query = MediaSearchQuery.Builder()
            .criterion(Include(TAG, setOf(buildTag("tag1"), buildTag("tag2"))))
            .criterion(Exclude(TAG, setOf(buildTag("tag3"))))
            .build()

        assertDoesNotThrow { capabilities.validate(query) }
    }

    @Test
    fun validateMultipleCriteriaWithStrictCapabilityThrowsException() {
        val query = MediaSearchQuery.Builder()
            .criterion(Match(TITLE_NAME, "title"))
            .criterion(Range(PUBLICATION_YEAR, 1990, 2000))
            .build()

        assertThrows(IllegalArgumentException::class.java) { capabilities.validate(query) }
    }

    private fun buildTag(name: String) = MediaTag(title = name, key = "${name}Key", source = MediaParserSource.MANGADEX)
}
