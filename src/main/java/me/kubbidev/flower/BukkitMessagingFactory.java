package me.kubbidev.flower;

import me.kubbidev.flower.messaging.InternalMessagingService;
import me.kubbidev.flower.messaging.MessagingFactory;

public class BukkitMessagingFactory extends MessagingFactory<FBukkitPlugin> {
    public BukkitMessagingFactory(FBukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    protected InternalMessagingService getServiceFor(String messagingType) {
        return super.getServiceFor(messagingType);
    }
}