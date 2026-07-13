@file:Suppress("DEPRECATION")

package tsuki.core

import androidx.annotation.VisibleForTesting
import tsuki.MediaLoaderContext
import tsuki.model.Media
import tsuki.model.MediaSource
import tsuki.model.search.MediaSearchQuery
import tsuki.model.search.SearchableField
import tsuki.util.Paginator

@Deprecated("Too complex. Use PagedMediaParser instead")
public abstract class FlexiblePagedMediaParser(
	context: MediaLoaderContext,
	source: MediaSource,
	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED) @JvmField public val pageSize: Int,
	searchPageSize: Int = pageSize,
) : FlexibleMediaParser(context, source) {

	@JvmField
	protected val paginator: Paginator = Paginator(pageSize)

	@JvmField
	protected val searchPaginator: Paginator = Paginator(searchPageSize)

	@Deprecated("Too complex. Use getList with filter instead")
	final override suspend fun getList(query: MediaSearchQuery): List<Media> {
		var containTitleNameCriteria = false
		query.criteria.forEach {
			if (it.field == SearchableField.TITLE_NAME) {
				containTitleNameCriteria = true
			}
		}

		return searchMedia(
			paginator = if (containTitleNameCriteria) {
				paginator
			} else {
				searchPaginator
			},
			query = query,
		)
	}

	public abstract suspend fun getListPage(query: MediaSearchQuery, page: Int): List<Media>

	protected fun setFirstPage(firstPage: Int, firstPageForSearch: Int = firstPage) {
		paginator.firstPage = firstPage
		searchPaginator.firstPage = firstPageForSearch
	}

	private suspend fun searchMedia(
		paginator: Paginator,
		query: MediaSearchQuery,
	): List<Media> {
		val offset: Int = query.offset
		val page = paginator.getPage(offset)
		val list = getListPage(query, page)
		paginator.onListReceived(offset, page, list.size)
		return list
	}
}
