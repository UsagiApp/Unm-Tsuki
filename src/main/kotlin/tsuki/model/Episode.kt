package tsuki.model

import tsuki.util.formatSimple
import tsuki.util.ifNullOrEmpty

public data class Episode(
	/**
	 * An unique id of episode
	 */
	@JvmField public val id: Long,
	/**
	 * User-readable name of episode if provided by parser or null instead
	 */
	@JvmField public val title: String?,
	/**
	 * Episode number starting from 1, 0 if unknown
	 */
	@JvmField public val number: Float,
	/**
	 * Season number starting from 1, 0 if unknown
	 */
	@JvmField public val season: Int,
	/**
	 * Relative url to episode (**without** a domain) or any other uri.
	 * Used principally in parsers
	 */
	@JvmField public val url: String,
	/**
	 * User-readable name of fansub (fansubbing group) or null if unknown
	 */
	@JvmField public val fansub: String?,
	/**
	 * Episode upload date in milliseconds
	 */
	@JvmField public val uploadDate: Long,
	/**
	 * Duration in seconds, null if unknown
	 */
	@JvmField public val duration: Int? = null,
	/**
	 * User-readable name of dub.
	 * A dub is a group of episodes that overlap (e.g. different languages)
	 */
	@JvmField public val dub: String?,
	@JvmField public val source: MediaSource,
) {

	@Deprecated("Use title instead", ReplaceWith("title"))
	val name: String
		get() = title.ifNullOrEmpty {
			buildString {
				if (season > 0) append("S").append(season).append(' ')
				if (number > 0) append("E").append(number.formatSimple()) else append("Unnamed")
			}
		}

	public fun numberString(): String? = if (number > 0f) {
		number.formatSimple()
	} else {
		null
	}

	public fun seasonString(): String? = if (season > 0) {
		season.toString()
	} else {
		null
	}
}
