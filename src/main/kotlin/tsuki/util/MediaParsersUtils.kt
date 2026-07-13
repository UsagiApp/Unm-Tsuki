@file:JvmName("MediaParsersUtils")

package tsuki.util

import tsuki.model.Episode
import tsuki.model.MediaListFilter
import kotlin.contracts.contract

public fun MediaListFilter?.isNullOrEmpty(): Boolean {
	contract {
		returns(false) implies (this@isNullOrEmpty != null)
	}
	return this == null || this.isEmpty()
}

public fun Collection<Episode>.findById(chapterId: Long): Episode? = find { x ->
	x.id == chapterId
}
