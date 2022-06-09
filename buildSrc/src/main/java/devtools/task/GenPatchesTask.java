package devtools.task;

import devtools.task.DartTask;

public class GenPatchesTask extends DartTask {
    @Override
    public void run() {
        plugin.getGitManager().generatePatches();
    }
}
