package tsuki

import tsuki.config.ConfigKey
import tsuki.config.MediaSourceConfig

internal class SourceConfigMock : MediaSourceConfig {

	override fun <T> get(key: ConfigKey<T>): T = key.defaultValue
}