package me.kubbidev.flower;

import me.kubbidev.flower.plugin.scheduler.AbstractJavaScheduler;
import me.kubbidev.flower.plugin.scheduler.SchedulerAdapter;

import java.util.concurrent.Executor;

public class BukkitSchedulerAdapter extends AbstractJavaScheduler implements SchedulerAdapter {
    private final Executor sync;

    public BukkitSchedulerAdapter(FBukkitBootstrap bootstrap) {
        super(bootstrap);
        this.sync = r -> bootstrap.getServer().getScheduler().scheduleSyncDelayedTask(bootstrap.getLoader(), r);
    }

    @Override
    public Executor sync() {
        return this.sync;
    }

}