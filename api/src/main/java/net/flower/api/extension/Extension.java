package net.flower.api.extension;

import net.flower.api.Flower;

/**
 * Represents a simple extension "plugin" for Flower.
 *
 * <p>Yes, that's right. A plugin for a plugin.</p>
 *
 * <p>Extensions should either declare a no-arg constructor, or a constructor
 * that accepts a single {@link Flower} parameter as it's only argument.</p>
 */
public interface Extension {

    /**
     * Loads the extension.
     */
    void load();

    /**
     * Unloads the extension.
     */
    void unload();

}