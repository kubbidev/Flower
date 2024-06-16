package net.flower.api.event;

import net.flower.api.Flower;
import org.jetbrains.annotations.NotNull;

/**
 * A superinterface for all Flower events.
 */
public interface FlowerEvent {

    /**
     * Get the API instance this event was dispatched from
     *
     * @return the api instance
     */
    @NotNull
    Flower getFlower();

    /**
     * Gets the type of the event.
     *
     * @return the type of the event
     */
    @NotNull Class<? extends FlowerEvent> getEventType();

}