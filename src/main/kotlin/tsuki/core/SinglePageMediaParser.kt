package tsuki.core

import tsuki.InternalParsersApi
import tsuki.MediaLoaderContext
import tsuki.model.Media
import tsuki.model.MediaListFilter
import tsuki.model.MediaSource
import tsuki.model.SortOrder

@InternalParsersApi
public abstract class SinglePageMediaParser(
	context: MediaLoaderContext,
	source: MediaSource,
) : AbstractMediaParser(context, source) {

	final override suspend fun getList(offset: Int, order: SortOrder, filter: MediaListFilter): List<Media> {
		if (offset > 0) {
			return emptyList()
		}
		return getList(order, filter)
	}

	public abstract suspend fun getList(order: SortOrder, filter: MediaListFilter): List<Media>
}
