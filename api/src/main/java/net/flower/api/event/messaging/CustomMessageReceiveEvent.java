package net.flower.api.event.messaging;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.util.Param;
import net.flower.api.messaging.MessagingService;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a custom payload message is received via the {@link MessagingService}.
 *
 * <p>This event is effectively the 'other end' of
 * {@link MessagingService#sendCustomMessage(String, String)}.</p>
 */
public interface CustomMessageReceiveEvent extends FlowerEvent {

    /**
     * Gets the channel id.
     *
     * @return the channel id
     */
    @Param(0)
    @NotNull String getChannelId();

    /**
     * Gets the custom payload that was sent.
     *
     * @return the custom payload
     */
    @Param(1)
    @NotNull String getPayload();

}