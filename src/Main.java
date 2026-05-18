import kindergarten.factory.DatabaseFactory;
import kindergarten.factory.RepositoryFactory;
import kindergarten.factory.ServiceFactory;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.repository.jdbc.DatabaseConnectionProvider;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionProvider dbProvider = DatabaseFactory.createConnectionProvider();
        DatabaseFactory.initializeSchema(dbProvider);

        GroupRepository groupRepository = RepositoryFactory.createGroupRepository(dbProvider);
        ChildRepository childRepository = RepositoryFactory.createChildRepository(dbProvider);

        GroupService groupService = ServiceFactory.createGroupService(groupRepository, childRepository);
        ChildService childService = ServiceFactory.createChildService(childRepository, groupRepository);

        System.out.println("Deploy the application as a WAR on Tomcat.");
    }
}