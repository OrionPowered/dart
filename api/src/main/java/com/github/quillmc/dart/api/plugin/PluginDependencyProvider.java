package com.github.quillmc.dart.api.plugin;

import com.alexsobiek.nexus.inject.dependency.DependencyProvider;
import com.github.quillmc.dart.api.Server;
import org.slf4j.Logger;

import java.io.File;
import java.util.function.Supplier;

public class PluginDependencyProvider extends DependencyProvider {
    public PluginDependencyProvider(Server<?> server) {
        supply(Server.class, () -> server);
    }

    public DependencyProvider forPlugin(File dataFolder, Logger logger) {
        SinglePluginDependencyProvider provider = new SinglePluginDependencyProvider(this) {
        };
        provider.supply(File.class, "dataFolder", () -> dataFolder);
        provider.supply(Logger.class, () -> logger);
        return provider;
    }

    abstract static class SinglePluginDependencyProvider extends DependencyProvider {
        public SinglePluginDependencyProvider(PluginDependencyProvider prov) {
            append(prov);
        }

        @Override
        protected <T> void supply(Class<T> _class, Supplier<T> supplier) {
            super.supply(_class, supplier);
        }

        @Override
        protected <T> void supply(Class<T> _class, String identifier, Supplier<T> supplier) {
            super.supply(_class, identifier, supplier);
        }
    }
}
