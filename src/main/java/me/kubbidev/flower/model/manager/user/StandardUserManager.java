package me.kubbidev.flower.model.manager.user;

import me.kubbidev.flower.model.User;
import me.kubbidev.flower.plugin.FlowerPlugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class StandardUserManager extends AbstractUserManager<User> {
    private final FlowerPlugin plugin;

    public StandardUserManager(FlowerPlugin plugin) {
        super(plugin, UserHousekeeper.timeoutSettings(1, TimeUnit.MINUTES));
        this.plugin = plugin;
    }

    @Override
    public User apply(UUID id) {
        return new User(id, this.plugin);
    }
}