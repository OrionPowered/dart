package devtools.task;

import devtools.GitPatch;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class GenPatchesTask extends DefaultTask {
    public static void register(Project project) {
        project.getTasks().register("genPatches", GenPatchesTask.class);
    }

    @TaskAction
    public void run() {
        GitPatch.generate();
    }

}
