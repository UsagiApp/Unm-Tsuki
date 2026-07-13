package tsuki.util

import kotlinx.coroutines.*
import tsuki.MediaParser
import tsuki.model.Media
import tsuki.model.MediaListFilter
import tsuki.model.SortOrder

public class RelatedMediaFinder(
    private val parsers: Collection<MediaParser>,
) {

    public suspend operator fun invoke(seed: Media): List<Media> = withContext(Dispatchers.Default) {
        coroutineScope {
            parsers.singleOrNull()?.let { parser ->
                findRelatedImpl(this, parser, seed)
            } ?: parsers.map { parser ->
                async {
                    findRelatedImpl(this, parser, seed)
                }
            }.awaitAll().flatten()
        }
    }

    private suspend fun findRelatedImpl(scope: CoroutineScope, parser: MediaParser, seed: Media): List<Media> {
        val words = HashSet<String>()
        words += seed.title.splitByWhitespace()
        seed.altTitles.forEach {
            words += it.splitByWhitespace()
        }
        if (words.isEmpty()) {
            return emptyList()
        }
        val results = words.map { keyword ->
            scope.async {
                val result = parser.getList(
                    0,
                    if (SortOrder.RELEVANCE in parser.availableSortOrders) {
                        SortOrder.RELEVANCE
                    } else {
                        parser.availableSortOrders.first()
                    },
                    MediaListFilter(
                        query = keyword,
                    ),
                )
                result.filter { it.id != seed.id && it.containKeyword(keyword) }
            }
        }.awaitAll()
        return results.minBy { if (it.isEmpty()) Int.MAX_VALUE else it.size }
    }

    private fun Media.containKeyword(keyword: String): Boolean {
        return title.contains(keyword, ignoreCase = true)
            || altTitles.any { it.contains(keyword, ignoreCase = true) }
    }
}
