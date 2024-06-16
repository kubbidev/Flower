package me.kubbidev.flower.storage.implementation.custom;

import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.storage.implementation.StorageImplementation;

/**
 * A storage provider
 */
@FunctionalInterface
public interface CustomStorageProvider {

    StorageImplementation provide(FlowerPlugin plugin);

}