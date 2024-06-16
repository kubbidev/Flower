package me.kubbidev.flower.storage.implementation.sql.connection;

import me.kubbidev.flower.plugin.FlowerPlugin;
import me.kubbidev.flower.storage.StorageMetadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public interface ConnectionFactory {

    String getImplementationName();

    void init(FlowerPlugin plugin);

    void shutdown() throws Exception;

    StorageMetadata getMeta();

    Function<String, String> getStatementProcessor();

    Connection getConnection() throws SQLException;

}