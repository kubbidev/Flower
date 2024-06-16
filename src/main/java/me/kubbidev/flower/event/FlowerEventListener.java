package me.kubbidev.flower.event;

import net.flower.api.event.EventBus;
import net.flower.api.event.FlowerEvent;

/**
 * Defines a class which listens to {@link FlowerEvent}s.
 */
public interface FlowerEventListener {

    void bind(EventBus bus);

}