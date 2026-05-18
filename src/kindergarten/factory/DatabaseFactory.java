package kindergarten.factory;

import kindergarten.config.DatabaseConfig;
import kindergarten.repository.jdbc.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseFactory {

    public static DatabaseConnectionProvider createConnectionProvider() {
        DatabaseConfig config = new DatabaseConfig();
        return new DatabaseConnectionProvider(config);
    }

    public static void initializeSchema(DatabaseConnectionProvider provider) {
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS groups (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL UNIQUE,
                    number INT NOT NULL UNIQUE
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS children (
                    id SERIAL PRIMARY KEY,
                    full_name VARCHAR(100) NOT NULL,
                    gender VARCHAR(1) NOT NULL,
                    age INT NOT NULL,
                    group_id INT REFERENCES groups(id) ON DELETE SET NULL
                )
                """);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize database schema", e);
        }
    }
}