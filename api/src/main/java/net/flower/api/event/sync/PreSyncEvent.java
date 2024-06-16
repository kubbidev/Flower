package net.flower.api.event.sync;

import net.flower.api.event.FlowerEvent;
import net.flower.api.event.type.Cancellable;

/**
 * Called just before a full synchronisation task runs.
 */
public interface PreSyncEvent extends FlowerEvent, Cancellable {

}