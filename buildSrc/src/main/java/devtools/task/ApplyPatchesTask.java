package devtools.task;

import devtools.task.DartTask;

public class ApplyPatchesTask extends DartTask {
    @Override
    public void run() {
        plugin.getGitManager().applyPatches();
    }
}
