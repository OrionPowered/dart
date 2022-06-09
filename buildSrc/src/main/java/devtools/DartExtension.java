package devtools;

import com.github.quillmc.tinymcp.Version;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public abstract class DartExtension {
    public static DartExtension register(Project project) {
        return project.getExtensions().create("dart", DartExtension.class);
    }

    abstract public Property<Version> getServerVersion();
}
