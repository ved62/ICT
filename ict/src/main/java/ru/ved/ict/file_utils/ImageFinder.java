package ru.ved.ict.file_utils;

import java.nio.file.Path;

public interface ImageFinder {

	Path[] getImagePaths(Path folder, boolean listSubfolders);

}
