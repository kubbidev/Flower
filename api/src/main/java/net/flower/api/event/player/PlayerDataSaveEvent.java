package net.flower.api.event.player;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.util.Param;
import net.flower.api.model.PlayerSaveResult;
import net.flower.api.model.user.UserManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Called when player data is saved to the storage.
 *
 * <p>Data can be saved using {@link UserManager#savePlayerData(UUID, String)}.</p>
 */
public interface PlayerDataSaveEvent extends FlowerEvent {

    /**
     * Gets the unique ID that was saved.
     *
     * @return the uuid
     */
    @Param(0)
    @NotNull UUID getUniqueId();

    /**
     * Gets the username that was saved.
     *
     * @return the username
     */
    @Param(1)
    @NotNull String getUsername();

    /**
     * Gets the result of the operation.
     *
     * @return the result
     */
    @Param(2)
    @NotNull PlayerSaveResult getResult();

}