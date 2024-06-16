package net.flower.api.event.player.lookup;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.util.Param;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Called when the validity of a username is being tested.
 */
public interface UsernameValidityCheckEvent extends FlowerEvent {

    /**
     * Gets the username being tested.
     *
     * @return the username
     */
    @Param(0)
    @NotNull String getUsername();

    /**
     * Gets the current validity state for the username.
     *
     * @return the validity state
     */
    @Param(1)
    @NotNull AtomicBoolean validityState();

    /**
     * Gets if the username is currently considered to be valid.
     *
     * @return if the username is valid
     */
    default boolean isValid() {
        return validityState().get();
    }

    /**
     * Sets if the username should be considered valid or not.
     *
     * @param valid whether the username is valid
     */
    default void setValid(boolean valid) {
        validityState().set(valid);
    }

}