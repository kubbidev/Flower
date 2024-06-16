package me.kubbidev.flower.config.generic.adapter;

import me.kubbidev.flower.config.ConfigKeys;
import me.kubbidev.flower.plugin.FlowerPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class EnvironmentVariableConfigAdapter extends StringBasedConfigurationAdapter {
    private static final String PREFIX = "FLOWER_";

    private final FlowerPlugin plugin;

    public EnvironmentVariableConfigAdapter(FlowerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected @Nullable String resolveValue(String path) {
        // e.g.
        // 'server'            -> FLOWER_SERVER
        // 'data.table_prefix' -> FLOWER_DATA_TABLE_PREFIX
        String key = PREFIX + path.toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace('.', '_');

        String value = System.getenv(key);
        if (value != null) {
            String printableValue = ConfigKeys.shouldCensorValue(path) ? "*****" : value;
            this.plugin.getLogger().info(String.format("Resolved configuration value from environment variable: %s = %s", key, printableValue));
        }
        return value;
    }

    @Override
    public FlowerPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void reload() {
        // no-op
    }
}