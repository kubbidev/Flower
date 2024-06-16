package me.kubbidev.flower.plugin.bootstrap;

/**
 * A {@link FlowerBootstrap} that was bootstrapped by a loader.
 */
public interface BootstrappedWithLoader {

    /**
     * Gets the loader object that did the bootstrapping.
     *
     * @return the loader
     */
    Object getLoader();
}