package tsuki.exception

import okio.IOException
import tsuki.InternalParsersApi
import tsuki.model.MediaSource

/**
 * Authorization is required for access to the requested content
 */
public class AuthRequiredException @InternalParsersApi @JvmOverloads constructor(
	public val source: MediaSource,
	cause: Throwable? = null,
) : IOException("Authorization required", cause)
