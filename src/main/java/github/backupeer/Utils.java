package github.backupeer;

import java.io.File;

public class Utils {
    public static  String putSeparator(String s) {
        if (!s.endsWith(File.separator)) {
            s += File.separator;
        }
        return s;
    }

    public static String removeAsterisk(String s) {
        if (s.equals("*") || s.equals("**")) return s;

        if (s.startsWith("*")) {
            s = s.substring(1);
        }

        if (s.endsWith("*")) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

}
