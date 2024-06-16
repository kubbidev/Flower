package me.kubbidev.flower.storage.implementation;

import me.kubbidev.flower.model.User;
import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.storage.StorageMetadata;
import net.flower.api.model.PlayerSaveResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface StorageImplementation {
    FlowerPlugin getPlugin();

    String getImplementationName();

    void init() throws Exception;

    void shutdown();

    StorageMetadata getMeta();

    User loadUser(UUID uniqueId, String username) throws Exception;

    Map<UUID, User> loadUsers(Set<UUID> uniqueIds) throws Exception;

    void saveUser(User user) throws Exception;

    Set<UUID> getUniqueUsers() throws Exception;

    PlayerSaveResult savePlayerData(UUID uniqueId, String username) throws Exception;

    void deletePlayerData(UUID uniqueId) throws Exception;

    @Nullable UUID getPlayerUniqueId(String username) throws Exception;

    @Nullable String getPlayerName(UUID uniqueId) throws Exception;
}