package devtools;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final Path CACHE = Paths.get(
            System.getenv("GRADLE_USER_HOME") != null
                    ? System.getenv("GRADLE_USER_HOME")
                    : System.getenv("HOME") + "/.gradle",
            "caches", "quill");
}
