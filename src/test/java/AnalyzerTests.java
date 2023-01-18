import github.backupeer.Analyzer;
import github.backupeer.AnalyzerRestrictions;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerTests {
    @BeforeAll
    public static void setup() {
        TestUtils.createFileTree();
    }

    @AfterAll
    public static void cleanup() {
        TestUtils.deleteFileTree();
    }

    public List<String> scan(AnalyzerRestrictions r, boolean wantErrors) {
        List<String> res = new ArrayList<>();
        Analyzer analyzer = new Analyzer();


        analyzer.scan(
                r,
                (it) -> {
                    if (!wantErrors) {
                        res.add(it);
                    }
                },
                (dir, err) -> {
                    if (wantErrors) {
                        res.add(dir);
                    }
                }
        );

        return res;
    }

    @Test
    public void scanTest() {
        AnalyzerRestrictions base = new AnalyzerRestrictions()
                .setDirs(List.of(FilenameUtils.separatorsToSystem("./home")))
                .setRecursive(true);

        AnalyzerRestrictions noRecursive = base.copy().setRecursive(false);
        AnalyzerRestrictions onlyLnk = base.copy().setFilenames(List.of("*.lnk"));
        AnalyzerRestrictions onlyDownloads = base.copy().setDirs(List.of(FilenameUtils.separatorsToSystem("./home/Downloads")));
        AnalyzerRestrictions excludeDownloads = base.copy().setIgnoredDirs(List.of(FilenameUtils.separatorsToSystem("./home/Downloads")));

        Assertions.assertEquals(10, scan(base, false).size());
        Assertions.assertEquals(2, scan(noRecursive, false).size());
        Assertions.assertEquals(2, scan(onlyLnk, false).size());
        Assertions.assertEquals(3, scan(onlyDownloads, false).size());
        Assertions.assertEquals(7, scan(excludeDownloads, false).size());
    }
}
