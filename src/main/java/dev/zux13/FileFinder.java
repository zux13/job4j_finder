package dev.zux13;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileFinder {

    private final SearchType type;
    private final String pattern;
    private final String directory;

    public FileFinder(SearchType type, String pattern, String directory) {
        this.type = type;
        this.pattern = pattern;
        this.directory = directory;
    }

    public List<Path> findFiles() throws IOException {

        List<Path> foundFiles = new ArrayList<>();

        Files.walkFileTree(Path.of(directory), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (isMatch(file)) {
                    foundFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {

                if (exc instanceof AccessDeniedException) {
                    System.err.println("Access denied to: " + file.toString());
                } else {
                    System.err.println("Error accessing file: " + file.toString() + " - " + exc.getMessage());
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return foundFiles;
    }

    private boolean isMatch(Path path) {
        if (type == SearchType.NAME) {
            return path.getFileName().toString().contains(pattern);
        } else  {
            Pattern pat = Pattern.compile((type == SearchType.MASK) ? convertMaskToRegex() : pattern);
            return pat.matcher(path.getFileName().toString()).find();
        }
    }

    private String convertMaskToRegex() {

        return pattern.replaceAll("\\.", "[.]")
                .replaceAll("\\*", ".*")
                .replaceAll("\\?", ".");

    }
}
