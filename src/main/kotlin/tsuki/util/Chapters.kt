package tsuki.util

import org.json.JSONArray
import org.json.JSONObject
import tsuki.InternalParsersApi
import tsuki.model.Episode
import tsuki.util.json.asTypedList

@InternalParsersApi
public inline fun <T> List<T>.mapChapters(
	reversed: Boolean = false,
	transform: (index: Int, T) -> Episode?,
): List<Episode> {
	val builder = ChaptersListBuilder(collectionSize())
	var index = 0
	val elements = if (reversed) this.asReversed() else this
	for (item in elements) {
		if (builder.add(transform(index, item))) {
			index++
		}
	}
	return builder.toList()
}

@InternalParsersApi
public inline fun JSONArray.mapChapters(
	reversed: Boolean = false,
	transform: (index: Int, JSONObject) -> Episode?,
): List<Episode> = asTypedList<JSONObject>().mapChapters(reversed, transform)

@InternalParsersApi
public inline fun <T> List<T>.flatMapChapters(
	reversed: Boolean = false,
	transform: (T) -> Iterable<Episode?>,
): List<Episode> {
	val builder = ChaptersListBuilder(collectionSize())
	val elements = if (reversed) this.asReversed() else this
	for (item in elements) {
		builder.addAll(transform(item))
	}
	return builder.toList()
}

public fun <T> Iterable<T>.collectionSize(): Int {
	return if (this is Collection<*>) this.size else 10
}

public class ChaptersListBuilder(initialSize: Int) {

	private val ids = HashSet<Long>(initialSize)
	private val list = ArrayList<Episode>(initialSize)

	fun add(episode: Episode?): Boolean {
		return episode != null && ids.add(episode.id) && list.add(episode)
	}

	fun addAll(chapters: Iterable<Episode?>) {
		if (chapters is Collection<*>) {
			list.ensureCapacity(list.size + chapters.size)
		}
		chapters.forEach { add(it) }
	}

	operator fun plusAssign(episode: Episode?) {
		add(episode)
	}

	fun reverse() {
		list.reverse()
	}

	fun toList(): List<Episode> = list
}
