@file:Suppress("DEPRECATION")

package tsuki

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import tsuki.config.ConfigKey
import tsuki.config.MediaSourceConfig
import tsuki.model.*
import tsuki.model.search.MediaSearchQuery
import tsuki.model.search.MediaSearchQueryCapabilities
import java.util.*

public interface MediaParser : Interceptor {

	public val source: MediaSource

	/**
	 * Supported [SortOrder] variants. Must not be empty.
	 *
	 * For better performance use [EnumSet] for more than one item.
	 */
	public val availableSortOrders: Set<SortOrder>

	@Deprecated("Too complex. Use filterCapabilities instead")
	public val searchQueryCapabilities: MediaSearchQueryCapabilities

	public val filterCapabilities: MediaListFilterCapabilities

	public val config: MediaSourceConfig

	public val authorizationProvider: MediaParserAuthProvider?
		get() = this as? MediaParserAuthProvider

	/**
	 * Provide default domain and available alternatives, if any.
	 *
	 * Never hardcode domain in requests, use [domain] instead.
	 */
	public val configKeyDomain: ConfigKey.Domain

	public val domain: String

	@Deprecated("Too complex. Use getList with filter instead")
	public suspend fun getList(query: MediaSearchQuery): List<Media>

	public suspend fun getList(offset: Int, order: SortOrder, filter: MediaListFilter): List<Media>

	/**
	 * Parse details for [Media]: episodes list, description, large cover, etc.
	 * Must return the same media, may change any fields excepts id, url and source
	 * @see Media.copy
	 */
	public suspend fun getDetails(media: Media): Media

	/**
	 * Parse video sources list for specified episode.
	 * @see VideoSource for details
	 */
	public suspend fun getVideoSources(episode: Episode): List<VideoSource>

	/**
	 * Fetch direct link to the video source.
	 */
	public suspend fun getVideoUrl(source: VideoSource): String

	public suspend fun getFilterOptions(): MediaListFilterOptions

	/**
	 * Parse favicons from the main page of the source`s website
	 */
	public suspend fun getFavicons(): Favicons

	public fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>)

	public suspend fun getRelatedMedia(seed: Media): List<Media>

	public fun getRequestHeaders(): Headers

	/**
	 * Return [Media] object by web link to it
	 * @see [Media.publicUrl]
	 */
	public suspend fun resolveLink(link: HttpUrl): Media? {
		return resolveLink(tsuki.util.LinkResolver(link, parser = this), link)
	}

	@Deprecated("Use resolveLink(HttpUrl) instead")
	public suspend fun resolveLink(resolver: tsuki.util.LinkResolver, link: HttpUrl): Media? = null

	/**
	 * Backward-compatible overload for resolveLink with String
	 */
	public suspend fun resolveLink(link: String): Media? {
		val url = link.toHttpUrlOrNull() ?: return null
		return resolveLink(url)
	}
}
