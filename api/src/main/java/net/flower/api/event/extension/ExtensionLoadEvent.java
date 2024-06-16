package net.flower.api.event.extension;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.util.Param;
import net.flower.api.extension.Extension;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an {@link Extension} is loaded.
 */
public interface ExtensionLoadEvent extends FlowerEvent {

    /**
     * Gets the extension that was loaded.
     *
     * @return the extension
     */
    @Param(0)
    @NotNull Extension getExtension();

}