package me.kubbidev.flower.event.gen;

import net.flower.api.Flower;
import net.flower.api.event.FlowerEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

/**
 * Abstract implementation of {@link FlowerEvent}.
 */
public abstract class AbstractEvent implements FlowerEvent {
    private final Flower api;

    protected AbstractEvent(Flower api) {
        this.api = api;
    }

    @Override
    public @NotNull Flower getFlower() {
        return this.api;
    }

    // Overridden by the subclass. Used by GeneratedEventClass to get setter method handles.
    public MethodHandles.Lookup mhl() {
        throw new UnsupportedOperationException();
    }
}