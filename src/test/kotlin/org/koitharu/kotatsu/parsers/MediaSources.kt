package tsuki

import org.junit.jupiter.params.provider.EnumSource
import tsuki.model.MediaParserSource

// Change 'names' to test specified parsers
@EnumSource(MediaParserSource::class, names = [], mode = EnumSource.Mode.INCLUDE)
internal annotation class MediaSources
