package github.backupeer;

import java.io.File;
import java.util.List;

import static github.backupeer.Utils.putSeparator;
import static github.backupeer.Utils.removeAsterisk;

public class AnalyzerFilters {
    public AnalyzerFilters() {
    }



    public boolean filterIgnoredDirs(List<String> source, String dir) {
        for (String s : source) {
            if (dir.startsWith(putSeparator(s))) return true;
        }
        return false;
    }


    public boolean filterFilenames(List<String> src, String filename) {
        for (String s : src) {
            String raw = removeAsterisk(s);

            if(s.startsWith("*") && s.endsWith("*")){
                if(filename.contains(raw)){
                    return true;
                }
                continue;
            }

            if(!s.startsWith("*") && !s.endsWith("*")){
                if(filename == raw){
                    return true;
                }
                continue;
            }

            if(s.startsWith("*")){
                if(filename.endsWith(raw)){
                    return true;
                }
                continue;
            }

            if(s.endsWith("*")){
                if(filename.startsWith(raw)){
                    return true;
                }
                continue;
            }
        }
        return false;
    }
}
