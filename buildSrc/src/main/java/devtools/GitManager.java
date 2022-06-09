package devtools;

import com.alexsobiek.async.util.Lazy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gradle.api.GradleException;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;


@RequiredArgsConstructor
public class GitManager {
    @Getter
    private final File directory;
    @Getter
    private final File patchDirectory;

    private final Lazy<Git> git = new Lazy<>(() -> {
        try {
            return Git.open(getDirectory());
        } catch (IOException e) {
            throw new GradleException("Failed loading Git repository", e);
        }
    });

    private final Lazy<RevCommit> firstCommit = new Lazy<>(() -> {
        Iterator<RevCommit> commits = iterator();
        RevCommit commit = commits.next();
        while (commits.hasNext()) commit = commits.next(); // get first commit
        return commit;
    });

    public Git git() {
        return git.get();
    }

    private Iterator<RevCommit> iterator() {
        try {
            return git.get().log().call().iterator();
        } catch (GitAPIException e) {
            throw new GradleException("Failed getting commit iterator", e);
        }
    }

    private void forEachCommit(Consumer<RevCommit> commit) {
        iterator().forEachRemaining(commit);
    }

    public void setupGit() {
        try {
            Git git = Git.init().setDirectory(directory).call();
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
        applyPatches();
    }

    public void applyPatches() {
        File failed = Paths.get(directory.getAbsolutePath(), ".git", "patch-apply-failed").toFile();
        if (failed.exists())
            if (!failed.delete()) throw new RuntimeException("Failed to delete .git/patch-apply-failed");

        try {
            process(new ProcessBuilder("git", "am", "--abort"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Arrays.stream(Objects.requireNonNull(patchDirectory.listFiles())).filter(f -> f.getName().endsWith(".patch")).sorted().forEach(patch -> {
            //
            try {
                System.out.printf("Applying patch %s%n",patch.getName());
                System.out.print("STATUS: ");
                Process checkProcess = process(new ProcessBuilder("git", "apply", "--check", patch.getAbsolutePath()));
                if (checkProcess.exitValue() == 0) {
                    System.out.print("Applying...");
                    Process amProcess = process(new ProcessBuilder("git", "am", "--3way", "--ignore-whitespace", patch.getAbsolutePath()));
                    if (amProcess.exitValue() == 0) System.out.printf("Applied%n");
                    else System.out.printf("Failed%n");
                } else System.out.printf("Skipped%n");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to apply patch" + patch.getName(), e);
            }
        });
    }

    public void generatePatches() {
        RevCommit commit = firstCommit.get();
        try {
            ProcessBuilder builder = new ProcessBuilder("git", "format-patch", "-N", commit.getName(), "-o", patchDirectory.getAbsolutePath());
            process(builder);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed generating patches", e);
        }
    }

    private Process process(ProcessBuilder builder) throws IOException, InterruptedException {
        builder.directory(directory);
        builder.inheritIO();
        builder.redirectErrorStream(true);
        Process process = builder.start();
        process.waitFor();
        return process;
    }

    public String patchHash(File patch) throws IOException {
        byte[] buffer = new byte[40];
        InputStream is = new FileInputStream(patch);
        is.skipNBytes(5); // Skip "From "
        is.read(buffer);
        is.close();
        return new String(buffer);
    }
}
