package net.flower.api;

import net.flower.api.event.EventBus;
import net.flower.api.messaging.MessagingService;
import net.flower.api.messenger.MessengerProvider;
import net.flower.api.model.user.User;
import net.flower.api.model.user.UserManager;
import net.flower.api.platform.Health;
import net.flower.api.platform.Platform;
import net.flower.api.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The Flower API.
 *
 * <p>The API allows other plugins on the server to read and modify Flower
 * data, change behaviour of the plugin, listen to certain events, and integrate
 * Flower into other plugins and systems.</p>
 *
 * <p>This interface represents the base of the API package. All functions are
 * accessed via this interface.</p>
 *
 * <p>To start using the API, you need to obtain an instance of this interface.
 * These are registered by the Flower plugin to the platforms Services
 * Manager. This is the preferred method for obtaining an instance.</p>
 *
 * <p>For ease of use, and for platforms without a Service Manager, an instance
 * can also be obtained from the static singleton accessor in
 * {@link FlowerProvider}.</p>
 */
public interface Flower {

    /**
     * Gets the {@link UserManager}, responsible for managing
     * {@link User} instances.
     *
     * <p>This manager can be used to retrieve instances of {@link User} by uuid
     * or name, or query all loaded users.</p>
     *
     * @return the user manager
     */
    @NotNull UserManager getUserManager();

    /**
     * Gets the {@link Platform}, which represents the server platform the
     * plugin is running on.
     *
     * @return the platform
     */
    @NotNull Platform getPlatform();

    /**
     * Gets the {@link PluginMetadata}, responsible for providing metadata about
     * the Flower plugin currently running.
     *
     * @return the plugin metadata
     */
    @NotNull PluginMetadata getPluginMetadata();

    /**
     * Gets the {@link EventBus}, used for subscribing to internal Flower
     * events.
     *
     * @return the event bus
     */
    @NotNull EventBus getEventBus();

    /**
     * Gets the {@link MessagingService}, used to dispatch updates throughout a
     * network of servers running the plugin.
     *
     * <p>Not all instances of Flower will have a messaging service setup and
     * configured.</p>
     *
     * @return the messaging service instance, if present.
     */
    @NotNull Optional<MessagingService> getMessagingService();

    /**
     * Schedules the execution of an update task, and returns an encapsulation
     * of the task as a {@link CompletableFuture}.
     *
     * <p>The exact actions performed in an update task remains an
     * implementation detail of the plugin, however, as a minimum, it is
     * expected to perform a full reload of user data, and
     * ensure that any changes are fully applied and propagated.</p>
     *
     * @return a future
     */
    @NotNull CompletableFuture<Void> runUpdateTask();

    /**
     * Executes a health check.
     *
     * <p>This task checks if the Flower implementation is running and
     * whether it has a connection to the database (if applicable).</p>
     *
     * @return the health status
     */
    @NotNull Health runHealthCheck();

    /**
     * Registers a {@link MessengerProvider} for use by the platform.
     *
     * <p>Note that the mere action of registering a provider doesn't
     * necessarily mean that it will be used.</p>
     *
     * @param messengerProvider the messenger provider.
     */
    void registerMessengerProvider(@NotNull MessengerProvider messengerProvider);
}
