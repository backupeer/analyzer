package github.backupeer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static github.backupeer.Utils.putSeparator;

public class Analyzer {
    public Analyzer() {
    }

    public void scan(AnalyzerRestrictions restrictions, Consumer<String> onFound, BiConsumer<String, Exception> onError) {
        AnalyzerFilters filters = new AnalyzerFilters();

        for (String d : restrictions.getDirs()) {
            Path directory = Path.of(d);
            try {
                Files.walkFileTree(directory, new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!attrs.isDirectory() && !attrs.isSymbolicLink()) {

                            if (!restrictions.isRecursive()) {
                                String parent = putSeparator(file.getParent().toString());
                                String stdDir = putSeparator(d);

                                if (!parent.equals(stdDir)) {
                                    return FileVisitResult.CONTINUE;
                                }
                            }

                            if (restrictions.getIgnoredDirs() != null && !restrictions.getIgnoredDirs().isEmpty()) {
                                if (filters.filterIgnoredDirs(restrictions.getIgnoredDirs(), file.toString())) {
                                    return FileVisitResult.CONTINUE;
                                }
                            }

                            String filename = file.getFileName().toString();

                            if (restrictions.getFilenames() != null && !restrictions.getFilenames().isEmpty()) {
                                if (!filters.filterFilenames(restrictions.getFilenames(), filename)) {
                                    return FileVisitResult.CONTINUE;
                                }
                            }

                            if (restrictions.getIgnoredFilenames() != null && !restrictions.getIgnoredFilenames().isEmpty()) {
                                if (filters.filterFilenames(restrictions.getIgnoredFilenames(), filename)) {
                                    return FileVisitResult.CONTINUE;
                                }
                            }

                            onFound.accept(file.toString());
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        if (!file.toFile().isDirectory()) {
                            onError.accept(file.toString(), exc);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
