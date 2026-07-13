package tsuki.model

import java.util.*

public data class MediaListFilter(
	@JvmField val query: String? = null,
	@JvmField val tags: Set<MediaTag> = emptySet(),
	@JvmField val tagsExclude: Set<MediaTag> = emptySet(),
	@JvmField val locale: Locale? = null,
	@JvmField val originalLocale: Locale? = null,
	@JvmField val states: Set<MediaState> = emptySet(),
	@JvmField val contentRating: Set<ContentRating> = emptySet(),
	@JvmField val types: Set<ContentType> = emptySet(),
	@JvmField val year: Int = YEAR_UNKNOWN,
	@JvmField val yearFrom: Int = YEAR_UNKNOWN,
	@JvmField val yearTo: Int = YEAR_UNKNOWN,
	@JvmField val author: String? = null,
	@JvmField val rawFilter: Any? = null,
) {

	private fun isNonSearchOptionsEmpty(): Boolean = tags.isEmpty() &&
		tagsExclude.isEmpty() &&
		locale == null &&
		originalLocale == null &&
		states.isEmpty() &&
		contentRating.isEmpty() &&
		year == YEAR_UNKNOWN &&
		yearFrom == YEAR_UNKNOWN &&
		yearTo == YEAR_UNKNOWN &&
		types.isEmpty() &&
		author.isNullOrEmpty() &&
		rawFilter == null

	public fun isEmpty(): Boolean = isNonSearchOptionsEmpty() && query.isNullOrEmpty()

	public fun isNotEmpty(): Boolean = !isEmpty()

	public fun hasNonSearchOptions(): Boolean = !isNonSearchOptionsEmpty()

	public companion object {

		@JvmStatic
		public val EMPTY: MediaListFilter = MediaListFilter()
	}

	internal class Builder {
		private var query: String? = null
		private val tags: MutableSet<MediaTag> = mutableSetOf()
		private val tagsExclude: MutableSet<MediaTag> = mutableSetOf()
		private var locale: Locale? = null
		private var originalLocale: Locale? = null
		private val states: MutableSet<MediaState> = mutableSetOf()
		private val contentRating: MutableSet<ContentRating> = mutableSetOf()
		private val types: MutableSet<ContentType> = mutableSetOf()
		private var year: Int = YEAR_UNKNOWN
		private var yearFrom: Int = YEAR_UNKNOWN
		private var yearTo: Int = YEAR_UNKNOWN
		private var author: String? = null
		private var rawFilter: Any? = null

		fun query(query: String?): Builder = apply { this.query = query }
		fun addTag(tag: MediaTag): Builder = apply { tags.add(tag) }
		fun addTags(tags: Collection<MediaTag>): Builder = apply { this.tags.addAll(tags) }
		fun excludeTag(tag: MediaTag): Builder = apply { tagsExclude.add(tag) }
		fun excludeTags(tags: Collection<MediaTag>): Builder = apply { this.tagsExclude.addAll(tags) }
		fun locale(locale: Locale?): Builder = apply { this.locale = locale }
		fun originalLocale(locale: Locale?): Builder = apply { this.originalLocale = locale }
		fun addState(state: MediaState): Builder = apply { states.add(state) }
		fun addStates(states: Collection<MediaState>): Builder = apply { this.states.addAll(states) }
		fun addContentRating(rating: ContentRating): Builder = apply { contentRating.add(rating) }
		fun addContentRatings(ratings: Collection<ContentRating>): Builder =
			apply { this.contentRating.addAll(ratings) }

		fun addType(type: ContentType): Builder = apply { types.add(type) }
		fun addTypes(types: Collection<ContentType>): Builder = apply { this.types.addAll(types) }

		fun year(year: Int): Builder = apply { this.year = year }
		fun yearFrom(year: Int): Builder = apply { this.yearFrom = year }
		fun yearTo(year: Int): Builder = apply { this.yearTo = year }
		fun author(author: String?): Builder = apply { this.author = author }
		fun rawFilter(value: Any?): Builder = apply { this.rawFilter = value }

		fun build(): MediaListFilter = MediaListFilter(
			query, tags, tagsExclude, locale, originalLocale, states,
			contentRating, types, year, yearFrom, yearTo,
			author, rawFilter
		)
	}
}
