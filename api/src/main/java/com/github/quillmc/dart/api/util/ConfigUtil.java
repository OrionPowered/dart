package com.github.quillmc.dart.api.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConfigUtil {
    public static CommentedFileConfig getConfig(Class<?> _class, File file, String name) {
        try (InputStream is = _class.getResourceAsStream((name.startsWith("/") ? name : "/" + name))) {
            CommentedFileConfig config = CommentedFileConfig.builder(file)
                    .onFileNotFound(FileNotFoundAction.copyData(is))
                    .autosave()
                    .build();
            config.load();
            is.close();
            return config;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
