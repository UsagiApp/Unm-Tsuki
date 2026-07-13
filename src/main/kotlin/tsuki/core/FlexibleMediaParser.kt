@file:Suppress("DEPRECATION")

package tsuki.core

import androidx.annotation.CallSuper
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import tsuki.InternalParsersApi
import tsuki.MediaLoaderContext
import tsuki.MediaParser
import tsuki.config.ConfigKey
import tsuki.config.MediaSourceConfig
import tsuki.model.*
import tsuki.model.search.MediaSearchQuery
import tsuki.model.search.MediaSearchQueryCapabilities
import tsuki.network.CommonHeaders
import tsuki.network.OkHttpWebClient
import tsuki.network.WebClient
import tsuki.util.*
import java.util.*

@Deprecated("Too complex. Use AbstractMediaParser instead")
public abstract class FlexibleMediaParser @InternalParsersApi constructor(
	@property:InternalParsersApi public val context: MediaLoaderContext,
	public final override val source: MediaSource,
) : MediaParser {

	override val config: MediaSourceConfig by lazy { context.getConfig(source) }

	public open val sourceLocale: Locale
		get() = if (source.locale.isEmpty()) Locale.ROOT else Locale(source.locale)

	protected open val userAgentKey: ConfigKey.UserAgent = ConfigKey.UserAgent(context.getDefaultUserAgent())

	public final override val filterCapabilities: MediaListFilterCapabilities
		get() = searchQueryCapabilities.toMediaListFilterCapabilities()

	protected val sourceContentRating: ContentRating?
		get() = if (source.contentType == ContentType.HENTAI) {
			ContentRating.ADULT
		} else {
			null
		}

	public final override val domain: String
		get() = config[configKeyDomain]

	@Deprecated("Override intercept() instead")
	override fun getRequestHeaders(): Headers = Headers.Builder()
		.add(CommonHeaders.USER_AGENT, config[userAgentKey])
		.build()

	/**
	 * Used as fallback if value of `order` passed to [getList] is null
	 */
	public open val defaultSortOrder: SortOrder
		get() {
			val supported = availableSortOrders
			return SortOrder.entries.first { it in supported }
		}

	protected open val webClient: WebClient = OkHttpWebClient(context.httpClient, source)

	/**
	 * Fetch direct link to the page image.
	 */
	public override suspend fun getVideoUrl(page: VideoSource): String = page.url.toAbsoluteUrl(domain)

	public final override suspend fun getList(offset: Int, order: SortOrder, filter: MediaListFilter): List<Media> {
		return getList(convertToMediaSearchQuery(offset, order, filter))
	}

	/**
	 * Parse favicons from the main page of the source`s website
	 */
	public override suspend fun getFavicons(): Favicons {
		return FaviconParser(webClient, domain).parseFavicons()
	}

	@CallSuper
	public override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
		keys.add(configKeyDomain)
	}

	public override suspend fun getRelatedMedia(seed: Media): List<Media> {
		return RelatedMediaFinder(listOf(this)).invoke(seed)
	}

	/**
	 * Return [Media] object by web link to it
	 * @see [Media.publicUrl]
	 */
	@Deprecated("Use resolveLink(HttpUrl) instead")
    override suspend fun resolveLink(resolver: LinkResolver, link: HttpUrl): Media? = null

	override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}
