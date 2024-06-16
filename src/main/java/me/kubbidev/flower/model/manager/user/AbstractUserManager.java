package me.kubbidev.flower.model.manager.user;

import me.kubbidev.flower.model.User;
import me.kubbidev.flower.model.manager.AbstractManager;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.util.CompletableFutures;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractUserManager<T extends User> extends AbstractManager<UUID, User, T> implements UserManager<T> {

    private final FlowerPlugin plugin;
    private final UserHousekeeper housekeeper;

    public AbstractUserManager(FlowerPlugin plugin, UserHousekeeper.TimeoutSettings timeoutSettings) {
        this.plugin = plugin;
        this.housekeeper = new UserHousekeeper(plugin, this, timeoutSettings);
        this.plugin.getBootstrap().getScheduler().asyncRepeating(this.housekeeper, 30, TimeUnit.SECONDS);
    }

    @Override
    public T getOrMake(UUID id, String username) {
        T user = getOrMake(id);
        if (username != null) {
            user.setUsername(username, false);
        }
        return user;
    }

    @Override
    public T getByUsername(String name) {
        for (T user : getAll().values()) {
            Optional<String> n = user.getUsername();
            if (n.isPresent() && n.get().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserHousekeeper getHouseKeeper() {
        return this.housekeeper;
    }

    @Override
    public CompletableFuture<Void> loadAllUsers() {
        Set<UUID> ids = new HashSet<>(getAll().keySet());
        ids.addAll(this.plugin.getBootstrap().getOnlinePlayers());

        return ids.stream()
                .map(id -> this.plugin.getStorage().loadUser(id, null))
                .collect(CompletableFutures.collector());
    }
}