package github.backupeer;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerRestrictions {
    List<String> dirs, ignoredDirs, filenames, ignoredFilenames;
    boolean recursive;

    public AnalyzerRestrictions(){
        this.dirs = new ArrayList<>();
        this.ignoredDirs = new ArrayList<>();
        this.filenames = new ArrayList<>();
        this.ignoredFilenames = new ArrayList<>();
        this.recursive = false;
    }


    public AnalyzerRestrictions copy() {
        return new AnalyzerRestrictions()
                .setDirs(dirs)
                .setIgnoredDirs(ignoredDirs)
                .setFilenames(filenames)
                .setIgnoredFilenames(ignoredFilenames)
                .setRecursive(recursive);
    }

    public List<String> getDirs() {
        return dirs;
    }

    public AnalyzerRestrictions setDirs(List<String> dirs) {
        this.dirs = dirs;
        return this;
    }

    public List<String> getIgnoredDirs() {
        return ignoredDirs;
    }

    public AnalyzerRestrictions setIgnoredDirs(List<String> ignoredDirs) {
        this.ignoredDirs = ignoredDirs;
        return this;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public AnalyzerRestrictions setFilenames(List<String> filenames) {
        this.filenames = filenames;
        return this;
    }

    public List<String> getIgnoredFilenames() {
        return ignoredFilenames;
    }

    public AnalyzerRestrictions setIgnoredFilenames(List<String> ignoredFilenames) {
        this.ignoredFilenames = ignoredFilenames;
        return this;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public AnalyzerRestrictions setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    public static AnalyzerRestrictions fromJSON(String path) throws IOException {
        path = FilenameUtils.separatorsToSystem(path);
        String raw = Files.readString(Path.of(path));
        return new Gson().fromJson(raw, AnalyzerRestrictions.class);
    }

    private void exportJSON(String path) throws IOException {
        String raw = new Gson().toJson(this);
        OpenOption[] options = new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        Files.writeString(Path.of(path), raw, Charset.defaultCharset(), options);
    }

    public void toJSON(String path) throws IOException {
        path = FilenameUtils.separatorsToSystem(path);
        Path parent = Path.of(path).getParent();

        if (path == null || path == "" ||path == "." || path == FilenameUtils.separatorsToSystem("./")){
            exportJSON(path);
            return;
        }else{
            parent.toFile().mkdirs();
            exportJSON(path);
        }
    }
}
