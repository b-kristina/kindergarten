package kindergarten.factory;

import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.repository.jdbc.DatabaseConnectionProvider;
import kindergarten.repository.jdbc.JdbcChildRepository;
import kindergarten.repository.jdbc.JdbcGroupRepository;

public class RepositoryFactory {

    public static GroupRepository createGroupRepository(DatabaseConnectionProvider provider) {
        return new JdbcGroupRepository(provider);
    }

    public static ChildRepository createChildRepository(DatabaseConnectionProvider provider) {
        return new JdbcChildRepository(provider);
    }
}