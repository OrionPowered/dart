package devtools;

import com.alexsobiek.async.util.Lazy;
import com.github.quillmc.tinymcp.TinyMCP;
import devtools.task.GenPatchesTask;
import devtools.task.GenSourcesTask;
import org.gradle.api.Project;
import org.slf4j.Logger;

import java.io.File;

public class DartPlugin implements org.gradle.api.Plugin<Project> {

    private static DartPlugin plugin;
    private static DartExtension extension;
    private static Logger logger;
    private static final Lazy<Project> project = Lazy.delayed();
    public static final Lazy<TinyMCP> tinymcp = Lazy.delayed();
    private static final Lazy<File> patchDir = new Lazy<>(() -> new File(project.get().getProjectDir(), "patches"));
    private static final Lazy<File> sourceDir = new Lazy<>(() -> new File(project.get().getProjectDir(), "src/main/java"));

    @Override
    public void apply(Project project) {
        this.plugin = this;
        this.project.set(project);
        if (!patchDir.get().exists()) patchDir.get().mkdirs();

        extension = DartExtension.register(project);
        GenSourcesTask.register(project);
        GenPatchesTask.register(project);
    }


    public static Project getProject() {
        return project.get();
    }

    public static Logger getLogger() {
        return logger;
    }

    public static DartExtension getExtension() {
        return extension;
    }

    public static TinyMCP getTinyMCP() {
        return tinymcp.get();
    }

    public static File getProjectDir() {
        return project.get().getProjectDir();
    }

    public static File getSourceDir() {
        return sourceDir.get();
    }

    public static File getPatchDir() {
        return patchDir.get();
    }

    public static DartPlugin get() {
        return plugin;
    }
}
