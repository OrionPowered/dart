package com.github.quillmc.dart.plugin;

import com.alexsobiek.nexus.plugin.PluginManager;
import com.alexsobiek.nexus.plugin.exception.InvalidPluginException;
import com.alexsobiek.nexus.plugin.loader.PluginContainer;
import com.github.quillmc.dart.DartServer;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.plugin.DartPlugin;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Getter
public class DartPluginManager extends PluginManager<DartPlugin<? extends Server<?>>> {
    private final Logger logger = LoggerFactory.getLogger("PluginManager");
    private final DartServer<?> server;
    private final PluginDependencyProvider dependencyProvider;
    private final File pluginsDir = new File("plugins");

    public DartPluginManager(DartServer<?> server) {
        this.server = server;
        this.dependencyProvider = new PluginDependencyProvider(server);
        if (!pluginsDir.exists()) pluginsDir.mkdirs();
    }

    @Override
    protected DartPlugin<? extends Server<?>> construct(PluginContainer<DartPlugin<? extends Server<?>>> container) {
        String name = container.getInfo().name().replaceAll("\\s+", "");
        logger.info("Loading plugin {} version {} by {}", name, container.getInfo().version(), container.getInfo().authors());
        Logger logger = LoggerFactory.getLogger(name);
        File dataFolder = new File(pluginsDir.getAbsolutePath(), name);
        DartPlugin<?> pl;
        try {
            Optional<DartPlugin<?>> opt = newInstance(container.getMainClass(), dependencyProvider.forPlugin(dataFolder, logger)).get();
            if (opt.isPresent()) pl = opt.get();
            else throw new InvalidPluginException("Failed to call main class " + container.getMainClass());
        } catch (InvalidPluginException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        pl.onReady();
        return pl;
    }

    @Override
    public void enableAll() {
        super.enableAll();
    }

    @Override
    public void reloadAll() {
        super.reloadAll();
    }

    @Override
    public void disableAll() {
        super.disableAll();
    }
}
