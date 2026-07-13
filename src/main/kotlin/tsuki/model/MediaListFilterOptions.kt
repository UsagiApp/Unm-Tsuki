package tsuki.model

import tsuki.InternalParsersApi
import java.util.*

public data class MediaListFilterOptions @InternalParsersApi constructor(

	/**
	 * Available tags (genres)
	 */
	public val availableTags: Set<MediaTag> = emptySet(),

	/**
	 * Supported [MediaState] variants for filtering. May be empty.
	 *
	 * For better performance use [EnumSet] for more than one item.
	 */
	public val availableStates: Set<MediaState> = emptySet(),

	/**
	 * Supported [ContentRating] variants for filtering. May be empty.
	 *
	 * For better performance use [EnumSet] for more than one item.
	 */
	public val availableContentRating: Set<ContentRating> = emptySet(),

	/**
	 * Supported [ContentType] variants for filtering. May be empty.
	 *
	 * For better performance use [EnumSet] for more than one item.
	 */
	public val availableContentTypes: Set<ContentType> = emptySet(),

	/**
	 * Supported content locales for multilingual sources
	 */
	public val availableLocales: Set<Locale> = emptySet(),
)
