package me.kubbidev.flower;

import me.kubbidev.flower.api.FlowerApiProvider;
import me.kubbidev.flower.brigadier.FlowerBrigadier;
import me.kubbidev.flower.config.ConfigKeys;
import me.kubbidev.flower.config.generic.adapter.ConfigurationAdapter;
import me.kubbidev.flower.dependencies.Dependency;
import me.kubbidev.flower.event.AbstractEventBus;
import me.kubbidev.flower.listeners.BukkitConnectionListener;
import me.kubbidev.flower.messaging.MessagingFactory;
import me.kubbidev.flower.model.manager.user.StandardUserManager;
import me.kubbidev.flower.plugin.AbstractFlowerPlugin;
import me.kubbidev.flower.plugin.util.AbstractConnectionListener;
import me.kubbidev.flower.sender.Sender;
import net.flower.api.Flower;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Flower implementation for the Bukkit API.
 */
public class FBukkitPlugin extends AbstractFlowerPlugin {
    private final FBukkitBootstrap bootstrap;

    private BukkitSenderFactory senderFactory;
    private BukkitConnectionListener connectionListener;
    private BukkitCommandExecutor commandManager;
    private StandardUserManager userManager;

    public FBukkitPlugin(FBukkitBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public FBukkitBootstrap getBootstrap() {
        return this.bootstrap;
    }

    public JavaPlugin getLoader() {
        return this.bootstrap.getLoader();
    }

    @Override
    protected void setupSenderFactory() {
        this.senderFactory = new BukkitSenderFactory(this);
    }

    @Override
    protected Set<Dependency> getGlobalDependencies() {
        Set<Dependency> dependencies = super.getGlobalDependencies();
        if (isBrigadierSupported()) {
            dependencies.add(Dependency.COMMODORE);
            dependencies.add(Dependency.COMMODORE_FILE);
        }
        return dependencies;
    }

    @Override
    protected ConfigurationAdapter provideConfigurationAdapter() {
        return new BukkitConfigAdapter(this, resolveConfig("config.yml").toFile());
    }

    @Override
    protected void registerPlatformListeners() {
        this.connectionListener = new BukkitConnectionListener(this);
        this.bootstrap.getServer().getPluginManager().registerEvents(this.connectionListener, this.bootstrap.getLoader());
    }

    @Override
    protected MessagingFactory<?> provideMessagingFactory() {
        return new BukkitMessagingFactory(this);
    }

    @Override
    protected void registerCommands() {
        PluginCommand command = this.bootstrap.getLoader().getCommand("flower");
        if (command == null) {
            getLogger().severe("Unable to register /flower command with the server");
            return;
        }

        if (isAsyncTabCompleteSupported()) {
            this.commandManager = new BukkitAsyncCommandExecutor(this, command);
        } else {
            this.commandManager = new BukkitCommandExecutor(this, command);
        }

        this.commandManager.register();

        // setup brigadier
        if (isBrigadierSupported() && getConfiguration().get(ConfigKeys.REGISTER_COMMAND_LIST_DATA)) {
            try {
                FlowerBrigadier.register(this, command);
            } catch (Exception e) {
                if (!(e instanceof RuntimeException && e.getMessage().contains("not supported by the server"))) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void setupManagers() {
        this.userManager = new StandardUserManager(this);
    }

    @Override
    protected void setupPlatformHooks() {

    }

    @Override
    protected AbstractEventBus<?> provideEventBus(FlowerApiProvider apiProvider) {
        return new BukkitEventBus(this, apiProvider);
    }

    @Override
    protected void registerApiOnPlatform(Flower api) {
        this.bootstrap.getServer().getServicesManager().register(Flower.class, api, this.bootstrap.getLoader(), ServicePriority.Normal);
    }

    @Override
    protected void performFinalSetup() {
        // Load any online users (in the case of a reload)
        for (Player player : this.bootstrap.getServer().getOnlinePlayers()) {
            this.bootstrap.getScheduler().executeAsync(() -> {
                try {
                    this.connectionListener.loadUser(player.getUniqueId(), player.getName());
                } catch (Exception e) {
                    getLogger().severe("Exception occurred whilst loading data for " +
                            player.getUniqueId() + " - " + player.getName(), e);
                }
            });
        }
    }

    @Override
    protected void removePlatformHooks() {
        // Unload players
        for (Player player : this.bootstrap.getServer().getOnlinePlayers()) {
            getUserManager().unload(player.getUniqueId());
        }
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean isBrigadierSupported() {
        return classExists("com.mojang.brigadier.CommandDispatcher");
    }

    private static boolean isAsyncTabCompleteSupported() {
        return classExists("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
    }

    @Override
    public Stream<Sender> getOnlineSenders() {
        List<Player> players = new ArrayList<>(this.bootstrap.getServer().getOnlinePlayers());
        return Stream.concat(
                Stream.of(getConsoleSender()),
                players.stream().map(p -> getSenderFactory().wrap(p))
        );
    }

    @Override
    public Sender getConsoleSender() {
        return getSenderFactory().wrap(this.bootstrap.getConsole());
    }

    public BukkitSenderFactory getSenderFactory() {
        return this.senderFactory;
    }

    @Override
    public AbstractConnectionListener getConnectionListener() {
        return this.connectionListener;
    }

    @Override
    public BukkitCommandExecutor getCommandManager() {
        return this.commandManager;
    }

    @Override
    public StandardUserManager getUserManager() {
        return this.userManager;
    }
}