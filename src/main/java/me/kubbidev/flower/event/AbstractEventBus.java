package me.kubbidev.flower.event;

import me.kubbidev.flower.api.FlowerApiProvider;
import me.kubbidev.flower.plugin.FlowerPlugin;
import net.flower.api.event.EventBus;
import net.flower.api.event.EventSubscription;
import net.flower.api.event.FlowerEvent;
import net.kyori.event.EventSubscriber;
import net.kyori.event.SimpleEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractEventBus<P> implements EventBus, AutoCloseable {

    /**
     * The plugin instance
     */
    private final FlowerPlugin plugin;

    /**
     * The api provider instance
     */
    private final FlowerApiProvider apiProvider;

    /**
     * The delegate event bus
     */
    private final Bus bus = new Bus();

    protected AbstractEventBus(FlowerPlugin plugin, FlowerApiProvider apiProvider) {
        this.plugin = plugin;
        this.apiProvider = apiProvider;
    }

    public FlowerPlugin getPlugin() {
        return this.plugin;
    }

    public FlowerApiProvider getApiProvider() {
        return this.apiProvider;
    }

    /**
     * Checks that the given plugin object is a valid plugin instance for the platform
     *
     * @param plugin the object
     * @return a plugin
     * @throws IllegalArgumentException if the plugin is invalid
     */
    protected abstract P checkPlugin(Object plugin) throws IllegalArgumentException;

    public void post(FlowerEvent event) {
        this.bus.post(event);
    }

    public boolean shouldPost(Class<? extends FlowerEvent> eventClass) {
        return this.bus.hasSubscribers(eventClass);
    }

    public void subscribe(FlowerEventListener listener) {
        listener.bind(this);
    }

    @Override
    public <T extends FlowerEvent> @NotNull EventSubscription<T> subscribe(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler) {
        Objects.requireNonNull(eventClass, "eventClass");
        Objects.requireNonNull(handler, "handler");
        return registerSubscription(eventClass, handler, null);
    }

    @Override
    public <T extends FlowerEvent> @NotNull EventSubscription<T> subscribe(Object plugin, @NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(eventClass, "eventClass");
        Objects.requireNonNull(handler, "handler");
        return registerSubscription(eventClass, handler, checkPlugin(plugin));
    }

    private <T extends FlowerEvent> EventSubscription<T> registerSubscription(Class<T> eventClass, Consumer<? super T> handler, Object plugin) {
        if (!eventClass.isInterface()) {
            throw new IllegalArgumentException("class " + eventClass + " is not an interface");
        }
        if (!FlowerEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException("class " + eventClass.getName() + " does not implement FlowerEvent");
        }

        FlowerEventSubscription<T> eventHandler = new FlowerEventSubscription<>(this, eventClass, handler, plugin);
        this.bus.register(eventClass, eventHandler);

        return eventHandler;
    }

    @Override
    public <T extends FlowerEvent> @NotNull Set<EventSubscription<T>> getSubscriptions(@NotNull Class<T> eventClass) {
        return this.bus.getHandlers(eventClass);
    }

    /**
     * Removes a specific handler from the bus
     *
     * @param handler the handler to remove
     */
    public void unregisterHandler(FlowerEventSubscription<?> handler) {
        this.bus.unregister(handler);
    }

    /**
     * Removes all handlers for a specific plugin
     *
     * @param plugin the plugin
     */
    protected void unregisterHandlers(P plugin) {
        this.bus.unregister(sub -> ((FlowerEventSubscription<?>) sub).getPlugin() == plugin);
    }

    @Override
    public void close() {
        this.bus.unregisterAll();
    }

    private static final class Bus extends SimpleEventBus<FlowerEvent> {
        Bus() {
            super(FlowerEvent.class);
        }

        @Override
        protected boolean shouldPost(@NotNull FlowerEvent event, @NotNull EventSubscriber<?> subscriber) {
            return true;
        }

        public <T extends FlowerEvent> Set<EventSubscription<T>> getHandlers(Class<T> eventClass) {
            //noinspection unchecked
            return super.subscribers().values().stream()
                    .filter(s -> s instanceof EventSubscription && ((EventSubscription<?>) s).getEventClass().isAssignableFrom(eventClass))
                    .map(s -> (EventSubscription<T>) s)
                    .collect(Collectors.toSet());
        }
    }
}