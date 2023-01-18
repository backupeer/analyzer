import github.backupeer.AnalyzerRestrictions;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnalyzerRestrictionsTests {
    String filename = "./analyzer/restriction.json";

    private AnalyzerRestrictions analyzerRestrictions = new AnalyzerRestrictions()
            .setDirs(List.of("/"))
            .setIgnoredDirs(List.of("/opt", "/etc", "/proc"))
            .setRecursive(true);

    @Test
    @Order(1)
    public void toJSONTest() {
        Assertions.assertDoesNotThrow(() -> {
            analyzerRestrictions.toJSON(FilenameUtils.separatorsToSystem(filename));

            Assertions.assertTrue(new File(FilenameUtils.separatorsToSystem(filename)).exists());
        });
    }

    @Test
    @Order(2)
    public void fromJSONTest() {
        Assertions.assertDoesNotThrow(() -> {
            AnalyzerRestrictions restrictions = AnalyzerRestrictions.fromJSON(filename);

            Assertions.assertEquals(analyzerRestrictions.getDirs(), restrictions.getDirs());
            Assertions.assertEquals(analyzerRestrictions.getIgnoredDirs(), restrictions.getIgnoredDirs());
            Assertions.assertEquals(analyzerRestrictions.getFilenames(), restrictions.getFilenames());
            Assertions.assertEquals(analyzerRestrictions.getIgnoredFilenames(), restrictions.getIgnoredFilenames());
            Assertions.assertEquals(analyzerRestrictions.isRecursive(), restrictions.isRecursive());
        });

        Assertions.assertThrows(NoSuchFileException.class, () -> {
            AnalyzerRestrictions restrictions = AnalyzerRestrictions.fromJSON("somepath");
        });
    }

    @AfterAll
    public static void cleanup(){
        try{
            new File(FilenameUtils.separatorsToSystem("./analyzer/restriction.json")).delete();
            new File(FilenameUtils.separatorsToSystem("./analyzer")).delete();
        }catch (Exception e){}
    }
}
