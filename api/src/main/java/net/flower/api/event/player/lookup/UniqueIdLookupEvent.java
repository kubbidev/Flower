package net.flower.api.event.player.lookup;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.type.ResultEvent;
import net.flower.api.event.util.Param;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Called when the platform needs a unique id for a given username.
 */
public interface UniqueIdLookupEvent extends FlowerEvent, ResultEvent<UUID> {

    /**
     * Gets the username being looked up.
     *
     * @return the username
     */
    @Param(0)
    @NotNull String getUsername();

    /**
     * Sets the result unique id.
     *
     * @param uniqueId the unique id
     */
    default void setUniqueId(@Nullable UUID uniqueId) {
        result().set(uniqueId);
    }

}