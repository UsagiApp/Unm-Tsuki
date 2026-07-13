package tsuki.util

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tsuki.MediaLoaderContextMock
import tsuki.model.MediaParserSource
import kotlin.time.Duration.Companion.minutes

internal class LinkResolverTest {

	private val context = MediaLoaderContextMock

	@Test
	fun supportedSource() = runTest(timeout = 2.minutes) {
		val resolver = context.newLinkResolver("REDACTED" /* do not publish links to media on GitHub */)
		Assertions.assertEquals(MediaParserSource.MANGADEX, resolver.getSource())
		val media = resolver.getMedia()
		Assertions.assertEquals(resolver.link.toString(), media?.publicUrl)
	}

	@Test
	fun unsupportedSource2() = runTest(timeout = 2.minutes) {
		val resolver = context.newLinkResolver("REDACTED" /* do not publish links to media on GitHub */)
		Assertions.assertEquals(MediaParserSource.BATOTO, resolver.getSource())
		val media = resolver.getMedia()
		Assertions.assertEquals(resolver.link.toString(), media?.publicUrl)
	}
}
