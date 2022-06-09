package devtools.task;

import devtools.GitPatch;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class ApplyPatchesTask extends DefaultTask {
    public static void register(Project project) {
        project.getTasks().register("applyPatches", ApplyPatchesTask.class);
    }

    @TaskAction
    public void run() {
        System.out.println("Applying patches");
        GitPatch.apply();
    }
}
