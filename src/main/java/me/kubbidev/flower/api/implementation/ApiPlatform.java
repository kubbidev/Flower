package me.kubbidev.flower.api.implementation;

import me.kubbidev.flower.plugin.FlowerPlugin;
import net.flower.api.platform.Platform;
import net.flower.api.platform.PluginMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ApiPlatform implements Platform, PluginMetadata {
    private final FlowerPlugin plugin;

    public ApiPlatform(FlowerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getBootstrap().getVersion();
    }

    @Override
    public @NotNull String getApiVersion() {
        String[] version = this.plugin.getBootstrap().getVersion().split("\\.");
        return version[0] + '.' + version[1];
    }

    @Override
    public @NotNull Type getType() {
        return this.plugin.getBootstrap().getType();
    }

    @Override
    public @NotNull @Unmodifiable Set<UUID> getUniqueConnections() {
        return Collections.unmodifiableSet(this.plugin.getConnectionListener().getUniqueConnections());
    }

    @Override
    public @NotNull Instant getStartTime() {
        return this.plugin.getBootstrap().getStartupTime();
    }


}