package kindergarten.repository.jdbc;

import kindergarten.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseConnectionProvider {

    private final DatabaseConfig config;

    public DatabaseConnectionProvider(DatabaseConfig config) {
        this.config = Objects.requireNonNull(config, "DatabaseConfig cannot be null");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("PostgreSQL driver is not available", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    }
}