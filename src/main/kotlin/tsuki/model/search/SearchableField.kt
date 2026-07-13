package tsuki.model.search

import tsuki.model.*
import java.util.*

/**
 * Represents the various fields that can be used for searching media.
 * Each field is associated with a specific data type that defines its expected values.
 *
 * @property type The Java class representing the expected type of values for this field.
 */
@Deprecated("Too complex")
public enum class SearchableField(public val type: Class<*>) {
	TITLE_NAME(String::class.java),
	TAG(MediaTag::class.java),
	AUTHOR(MediaTag::class.java),
	LANGUAGE(Locale::class.java),
	ORIGINAL_LANGUAGE(Locale::class.java),
	STATE(MediaState::class.java),
	CONTENT_TYPE(ContentType::class.java),
	CONTENT_RATING(ContentRating::class.java),
	PUBLICATION_YEAR(Int::class.javaObjectType);
}
