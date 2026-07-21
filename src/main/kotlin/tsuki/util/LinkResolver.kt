package tsuki.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import tsuki.MediaLoaderContext
import tsuki.MediaParser
import tsuki.model.*

public class LinkResolver(
	public val link: HttpUrl,
	public val context: MediaLoaderContext? = null,
	public val parser: MediaParser? = null,
) {

	public suspend fun getSource(): MediaSource? = parser?.source

	public suspend fun getMedia(): Media? {
		val p = parser ?: return null // TODO: implement global link resolution via context
		return p.resolveLink(link) ?: resolveMedia(p)
	}

	public suspend fun resolveMedia(
		parser: MediaParser,
		url: String = link.toString().toRelativeUrl(link.host),
		id: Long = generateUid(parser.source, url),
		title: String = STUB_TITLE,
	): Media? = resolveBySeed(
		parser,
		Media(
			id = id,
			title = title,
			altTitles = emptySet(),
			url = url,
			publicUrl = link.toString(),
			rating = RATING_UNKNOWN,
			contentRating = null,
			coverUrl = "",
			tags = emptySet(),
			state = null,
			authors = emptySet(),
			largeCoverUrl = null,
			description = null,
			chapters = null,
			source = parser.source,
		),
	)

	private suspend fun resolveBySeed(parser: MediaParser, s: Media): Media? {
		val seed = parser.getDetails(s)
		if (!parser.filterCapabilities.isSearchSupported) {
			return seed.takeUnless { it.chapters.isNullOrEmpty() }
		}
		val query = when {
			seed.title != STUB_TITLE && seed.title.isNotEmpty() -> seed.title
			seed.altTitles.isNotEmpty() -> seed.altTitles.first()
			seed.authors.isNotEmpty() -> seed.authors.first()
			else -> return seed // unfortunately we do not know a real media title so unable to find it
		}
		val resolved = runCatching {
			val list = parser.getList(0, parser.bestSortOrder(), MediaListFilter(query = query))
			list.singleOrNull { media -> isSameUrl(media.publicUrl) }
		}.getOrNull()
		if (resolved == null) {
			return seed
		}
		return runCatching {
			parser.getDetails(resolved)
		}.getOrElse {
			resolved.copy(
				chapters = seed.chapters ?: resolved.chapters,
				description = seed.description ?: resolved.description,
				authors = seed.authors.ifEmpty { resolved.authors },
				tags = seed.tags + resolved.tags,
				state = seed.state ?: resolved.state,
				coverUrl = seed.coverUrl ?: resolved.coverUrl,
				largeCoverUrl = seed.largeCoverUrl ?: resolved.largeCoverUrl,
				altTitles = seed.altTitles + resolved.altTitles,
			)
		}
	}

	private fun isSameUrl(publicUrl: String): Boolean {
		if (publicUrl == link.toString()) {
			return true
		}
		val httpUrl = publicUrl.toHttpUrlOrNull() ?: return false
		return link.host == httpUrl.host
			&& link.encodedPath == httpUrl.encodedPath
	}

	private fun MediaParser.bestSortOrder(): SortOrder {
		val supported = availableSortOrders
		if (SortOrder.RELEVANCE in supported) {
			return SortOrder.RELEVANCE
		}
		return SortOrder.values().first { it in supported }
	}

	private companion object {
		const val STUB_TITLE = "Unknown media"
	}
}
