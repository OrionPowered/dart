package devtools;

import com.alexsobiek.async.util.Lazy;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gradle.api.GradleException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class GitPatch {

    private static final Lazy<File> gitDir = new Lazy<>(() -> new File(DartPlugin.getProjectDir(), "src"));

    private static final Lazy<Git> git = new Lazy<>(() -> {
        try {
            return Git.open(gitDir.get());
        } catch (IOException e) {
            throw new GradleException("Failed loading Git repository", e);
        }
    });

    private static final Lazy<RevCommit> firstCommit = new Lazy<>(() -> {
        try {
            Iterator<RevCommit> commits = git.get().log().call().iterator();
            RevCommit commit = commits.next();
            while (commits.hasNext()) commit = commits.next(); // get first commit

            return commit;
        } catch (GitAPIException e) {
            throw new GradleException("Failed looking up first commit", e);
        }
    });

    public static Git git() {
        return git.get();
    }

    public static void setupRepository() {
        try {
            Git git = Git.init().setDirectory(gitDir.get()).call();
            git.add().addFilepattern(".").call();
            git.commit()
                    .setAuthor("Mojang", "steve@minecraft.net")
                    .setMessage(String.format("Vanilla source"))
                    .setSign(false)
                    .call();
            git.close();
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to setup local git repository", e);
        }
    }

    public static void generate() {
        File patchDir = DartPlugin.getPatchDir();
        RevCommit commit = firstCommit.get();

        try {
            ProcessBuilder builder = new ProcessBuilder("git", "format-patch", commit.getName(), "-o", patchDir.getAbsolutePath());
            process(builder);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed generating patches", e);
        }
    }

    public static void apply() {
        Arrays.stream(Objects.requireNonNull(DartPlugin.getPatchDir().listFiles())).filter(f -> f.getName().endsWith(".patch")).forEach(patch -> {
            try {
                ProcessBuilder builder = new ProcessBuilder("git", "am", patch.getAbsolutePath());
                process(builder);
            } catch(IOException | InterruptedException e) {
                throw new RuntimeException("Failed to apply patch" + patch.getName(), e);
            }
        });
    }

    private static void process(ProcessBuilder builder) throws IOException, InterruptedException {
        builder.directory(DartPlugin.getProjectDir());
        builder.inheritIO();
        builder.redirectErrorStream(true);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process patchProcess = builder.start();
        patchProcess.waitFor();
    }
}
