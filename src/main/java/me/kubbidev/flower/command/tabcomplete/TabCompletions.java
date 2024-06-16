package me.kubbidev.flower.command.tabcomplete;

import me.kubbidev.flower.plugin.FlowerPlugin;

/**
 * Common completion suppliers used by the plugin
 */
public final class TabCompletions {

    private static final CompletionSupplier BOOLEAN = CompletionSupplier.startsWith("true", "false");

    // bit of a weird pattern, but meh it kinda works, reduces the boilerplate
    // of calling the commandmanager + tabcompletions getters every time


    public TabCompletions(FlowerPlugin plugin) {
    }

    public static CompletionSupplier booleans() {
        return BOOLEAN;
    }
}