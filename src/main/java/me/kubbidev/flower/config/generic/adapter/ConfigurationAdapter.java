package me.kubbidev.flower.config.generic.adapter;

import me.kubbidev.flower.plugin.FlowerPlugin;

import java.util.List;
import java.util.Map;

public interface ConfigurationAdapter {

    FlowerPlugin getPlugin();

    void reload();

    String getString(String path, String def);

    int getInteger(String path, int def);

    boolean getBoolean(String path, boolean def);

    List<String> getStringList(String path, List<String> def);

    Map<String, String> getStringMap(String path, Map<String, String> def);

}