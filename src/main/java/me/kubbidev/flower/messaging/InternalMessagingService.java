package me.kubbidev.flower.messaging;

import net.flower.api.messenger.Messenger;
import net.flower.api.messenger.MessengerProvider;

public interface InternalMessagingService {

    /**
     * Gets the name of this messaging service
     *
     * @return the name of this messaging service
     */
    String getName();

    Messenger getMessenger();

    MessengerProvider getMessengerProvider();

    /**
     * Closes the messaging service
     */
    void close();

    /**
     * Pushes a custom payload to connected instances.
     *
     * @param channelId the channel id
     * @param payload the payload
     */
    void pushCustomPayload(String channelId, String payload);
}