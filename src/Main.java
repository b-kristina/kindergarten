import kindergarten.command.Command;
import kindergarten.command.CommandType;
import kindergarten.command.ExitCommand;
import kindergarten.factory.CommandFactory;
import kindergarten.factory.DatabaseFactory;
import kindergarten.factory.RepositoryFactory;
import kindergarten.factory.ServiceFactory;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.repository.jdbc.DatabaseConnectionProvider;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import kindergarten.ui.CommandInvoker;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionProvider dbProvider = DatabaseFactory.createConnectionProvider();
        DatabaseFactory.initializeSchema(dbProvider);

        GroupRepository groupRepository = RepositoryFactory.createGroupRepository(dbProvider);
        ChildRepository childRepository = RepositoryFactory.createChildRepository(dbProvider);

        GroupService groupService = ServiceFactory.createGroupService(groupRepository, childRepository);
        ChildService childService = ServiceFactory.createChildService(childRepository, groupRepository);

        Scanner scanner = new Scanner(System.in);
        Map<CommandType, Command> commands = CommandFactory.createCommands(
                groupService, childService, scanner);

        ExitCommand exitCommand = (ExitCommand) commands.get(CommandType.EXIT);

        CommandInvoker invoker = new CommandInvoker(commands, exitCommand);
        invoker.start();
    }
}