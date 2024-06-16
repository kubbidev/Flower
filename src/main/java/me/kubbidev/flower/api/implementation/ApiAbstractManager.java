package me.kubbidev.flower.api.implementation;

import me.kubbidev.flower.plugin.FlowerPlugin;

public abstract class ApiAbstractManager<I, E, H> {
    protected final FlowerPlugin plugin;
    protected final H handle;

    protected ApiAbstractManager(FlowerPlugin plugin, H handle) {
        this.plugin = plugin;
        this.handle = handle;
    }

    protected abstract E proxy(I internal);

}