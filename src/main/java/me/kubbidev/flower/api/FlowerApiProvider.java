package me.kubbidev.flower.api;

import me.kubbidev.flower.api.implementation.ApiMessagingService;
import me.kubbidev.flower.api.implementation.ApiPlatform;
import me.kubbidev.flower.api.implementation.ApiUserManager;
import me.kubbidev.flower.config.ConfigKeys;
import me.kubbidev.flower.messaging.FlowerMessagingService;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.plugin.bootstrap.BootstrappedWithLoader;
import me.kubbidev.flower.plugin.bootstrap.FlowerBootstrap;
import me.kubbidev.flower.plugin.logging.PluginLogger;
import net.flower.api.Flower;
import net.flower.api.FlowerProvider;
import net.flower.api.event.EventBus;

import net.flower.api.messaging.MessagingService;
import net.flower.api.messenger.MessengerProvider;
import net.flower.api.model.user.UserManager;
import net.flower.api.platform.Health;
import net.flower.api.platform.Platform;
import net.flower.api.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Implements the Flower API using the plugin instance
 */
public class FlowerApiProvider implements Flower {

    private final FlowerPlugin plugin;

    private final ApiPlatform platform;
    private final UserManager userManager;

    public FlowerApiProvider(FlowerPlugin plugin) {
        this.plugin = plugin;

        this.platform = new ApiPlatform(plugin);
        this.userManager = new ApiUserManager(plugin, plugin.getUserManager());
    }

    public void ensureApiWasLoadedByPlugin() {
        FlowerBootstrap bootstrap = this.plugin.getBootstrap();
        ClassLoader pluginClassLoader;
        if (bootstrap instanceof BootstrappedWithLoader) {
            pluginClassLoader = ((BootstrappedWithLoader) bootstrap).getLoader().getClass().getClassLoader();
        } else {
            pluginClassLoader = bootstrap.getClass().getClassLoader();
        }

        for (Class<?> apiClass : new Class[]{Flower.class, FlowerProvider.class}) {
            ClassLoader apiClassLoader = apiClass.getClassLoader();

            if (!apiClassLoader.equals(pluginClassLoader)) {
                String guilty = "unknown";
                try {
                    guilty = bootstrap.identifyClassLoader(apiClassLoader);
                } catch (Exception e) {
                    // ignore
                }

                PluginLogger logger = this.plugin.getLogger();
                logger.warn("It seems that the Flower API has been (class)loaded by a plugin other than Flower!");
                logger.warn("The API was loaded by " + apiClassLoader + " (" + guilty + ") and the " +
                        "Flower plugin was loaded by " + pluginClassLoader.toString() + ".");
                logger.warn("This indicates that the other plugin has incorrectly \"shaded\" the " +
                        "Flower API into its jar file. This can cause errors at runtime and should be fixed.");
                return;
            }
        }
    }

    @Override
    public @NotNull UserManager getUserManager() {
        return this.userManager;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return this.platform;
    }

    @Override
    public @NotNull PluginMetadata getPluginMetadata() {
        return this.platform;
    }

    @Override
    public @NotNull EventBus getEventBus() {
        return  this.plugin.getEventDispatcher().getEventBus();
    }

    @Override
    public @NotNull Optional<MessagingService> getMessagingService() {
        return this.plugin.getMessagingService().map(ApiMessagingService::new);
    }

    @Override
    public @NotNull CompletableFuture<Void> runUpdateTask() {
        return this.plugin.getSyncTaskBuffer().request();
    }

    @Override
    public @NotNull Health runHealthCheck() {
        return this.plugin.runHealthCheck();
    }

    @Override
    public void registerMessengerProvider(@NotNull MessengerProvider messengerProvider) {
        if (this.plugin.getConfiguration().get(ConfigKeys.MESSAGING_SERVICE).equals("custom")) {
            this.plugin.setMessagingService(new FlowerMessagingService(this.plugin, messengerProvider));
        }
    }
}