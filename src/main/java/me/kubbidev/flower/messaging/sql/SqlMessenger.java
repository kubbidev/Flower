package me.kubbidev.flower.messaging.sql;

import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.plugin.scheduler.SchedulerAdapter;
import me.kubbidev.flower.plugin.scheduler.SchedulerTask;
import me.kubbidev.flower.storage.implementation.sql.SqlStorage;
import net.flower.api.messenger.IncomingMessageConsumer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class SqlMessenger extends AbstractSqlMessenger {
    private final FlowerPlugin plugin;
    private final SqlStorage sqlStorage;

    private SchedulerTask pollTask;
    private SchedulerTask housekeepingTask;

    public SqlMessenger(FlowerPlugin plugin, SqlStorage sqlStorage, IncomingMessageConsumer consumer) {
        super(consumer);
        this.plugin = plugin;
        this.sqlStorage = sqlStorage;
    }

    @Override
    public void init() {
        try {
            super.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // schedule poll tasks
        SchedulerAdapter scheduler = this.plugin.getBootstrap().getScheduler();
        this.pollTask = scheduler.asyncRepeating(this::pollMessages, 1, TimeUnit.SECONDS);
        this.housekeepingTask = scheduler.asyncRepeating(this::runHousekeeping, 30, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        SchedulerTask task = this.pollTask;
        if (task != null) {
            task.cancel();
        }
        task = this.housekeepingTask;
        if (task != null) {
            task.cancel();
        }

        this.pollTask = null;
        this.housekeepingTask = null;

        super.close();
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return this.sqlStorage.getConnectionFactory().getConnection();
    }

    @Override
    protected String getTableName() {
        return this.sqlStorage.getStatementProcessor().apply("{prefix}messenger");
    }
}