package kindergarten.web;

import kindergarten.factory.DatabaseFactory;
import kindergarten.factory.RepositoryFactory;
import kindergarten.factory.ServiceFactory;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.repository.jdbc.DatabaseConnectionProvider;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;

public final class AppContext {
    public static final String ATTRIBUTE_NAME = AppContext.class.getName();

    private final DatabaseConnectionProvider connectionProvider;
    private final GroupRepository groupRepository;
    private final ChildRepository childRepository;
    private final GroupService groupService;
    private final ChildService childService;

    private AppContext(
            DatabaseConnectionProvider connectionProvider,
            GroupRepository groupRepository,
            ChildRepository childRepository,
            GroupService groupService,
            ChildService childService) {
        this.connectionProvider = connectionProvider;
        this.groupRepository = groupRepository;
        this.childRepository = childRepository;
        this.groupService = groupService;
        this.childService = childService;
    }

    public static AppContext create() {
        DatabaseConnectionProvider connectionProvider = DatabaseFactory.createConnectionProvider();

        GroupRepository groupRepository = RepositoryFactory.createGroupRepository(connectionProvider);
        ChildRepository childRepository = RepositoryFactory.createChildRepository(connectionProvider);

        GroupService groupService = ServiceFactory.createGroupService(groupRepository, childRepository);
        ChildService childService = ServiceFactory.createChildService(childRepository, groupRepository);

        return new AppContext(connectionProvider, groupRepository, childRepository, groupService, childService);
    }

    public DatabaseConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public GroupRepository getGroupRepository() {
        return groupRepository;
    }

    public ChildRepository getChildRepository() {
        return childRepository;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public ChildService getChildService() {
        return childService;
    }
}