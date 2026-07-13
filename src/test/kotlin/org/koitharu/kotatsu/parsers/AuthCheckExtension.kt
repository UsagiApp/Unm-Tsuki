package tsuki

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import tsuki.model.MediaParserSource
import tsuki.util.runCatchingCancellable

class AuthCheckExtension : BeforeAllCallback {

	private val loaderContext: MediaLoaderContext = MediaLoaderContextMock

	override fun beforeAll(context: ExtensionContext) {
		for (source in MediaParserSource.entries) {
			val parser = loaderContext.newParserInstance(source)
			if (parser is MediaParserAuthProvider) {
				checkAuthorization(source, parser)
			}
		}
	}

	private fun checkAuthorization(source: MediaParserSource, parser: MediaParserAuthProvider) = runTest {
		runCatchingCancellable {
			parser.getUsername()
		}.onSuccess { username ->
			println("Signed in to ${source.name} as $username")
		}.onFailure { error ->
			System.err.println("Auth failed for ${source.name}: ${error.javaClass.name}(${error.message})")
		}
	}
}
