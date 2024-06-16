package net.flower.api.event.user;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.util.Param;
import net.flower.api.model.user.User;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a user is loaded into memory from the storage.
 */
public interface UserLoadEvent extends FlowerEvent {

    /**
     * Gets the user that was loaded
     *
     * @return the user that was loaded
     */
    @Param(0)
    @NotNull User getUser();

}