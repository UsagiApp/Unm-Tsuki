@file:Suppress("warnings", "unused")

package tsuki.util

import tsuki.MediaLoaderContext

public class WebViewHelper(
	private val context: MediaLoaderContext,
) {

	public suspend fun getLocalStorageValue(domain: String, key: String): String? {
		return context.evaluateJs("https://$domain/", "window.localStorage.getItem(\"$key\")")
	}

	public suspend fun getUrlValue(url: String, value: String): String? {
		return context.evaluateJs(url, "new URLSearchParams(window.location.search).get('$value');")
	}
}
