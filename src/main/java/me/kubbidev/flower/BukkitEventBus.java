package me.kubbidev.flower;

import me.kubbidev.flower.api.FlowerApiProvider;
import me.kubbidev.flower.event.AbstractEventBus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class BukkitEventBus extends AbstractEventBus<Plugin> implements Listener {
    public BukkitEventBus(FBukkitPlugin plugin, FlowerApiProvider apiProvider) {
        super(plugin, apiProvider);

        // register listener
        FBukkitBootstrap bootstrap = plugin.getBootstrap();
        bootstrap.getServer().getPluginManager().registerEvents(this, bootstrap.getLoader());
    }

    @Override
    protected Plugin checkPlugin(Object plugin) throws IllegalArgumentException {
        if (plugin instanceof Plugin) {
            return (Plugin) plugin;
        }

        throw new IllegalArgumentException("Object " + plugin + " (" + plugin.getClass().getName() + ") is not a plugin.");
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        Plugin plugin = e.getPlugin();
        unregisterHandlers(plugin);
    }

}