package com.github.quillmc.dart.api.plugin;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.github.quillmc.dart.api.exception.InvalidPluginException;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

public class PluginDescription {
    private final String name;
    private final String mainClass;
    private final String author;

    public PluginDescription(JarFile file) throws InvalidPluginException {
        String fileName = "plugin.toml";
        try (InputStream is = file.getInputStream(file.getEntry(fileName))) {
            if (is == null) throw new InvalidPluginException("Missing " + fileName);

            // TODO: find another solution for this. Using night-config requires the file to be first written
            // somewhere outside the jar, it can't simply load contents from an InputStream.

            CommentedFileConfig config = CommentedFileConfig.builder(fileName)
                    .onFileNotFound(FileNotFoundAction.copyData(is))
                    .autosave()
                    .build();
            config.load();

            name = config.get("name");
            author = config.get("author");
            mainClass = config.get("main");

            config.close();
        } catch (IOException e) {
            throw new InvalidPluginException("Failed to open " + fileName, e);
        }
    }

    public String getName() {
        return name;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getAuthor() {
        return author;
    }


}
