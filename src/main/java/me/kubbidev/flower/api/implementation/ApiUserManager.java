package me.kubbidev.flower.api.implementation;

import me.kubbidev.flower.api.ApiUtils;
import me.kubbidev.flower.model.User;
import me.kubbidev.flower.model.manager.user.UserManager;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.util.ImmutableCollectors;
import net.flower.api.model.PlayerSaveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ApiUserManager extends ApiAbstractManager<User, net.flower.api.model.user.User, UserManager<?>> implements net.flower.api.model.user.UserManager {
    public ApiUserManager(FlowerPlugin plugin, UserManager<?> handle) {
        super(plugin, handle);
    }

    @Override
    protected net.flower.api.model.user.User proxy(User internal) {
        return internal == null ? null : internal.getApiProxy();
    }

    private net.flower.api.model.user.User proxyAndRegisterUsage(User internal) {
        if (internal != null) {
            this.plugin.getUserManager().getHouseKeeper().registerApiUsage(internal.getUniqueId());
        }
        return proxy(internal);
    }

    @Override
    public @NotNull CompletableFuture<net.flower.api.model.user.User> loadUser(@NotNull UUID uniqueId, @Nullable String username) {
        Objects.requireNonNull(uniqueId, "uuid");
        ApiUtils.checkUsername(username, this.plugin);

        return this.plugin.getStorage().loadUser(uniqueId, username)
                .thenApply(this::proxyAndRegisterUsage);
    }

    @Override
    public @NotNull CompletableFuture<UUID> lookupUniqueId(@NotNull String username) {
        Objects.requireNonNull(username, "username");
        return this.plugin.getStorage().getPlayerUniqueId(username);
    }

    @Override
    public @NotNull CompletableFuture<String> lookupUsername(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uuid");
        return this.plugin.getStorage().getPlayerName(uniqueId);
    }

    @Override
    public @NotNull CompletableFuture<Void> saveUser(net.flower.api.model.user.@NotNull User user) {
        User internal = ApiUser.cast(Objects.requireNonNull(user, "user"));
        return this.plugin.getStorage().saveUser(internal);
    }

    @Override
    public @NotNull CompletableFuture<Void> modifyUser(@NotNull UUID uniqueId, @NotNull Consumer<? super net.flower.api.model.user.User> action) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        Objects.requireNonNull(action, "action");

        return this.plugin.getStorage().loadUser(uniqueId, null)
                .thenApplyAsync(user -> {
                    action.accept(user.getApiProxy());
                    return user;
                }, this.plugin.getBootstrap().getScheduler().async())
                .thenCompose(user -> this.plugin.getStorage().saveUser(user));
    }

    @Override
    public @NotNull CompletableFuture<PlayerSaveResult> savePlayerData(@NotNull UUID uniqueId, @NotNull String username) {
        Objects.requireNonNull(uniqueId, "uuid");
        Objects.requireNonNull(username, "username");
        return this.plugin.getStorage().savePlayerData(uniqueId, username);
    }

    @Override
    public @NotNull CompletableFuture<Void> deletePlayerData(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uniqueId");
        return this.plugin.getStorage().deletePlayerData(uniqueId);
    }

    @Override
    public @NotNull CompletableFuture<@Unmodifiable Set<UUID>> getUniqueUsers() {
        return this.plugin.getStorage().getUniqueUsers();
    }

    @Override
    public net.flower.api.model.user.@Nullable User getUser(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uuid");
        return proxyAndRegisterUsage(this.handle.getIfLoaded(uniqueId));
    }

    @Override
    public net.flower.api.model.user.@Nullable User getUser(@NotNull String username) {
        Objects.requireNonNull(username, "name");
        return proxyAndRegisterUsage(this.handle.getByUsername(username));
    }

    @Override
    public @NotNull @Unmodifiable Set<net.flower.api.model.user.User> getLoadedUsers() {
        return this.handle.getAll().values().stream()
                .map(this::proxy)
                .collect(ImmutableCollectors.toSet());
    }

    @Override
    public boolean isLoaded(@NotNull UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uuid");
        return this.handle.isLoaded(uniqueId);
    }

    @Override
    public void cleanupUser(net.flower.api.model.user.@NotNull User user) {
        Objects.requireNonNull(user, "user");
        this.handle.getHouseKeeper().clearApiUsage(ApiUser.cast(user).getUniqueId());
    }
}
