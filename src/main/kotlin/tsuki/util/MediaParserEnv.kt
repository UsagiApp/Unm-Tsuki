package tsuki.util

import okhttp3.HttpUrl
import org.jsoup.nodes.Element
import tsuki.ErrorMessages
import tsuki.InternalParsersApi
import tsuki.MediaParser
import tsuki.core.AbstractMediaParser
import tsuki.exception.ParseException
import tsuki.model.*


/**
 * Create a unique id for [Media]/[Episode]/[VideoSource].
 * @param source the media source
 * @param url must be relative url, without a domain
 * @see [Media.id]
 * @see [Episode.id]
 * @see [VideoSource.id]
 */
public fun generateUid(source: MediaSource, url: String): Long {
	var h = LONG_HASH_SEED
	source.name.forEach { c ->
		h = 31 * h + c.code
	}
	url.forEach { c ->
		h = 31 * h + c.code
	}
	return h
}

/**
 * Create a unique id for [Media]/[Episode]/[VideoSource].
 * @param source the media source
 * @param id an internal identifier
 * @see [Media.id]
 * @see [Episode.id]
 * @see [VideoSource.id]
 */
public fun generateUid(source: MediaSource, id: Long): Long {
	var h = LONG_HASH_SEED
	source.name.forEach { c ->
		h = 31 * h + c.code
	}
	h = 31 * h + id
	return h
}

@InternalParsersApi
public fun Element.parseFailed(message: String? = null): Nothing {
	throw ParseException(message, ownerDocument()?.location() ?: baseUri(), null)
}

@InternalParsersApi
public fun Set<MediaTag>?.oneOrThrowIfMany(): MediaTag? = oneOrThrowIfMany(
	ErrorMessages.FILTER_MULTIPLE_GENRES_NOT_SUPPORTED,
)

@InternalParsersApi
public fun Set<MediaState>?.oneOrThrowIfMany(): MediaState? = oneOrThrowIfMany(
	ErrorMessages.FILTER_MULTIPLE_STATES_NOT_SUPPORTED,
)

@InternalParsersApi
public fun Set<ContentType>?.oneOrThrowIfMany(): ContentType? = oneOrThrowIfMany(
	ErrorMessages.FILTER_MULTIPLE_CONTENT_TYPES_NOT_SUPPORTED,
)

@InternalParsersApi
public fun Set<ContentRating>?.oneOrThrowIfMany(): ContentRating? = oneOrThrowIfMany(
	ErrorMessages.FILTER_MULTIPLE_CONTENT_RATING_NOT_SUPPORTED,
)

private fun <T> Set<T>?.oneOrThrowIfMany(msg: String): T? = when {
	isNullOrEmpty() -> null
	size == 1 -> first()
	else -> throw IllegalArgumentException(msg)
}

public fun urlBuilder(domain: String, subdomain: String? = null): HttpUrl.Builder {
	return HttpUrl.Builder()
		.scheme(SCHEME_HTTPS)
		.host(if (subdomain == null) domain else "$subdomain.$domain")
}

public fun MediaParser.generateUid(url: String): Long =
	tsuki.util.generateUid(source, url)

public fun MediaParser.generateUid(id: Long): Long =
	tsuki.util.generateUid(source, id)


public fun MediaParser.urlBuilder(subdomain: String? = null): HttpUrl.Builder =
	tsuki.util.urlBuilder(domain, subdomain)

@InternalParsersApi
public fun getDomain(parser: MediaParser, subdomain: String): String {
	return subdomain + "." + parser.domain.removePrefix("www.")
}
