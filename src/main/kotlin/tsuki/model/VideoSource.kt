package tsuki.model

public data class VideoSource(
	/**
	 * Unique identifier for video source
	 */
	@JvmField public val id: Long,
	/**
	 * Absolute streaming URL
	 */
	@JvmField public val url: String,
	/**
	 * Video quality label (e.g. "1080p", "720p", "480p")
	 */
	@JvmField public val quality: String,
	/**
	 * Video format (e.g. "hls", "mp4", "dash")
	 */
	@JvmField public val format: String,
	/**
	 * Thumbnail/preview URL if available, null otherwise
	 */
	@JvmField public val thumbnail: String? = null,
	@JvmField public val source: MediaSource,
)
