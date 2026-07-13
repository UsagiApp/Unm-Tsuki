@file:Suppress("DEPRECATION")

package tsuki.core

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import tsuki.MediaParser
import tsuki.MediaParserAuthProvider
import tsuki.config.ConfigKey
import tsuki.config.MediaSourceConfig
import tsuki.model.*
import tsuki.model.search.MediaSearchQuery
import tsuki.model.search.MediaSearchQueryCapabilities
import tsuki.util.LinkResolver

public class MediaParserWrapper(
	private val delegate: MediaParser,
) : MediaParser, MediaParserAuthProvider by (delegate.authorizationProvider ?: EmptyAuthProvider) {

	override val source: MediaSource get() = delegate.source

	override val availableSortOrders: Set<SortOrder> get() = delegate.availableSortOrders

	@Deprecated("Too complex. Use filterCapabilities instead")
	override val searchQueryCapabilities: MediaSearchQueryCapabilities get() = delegate.searchQueryCapabilities

	override val filterCapabilities: MediaListFilterCapabilities get() = delegate.filterCapabilities

	override val config: MediaSourceConfig get() = delegate.config

	override val configKeyDomain: ConfigKey.Domain get() = delegate.configKeyDomain

	override val domain: String get() = delegate.domain

	@Deprecated("Too complex. Use getList with filter instead")
	override suspend fun getList(query: MediaSearchQuery): List<Media> = delegate.getList(query)

	override suspend fun getList(offset: Int, order: SortOrder, filter: MediaListFilter): List<Media> =
		delegate.getList(offset, order, filter)

	override suspend fun getDetails(media: Media): Media = delegate.getDetails(media)

	override suspend fun getVideoSources(episode: Episode): List<VideoSource> = delegate.getVideoSources(episode)

	override suspend fun getVideoUrl(page: VideoSource): String = delegate.getVideoUrl(page)

	override suspend fun getFilterOptions(): MediaListFilterOptions = delegate.getFilterOptions()

	override suspend fun getFavicons(): Favicons = delegate.getFavicons()

	override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>): Unit = delegate.onCreateConfig(keys)

	override suspend fun getRelatedMedia(seed: Media): List<Media> = delegate.getRelatedMedia(seed)

	override fun getRequestHeaders(): Headers = delegate.getRequestHeaders()

	override suspend fun resolveLink(link: HttpUrl): Media? = delegate.resolveLink(link)

	@Deprecated("Use resolveLink(HttpUrl) instead")
    override suspend fun resolveLink(resolver: LinkResolver, link: HttpUrl): Media? = delegate.resolveLink(resolver, link)

	override fun intercept(chain: Interceptor.Chain): Response = delegate.intercept(chain)

	private object EmptyAuthProvider : MediaParserAuthProvider {
		override val authUrl: String = ""
		override suspend fun isAuthorized(): Boolean = true
	}
}
