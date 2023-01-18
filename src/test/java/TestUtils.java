import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestUtils {
    private static String root = FilenameUtils.separatorsToSystem("./home");
    private static Map<String, List<String>> tree = Map.of(
            "Downloads", List.of("archlinux.iso", "naruto.torrent", "vscode_linux.tar.gz"),
            "Desktop", List.of("vscode.lnk", "firefox.lnk"),
            "Documents", List.of("report.pdf"),
            "Pictures", List.of("wallpaper.png", "screenshot.jpg")
    );

    public static void createFileTree() {
        try{
            File home = new File(root);
            home.mkdir();

            tree.forEach((k,v)->{
                File subfolder = new File(home, k);
                subfolder.mkdir();

                for(String file : v){
                    try {
                        new File(subfolder, file).createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            new File(home, ".bashrc").createNewFile();
            new File(home, ".bash_profile").createNewFile();
        }catch (Exception e){}
    }

    public static void deleteFileTree() {
        List<Path> folders = new ArrayList<>();
        try {
            Files.walk(Path.of(root)).forEach((it)->{
                if(it.toFile().isDirectory()) {
                    folders.add(it);
                    return;
                };
                it.toFile().delete();
            });

            folders.forEach((it)->{
                if(it.endsWith(root)) return;
                it.toFile().delete();
            });

            new File(root).delete();
        } catch (IOException e) {}
    }
}
