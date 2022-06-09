package devtools.task;

import devtools.DartPlugin;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.util.function.Consumer;

public abstract class DartTask extends DefaultTask {
    protected DartPlugin plugin;

    public static <T extends DartTask> void register(DartPlugin plugin, String taskName, Class<T> taskClass, Consumer<T> configConsumer) {
        plugin.getProject().getTasks().register(taskName, taskClass, task -> {
            task.plugin = plugin;
            configConsumer.accept(task);
        });
    }

    public static <T extends DartTask> void register(DartPlugin plugin, String taskName, Class<T> taskClass) {
        register(plugin, taskName, taskClass, ignored -> {});
    }


    @TaskAction
    abstract public void run();
}
