package tsuki.model

import tsuki.InternalParsersApi

public data class MediaListFilterCapabilities @InternalParsersApi constructor(

	/**
	 * Whether parser supports filtering by more than one tag
	 * @see [MediaListFilter.tags]
	 * @see [MediaListFilterOptions.availableTags]
	 */
	val isMultipleTagsSupported: Boolean = false,

	/**
	 * Whether parser supports tagsExclude field in filter
	 * @see [MediaListFilter.tagsExclude]
	 * @see [MediaListFilterOptions.availableTags]
	 */
	val isTagsExclusionSupported: Boolean = false,

	/**
	 * Whether parser supports searching by string query
	 * @see [MediaListFilter.query]
	 */
	val isSearchSupported: Boolean = false,

	/**
	 * Whether parser supports searching by string query combined within other filters
	 */
	val isSearchWithFiltersSupported: Boolean = false,

	/**
	 * Whether parser supports searching/filtering by year
	 * @see [MediaListFilter.year]
	 */
	val isYearSupported: Boolean = false,

	/**
	 * Whether parser supports searching by year range
	 * @see [MediaListFilter.yearFrom] and [MediaListFilter.yearTo]
	 */
	val isYearRangeSupported: Boolean = false,

	/**
	 * Whether parser supports searching Original Languages
	 * @see [MediaListFilter.originalLocale]
	 * @see [MediaListFilterOptions.availableLocales]
	 */
	val isOriginalLocaleSupported: Boolean = false,

	/**
	 * Whether parser supports searching by author name
	 * @see [MediaListFilter.author]
	 */
	val isAuthorSearchSupported: Boolean = false,
)
