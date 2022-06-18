package com.github.quillmc.dart.plugin.test;

import com.alexsobiek.nexus.plugin.annotation.Plugin;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.plugin.DartPlugin;

@Plugin(
        name = "Test Plugin",
        description = "Test plugin",
        version = "1.0",
        authors = {"Dart Developers"}
)
public class TestPlugin extends DartPlugin<Server.All> {


    @Override
    public void onReady() {
        logger.info("Plugin ready");
    }

    @Override
    public void onEnable() {
        logger.info("Plugin enabled");
    }

    @Override
    public void onReload() {
        logger.info("Plugin reloaded");
    }

    @Override
    public void onDisable() {
        logger.info("Plugin disabled");
    }
}
