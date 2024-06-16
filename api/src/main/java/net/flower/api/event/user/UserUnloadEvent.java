package net.flower.api.event.user;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.type.Cancellable;
import net.flower.api.event.util.Param;
import net.flower.api.model.user.User;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a user is about to be unloaded from memory.
 */
public interface UserUnloadEvent extends FlowerEvent, Cancellable {

    /**
     * Gets the user that is being unloaded
     *
     * @return the user that is being unloaded
     */
    @Param(0)
    @NotNull User getUser();
}