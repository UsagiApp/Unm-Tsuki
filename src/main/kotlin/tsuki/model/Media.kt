package tsuki.model

import androidx.collection.ArrayMap
import tsuki.util.findById
import tsuki.util.nullIfEmpty

public data class Media(
	/**
	 * Unique identifier for media
	 */
	@JvmField public val id: Long,
	/**
	 * Media title, human-readable
	 */
	@JvmField public val title: String,
	/**
	 * Alternative titles (for example on other language), may be empty
	 */
	@JvmField public val altTitles: Set<String>,
	/**
	 * Relative url to media (**without** a domain) or any other uri.
	 * Used principally in parsers
	 */
	@JvmField public val url: String,
	/**
	 * Absolute url to media, must be ready to open in browser
	 */
	@JvmField public val publicUrl: String,
	/**
	 * Normalized media rating, must be in range of 0..1 or [RATING_UNKNOWN] if rating s unknown
	 * @see hasRating
	 */
	@JvmField public val rating: Float,
	/**
	 * Indicates that media may contain sensitive information (18+, NSFW)
	 */
	@JvmField public val contentRating: ContentRating?,
	/**
	 * Absolute link to the cover
	 * @see largeCoverUrl
	 */
	@JvmField public val coverUrl: String?,
	/**
	 * Tags (genres) of the media
	 */
	@JvmField public val tags: Set<MediaTag>,
	/**
	 * Media status (ongoing, finished) or null if unknown
	 */
	@JvmField public val state: MediaState?,
	/**
	 * Authors of the media
	 */
	@JvmField public val authors: Set<String>,
	/**
	 * Large cover url (absolute), null if is no large cover
	 * @see coverUrl
	 */
	@JvmField public val largeCoverUrl: String? = null,
	/**
	 * Media description, may be html or null
	 */
	@JvmField public val description: String? = null,
	/**
	 * List of chapters
	 */
	@JvmField public val chapters: List<Episode>? = null,
	/**
	 * Media source
	 */
	@JvmField public val source: MediaSource,
) {

	@Deprecated("Use other constructor")
	public constructor(
		/**
		 * Unique identifier for media
		 */
		id: Long,
		/**
		 * Media title, human-readable
		 */
		title: String,
		/**
		 * Alternative title (for example on other language), may be null
		 */
		altTitle: String?,
		/**
		 * Relative url to media (**without** a domain) or any other uri.
		 * Used principally in parsers
		 */
		url: String,
		/**
		 * Absolute url to media, must be ready to open in browser
		 */
		publicUrl: String,
		/**
		 * Normalized media rating, must be in range of 0..1 or [RATING_UNKNOWN] if rating s unknown
		 * @see hasRating
		 */
		rating: Float,
		/**
		 * Indicates that media may contain sensitive information (18+, NSFW)
		 */
		isNsfw: Boolean,
		/**
		 * Absolute link to the cover
		 * @see largeCoverUrl
		 */
		coverUrl: String?,
		/**
		 * Tags (genres) of the media
		 */
		tags: Set<MediaTag>,
		/**
		 * Media status (ongoing, finished) or null if unknown
		 */
		state: MediaState?,
		/**
		 * Authors of the media
		 */
		author: String?,
		/**
		 * Large cover url (absolute), null if is no large cover
		 * @see coverUrl
		 */
		largeCoverUrl: String? = null,
		/**
		 * Media description, may be html or null
		 */
		description: String? = null,
		/**
		 * List of chapters
		 */
		chapters: List<Episode>? = null,
		/**
		 * Media source
		 */
		source: MediaSource,
	) : this(
		id = id,
		title = title,
		altTitles = setOfNotNull(altTitle?.nullIfEmpty()),
		url = url,
		publicUrl = publicUrl,
		rating = rating,
		contentRating = if (isNsfw) ContentRating.ADULT else null,
		coverUrl = coverUrl?.nullIfEmpty(),
		tags = tags,
		state = state,
		authors = setOfNotNull(author),
		largeCoverUrl = largeCoverUrl?.nullIfEmpty(),
		description = description?.nullIfEmpty(),
		chapters = chapters,
		source = source,
	)

	/**
	 * Author of the media, may be null
	 */
	@Deprecated("Please use authors")
	public val author: String?
		get() = authors.firstOrNull()

	/**
	 * Alternative title (for example on other language), may be null
	 */
	@Deprecated("Please use altTitles")
	public val altTitle: String?
		get() = altTitles.firstOrNull()

	/**
	 * Return if media has a specified rating
	 * @see rating
	 */
	public val hasRating: Boolean
		get() = rating > 0f && rating <= 1f

	@Deprecated("Use contentRating instead", ReplaceWith("contentRating == ContentRating.ADULT"))
	public val isNsfw: Boolean
		get() = contentRating == ContentRating.ADULT

	public fun getEpisodes(dub: String?): List<Episode> {
		return chapters?.filter { x -> x.dub == dub }.orEmpty()
	}

	public fun findEpisodeById(id: Long): Episode? = chapters?.findById(id)

	public fun requireEpisodeById(id: Long): Episode = findEpisodeById(id)
		?: throw NoSuchElementException("Episode with id $id not found")

	public fun getSeasons(): Map<String?, Int> {
		if (chapters.isNullOrEmpty()) {
			return emptyMap()
		}
		val result = ArrayMap<String?, Int>()
		chapters.forEach {
			val key = it.dub
			result[key] = result.getOrDefault(key, 0) + 1
		}
		return result
	}
}
