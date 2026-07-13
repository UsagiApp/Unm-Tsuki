package tsuki.config

public interface MediaSourceConfig {

	public operator fun <T> get(key: ConfigKey<T>): T
}
