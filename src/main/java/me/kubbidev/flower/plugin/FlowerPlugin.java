package me.kubbidev.flower.plugin;

import me.kubbidev.flower.api.FlowerApiProvider;
import me.kubbidev.flower.command.CommandManager;
import me.kubbidev.flower.command.abstraction.Command;
import me.kubbidev.flower.config.FlowerConfiguration;
import me.kubbidev.flower.dependencies.DependencyManager;
import me.kubbidev.flower.event.EventDispatcher;
import me.kubbidev.flower.extension.SimpleExtensionManager;
import me.kubbidev.flower.locale.TranslationManager;
import me.kubbidev.flower.messaging.InternalMessagingService;
import me.kubbidev.flower.model.User;
import me.kubbidev.flower.model.manager.user.UserManager;
import me.kubbidev.flower.plugin.bootstrap.FlowerBootstrap;
import me.kubbidev.flower.plugin.logging.PluginLogger;
import me.kubbidev.flower.plugin.util.AbstractConnectionListener;
import me.kubbidev.flower.sender.Sender;
import me.kubbidev.flower.storage.Storage;
import me.kubbidev.flower.storage.implementation.file.watcher.FileWatcher;
import me.kubbidev.flower.tasks.SyncTask;
import net.flower.api.platform.Health;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Main internal interface for Flower plugins, providing the base for
 * abstraction throughout the project.
 * <p>
 * All plugin platforms implement this interface.
 */
public interface FlowerPlugin {

    /**
     * Gets the bootstrap plugin instance
     *
     * @return the bootstrap plugin
     */
    FlowerBootstrap getBootstrap();

    /**
     * Gets the user manager instance for the platform
     *
     * @return the user manager
     */
    UserManager<? extends User> getUserManager();

    /**
     * Gets the plugin's configuration
     *
     * @return the plugin config
     */
    FlowerConfiguration getConfiguration();

    /**
     * Gets the primary data storage instance. This is likely to be wrapped with extra layers for caching, etc.
     *
     * @return the storage handler instance
     */
    Storage getStorage();

    /**
     * Gets a wrapped logger instance for the platform.
     *
     * @return the plugin's logger
     */
    PluginLogger getLogger();

    /**
     * Gets the messaging service.
     *
     * @return the messaging service
     */
    Optional<InternalMessagingService> getMessagingService();

    /**
     * Sets the messaging service.
     *
     * @param service the service
     */
    void setMessagingService(InternalMessagingService service);

    /**
     * Gets the event dispatcher
     *
     * @return the event dispatcher
     */
    EventDispatcher getEventDispatcher();

    /**
     * Returns the class implementing the FlowerAPI on this platform.
     *
     * @return the api
     */
    FlowerApiProvider getApiProvider();

    /**
     * Gets the extension manager.
     *
     * @return the extension manager
     */
    SimpleExtensionManager getExtensionManager();

    /**
     * Gets the command manager
     *
     * @return the command manager
     */
    CommandManager getCommandManager();

    /**
     * Gets the connection listener.
     *
     * @return the connection listener
     */
    AbstractConnectionListener getConnectionListener();

    /**
     * Gets the instance providing locale translations for the plugin
     *
     * @return the translation manager
     */
    TranslationManager getTranslationManager();

    /**
     * Gets the dependency manager for the plugin
     *
     * @return the dependency manager
     */
    DependencyManager getDependencyManager();

    /**
     * Gets the file watcher running on the platform
     *
     * @return the file watcher
     */
    Optional<FileWatcher> getFileWatcher();

    /**
     * Runs a health check for the plugin.
     *
     * @return the result of the healthcheck
     */
    Health runHealthCheck();

    /**
     * Lookup a uuid from a username.
     *
     * @param username the username to lookup
     * @return an optional uuid, if found
     */
    Optional<UUID> lookupUniqueId(String username);

    /**
     * Lookup a username from a uuid.
     *
     * @param uniqueId the uuid to lookup
     * @return an optional username, if found
     */
    Optional<String> lookupUsername(UUID uniqueId);

    /**
     * Tests whether the given username is valid.
     *
     * @param username the username
     * @return true if valid
     */
    boolean testUsernameValidity(String username);

    /**
     * Gets a list of online Senders on the platform
     *
     * @return a {@link List} of senders online on the platform
     */
    Stream<Sender> getOnlineSenders();

    /**
     * Gets the console.
     *
     * @return the console sender of the instance
     */
    Sender getConsoleSender();

    default List<Command<?>> getExtraCommands() {
        return Collections.emptyList();
    }

    /**
     * Gets the sync task buffer of the platform, used for scheduling and running sync tasks.
     *
     * @return the sync task buffer instance
     */
    SyncTask.Buffer getSyncTaskBuffer();

    /**
     * Called at the end of the sync task.
     */
    default void performPlatformDataSync() {

    }
}
