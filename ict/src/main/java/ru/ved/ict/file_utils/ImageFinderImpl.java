package ru.ved.ict.file_utils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageFinderImpl implements ImageFinder {

	/**
	 * IS_IMAGE - handy predicate for filtering images within Files.find
	 */
	private static final BiPredicate<Path, BasicFileAttributes> IS_IMAGE = (p, a) -> {
		if (a.isRegularFile()) {
			return getMimeType(p).startsWith("image");
		}
		return false;
	};

	private static final Logger LOG = LoggerFactory.getLogger(ImageFinderImpl.class);

	/**
	 * getMimeType - handy method used by IS_IMAGE predicate. It has been created
	 * for exception handling outside of predicate.
	 *
	 * @param path - Path to file for detection
	 *
	 * @return String with mime information
	 */
	private static String getMimeType(final Path path) {
		var mime = "";
		try {
			mime = Files.probeContentType(path);
		} catch (final IOException ex) {
			LOG.error("Unable to get mime type for {} ", path, ex);
		}
		return mime;
	}

	@Override
	public Path[] getImagePaths(final Path folder, final boolean listSubfolders) {
		final var maxDepth = listSubfolders ? 99 : 0;
		try (var pathStream = Files.find(folder, maxDepth, IS_IMAGE, FileVisitOption.FOLLOW_LINKS)) {
			return pathStream.toArray(Path[]::new);
		} catch (final IOException ex) {
			LOG.error("Unable to read folder {} ", folder, ex);
		}
		return new Path[0];
	}
}
