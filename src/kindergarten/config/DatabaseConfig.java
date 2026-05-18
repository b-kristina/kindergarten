package kindergarten.config;

import java.util.Objects;

public class DatabaseConfig {

    private static final String DEFAULT_URL = "jdbc:postgresql://127.0.0.1:5433/kindergarten";
    private static final String DEFAULT_USER = "kindergarten";
    private static final String DEFAULT_PASSWORD = "kindergarten";

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        this(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public DatabaseConfig(String url, String user, String password) {
        this.url = Objects.requireNonNull(url, "DB URL cannot be null");
        this.user = Objects.requireNonNull(user, "DB user cannot be null");
        this.password = Objects.requireNonNull(password, "DB password cannot be null");
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}