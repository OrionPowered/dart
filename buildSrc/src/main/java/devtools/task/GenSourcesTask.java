package devtools.task;

import com.github.quillmc.tinymcp.TinyMCP;
import com.github.quillmc.tinymcp.Version;
import devtools.DartPlugin;
import devtools.GitPatch;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.util.getopt.Options;
import org.benf.cfr.reader.util.getopt.OptionsImpl;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GenSourcesTask extends DefaultTask {
    public static void register(Project project) {
        project.getTasks().register("genSources", GenSourcesTask.class);
    }

    @TaskAction
    public void run() {
        Property<Version> version = DartPlugin.getExtension().getServerVersion();
        if (version.isPresent()) {
            Version v = version.get();
            System.out.println("Setting up development environment for Minecraft version " + v);

            TinyMCP mcp = v.server();
            DartPlugin.tinymcp.setIfAbsent(mcp);

            File jar = mcp.getMappedJar();

            File srcDir = DartPlugin.getSourceDir();
            if (srcDir.exists() || srcDir.mkdirs()) {
                // decompile sources
                Options options = OptionsImpl.getFactory().create(Map.of(
                        "renameillegalidents", "true",
                        "outputdir", srcDir.getAbsolutePath()
                ));

                CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(options).build();
                cfrDriver.analyse(List.of(jar.getAbsolutePath()));
                File cfrSummary = new File(srcDir, "summary.txt");
                if (cfrSummary.exists()) cfrSummary.delete();

                GitPatch.setupRepository();
                GitPatch.apply();
            } else throw new GradleException("Failed to setup project sources at " + srcDir);
        }
    }
}
