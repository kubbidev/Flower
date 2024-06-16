package net.flower.api.messenger.message.type;

import net.flower.api.messenger.message.Message;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a "custom payload" message.
 *
 * <p>Used by API consumers to send custom messages between servers.</p>
 *
 * @see net.flower.api.messaging.MessagingService#sendCustomMessage(String, String)
 * @see net.flower.api.event.messaging.CustomMessageReceiveEvent
 */
public interface CustomMessage extends Message {

    /**
     * Gets the channel identifier.
     *
     * @return the namespace
     */
    @NotNull String getChannelId();

    /**
     * Gets the payload.
     *
     * @return the payload
     */
    @NotNull String getPayload();

}