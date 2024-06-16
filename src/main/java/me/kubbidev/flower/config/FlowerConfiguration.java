package me.kubbidev.flower.config;

import me.kubbidev.flower.config.generic.KeyedConfiguration;
import me.kubbidev.flower.config.generic.adapter.ConfigurationAdapter;
import me.kubbidev.flower.plugin.FlowerPlugin;

public class FlowerConfiguration extends KeyedConfiguration {
    private final FlowerPlugin plugin;

    public FlowerConfiguration(FlowerPlugin plugin, ConfigurationAdapter adapter) {
        super(adapter, ConfigKeys.getKeys());
        this.plugin = plugin;

        init();
    }

    @Override
    public void reload() {
        super.reload();
        getPlugin().getEventDispatcher().dispatchConfigReload();
    }

    public FlowerPlugin getPlugin() {
        return this.plugin;
    }
}
