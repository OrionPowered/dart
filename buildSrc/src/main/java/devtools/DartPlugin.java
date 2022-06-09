package devtools;


import com.github.quillmc.tinymcp.TinyMCP;
import devtools.GitManager;
import devtools.task.ApplyPatchesTask;
import devtools.task.DartTask;
import devtools.task.GenPatchesTask;
import devtools.task.GenSourcesTask;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

@Getter
public class DartPlugin implements Plugin<Project> {
    private Project project;
    private DartExtension extension;
    private GitManager gitManager;
    @Setter
    private TinyMCP mcp;

    private File gitDir;
    private File patchDir;
    private File sourceDir;
    private File resourceDir;


    @Override
    public void apply(Project project) {
        this.project = project;
        System.setProperty("TINYMCP_CACHE", Constants.CACHE.resolve("tinymcp").toAbsolutePath().toString());

        extension = DartExtension.register(project);
        setupDirectories();

        gitManager = new GitManager(gitDir, patchDir);

        registerTasks();
    }

    private File file(String name) {
        return new File(project.getProjectDir(), name);
    }

    private void setupDirectories() {
        gitDir = file("src");

        patchDir = file("patches");
        if (!patchDir.exists()) patchDir.mkdirs();

        sourceDir = file("src/main/java");
        resourceDir = file("src/main/resources");
    }

    private void registerTasks() {
        DartTask.register(this, "genSources", GenSourcesTask.class, task -> {
            task.serverVersion = extension.getServerVersion();
            task.sourcesDir = sourceDir;
            task.resourcesDir = resourceDir;
        });
        DartTask.register(this, "genPatches", GenPatchesTask.class);
        DartTask.register(this, "applyPatches", ApplyPatchesTask.class);
    }
}
