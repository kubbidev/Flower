package me.kubbidev.flower.tasks;

import me.kubbidev.flower.cache.BufferedRequest;
import me.kubbidev.flower.plugin.FlowerPlugin;

import java.util.concurrent.TimeUnit;

/**
 * System wide sync task for Flower.
 *
 * <p>Ensures that all local data is consistent with the storage.</p>
 */
public class SyncTask implements Runnable {
    private final FlowerPlugin plugin;

    public SyncTask(FlowerPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs the update task
     *
     * <p>Called <b>async</b>.</p>
     */
    @Override
    public void run() {
        if (this.plugin.getEventDispatcher().dispatchPreSync(false)) {
            return;
        }

        // Reload all online users.
        this.plugin.getUserManager().loadAllUsers().join();

        this.plugin.performPlatformDataSync();
        this.plugin.getEventDispatcher().dispatchPostSync();
    }

    public static class Buffer extends BufferedRequest<Void> {
        private final FlowerPlugin plugin;

        public Buffer(FlowerPlugin plugin) {
            super(500L, TimeUnit.MILLISECONDS, plugin.getBootstrap().getScheduler());
            this.plugin = plugin;
        }

        @Override
        protected Void perform() {
            new SyncTask(this.plugin).run();
            return null;
        }
    }
}