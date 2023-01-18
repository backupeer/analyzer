import github.backupeer.AnalyzerFilters;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerFiltersTests {
    @BeforeAll
    public static void setup() {
        TestUtils.createFileTree();
    }

    @AfterAll
    public static void cleanup() {
        TestUtils.deleteFileTree();
    }

    private List<String> filterIgnoredDirs(List<String> src) {
        AnalyzerFilters filters = new AnalyzerFilters();
        List<String> founds = new ArrayList<>();

        try {
            Path path = Path.of(FilenameUtils.separatorsToSystem("./home"));
            Files.walk(path).forEach((it) -> {
                if (!it.toFile().isDirectory()) {
                    if (!filters.filterIgnoredDirs(src, it.toString())) {
                        founds.add(it.toString());
                    }
                }
            });
        } catch (IOException e) {
        }

        return founds;
    }

    @Test
    public void filterIgnoredDirsTest() {
        String downloads = FilenameUtils.separatorsToSystem("./home/Downloads");
        String doc = FilenameUtils.separatorsToSystem("./home/Doc");
        List<String> res1 = filterIgnoredDirs(List.of(downloads, doc));
        Assertions.assertEquals(7, res1.size());
    }

    public List<String> filterFilenames(List<String> src) {
        AnalyzerFilters filters = new AnalyzerFilters();
        Path path = Path.of(FilenameUtils.separatorsToSystem("./home"));
        List<String> founds = new ArrayList<>();
        try {
            Files.walk(path).forEach((it) -> {
                if(!it.toFile().isDirectory()){
                    if(filters.filterFilenames(src, it.getFileName().toString())){
                        founds.add(it.toString());
                    }
                }
            });
        } catch (IOException e) {}
        return founds;
    }

    @Test
    public void filterFilenamesTest() {
        List<String> res1 = filterFilenames(List.of("*bash*"));
        List<String> res2 = filterFilenames(List.of("*lnk"));
        List<String> res3 = filterFilenames(List.of("arch*"));

        Assertions.assertEquals(2, res1.size());
        Assertions.assertEquals(2, res2.size());
        Assertions.assertEquals(1, res3.size());

        Assertions.assertTrue(res1.contains(FilenameUtils.separatorsToSystem("./home/.bash_profile")));
        Assertions.assertTrue(res1.contains(FilenameUtils.separatorsToSystem("./home/.bashrc")));
        Assertions.assertTrue(res2.contains(FilenameUtils.separatorsToSystem("./home/Desktop/firefox.lnk")));
        Assertions.assertTrue(res2.contains(FilenameUtils.separatorsToSystem("./home/Desktop/vscode.lnk")));
        Assertions.assertTrue(res3.contains(FilenameUtils.separatorsToSystem("./home/Downloads/archlinux.iso")));
    }
}
