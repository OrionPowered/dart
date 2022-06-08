package devtools;

import com.github.quillmc.tinymcp.TinyMCP;
import com.github.quillmc.tinymcp.Version;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public abstract class DartExtension {

    public static DartExtension register(Project project) {
        DartExtension ext = project.getExtensions().create("dart", DartExtension.class);
        System.setProperty("TINYMCP_CACHE", Constants.CACHE.resolve("tinymcp").toAbsolutePath().toString());
        return ext;
    }

    abstract public Property<Version> getServerVersion();
}
