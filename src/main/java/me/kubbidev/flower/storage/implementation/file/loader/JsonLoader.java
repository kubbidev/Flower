package me.kubbidev.flower.storage.implementation.file.loader;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonLoader implements ConfigurateLoader {

    @Override
    public ConfigurationLoader<? extends ConfigurationNode> loader(Path path) {
        return GsonConfigurationLoader.builder()
                .setIndent(2)
                .setSource(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8))
                .setSink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8))
                .build();
    }
}