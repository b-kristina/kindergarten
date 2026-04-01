import kindergarten.command.AddChildCommand;
import kindergarten.command.AddGroupCommand;
import kindergarten.command.DeleteGroupCommand;
import kindergarten.command.ShowGroupsCommand;
import kindergarten.command.DeleteChildCommand;
import kindergarten.command.EditGroupCommand;
import kindergarten.command.EditChildCommand;
import kindergarten.command.ExitCommand;
import kindergarten.command.Command;
import kindergarten.repository.memory.InMemoryChildRepository;
import kindergarten.repository.memory.InMemoryGroupRepository;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import kindergarten.service.impl.ChildServiceImpl;
import kindergarten.service.impl.GroupServiceImpl;
import kindergarten.ui.CommandInvoker;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GroupRepository groupRepository = new InMemoryGroupRepository();
        ChildRepository childRepository = new InMemoryChildRepository();

        GroupService groupService = new GroupServiceImpl(groupRepository, childRepository);
        ChildService childService = new ChildServiceImpl(childRepository, groupRepository);

        Scanner scanner = new Scanner(System.in);

        Map<Integer, Command> commands = new HashMap<>();
        commands.put(1, new AddGroupCommand(groupService, scanner));
        commands.put(2, new AddChildCommand(childService, groupService, scanner));
        commands.put(3, new ShowGroupsCommand(groupService, childService));
        commands.put(4, new DeleteGroupCommand(groupService, childService, scanner));
        commands.put(5, new DeleteChildCommand(childService, groupService, scanner));
        commands.put(6, new EditGroupCommand(groupService, scanner));
        commands.put(7, new EditChildCommand(childService, groupService, scanner));

        ExitCommand exitCommand = new ExitCommand();
        commands.put(0, exitCommand);

        CommandInvoker invoker = new CommandInvoker(commands, exitCommand);
        invoker.start();
    }
}