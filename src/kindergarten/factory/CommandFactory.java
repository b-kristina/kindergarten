package kindergarten.factory;

import kindergarten.command.*;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.Map;
import java.util.Scanner;

public class CommandFactory {

    public static Map<CommandType, Command> createCommands(
            GroupService groupService,
            ChildService childService,
            Scanner scanner) {

        Map<CommandType, Command> commands = new java.util.HashMap<>();

        commands.put(CommandType.ADD_GROUP, new AddGroupCommand(groupService, scanner));
        commands.put(CommandType.ADD_CHILD, new AddChildCommand(childService, groupService, scanner));
        commands.put(CommandType.SHOW_GROUPS, new ShowGroupsCommand(groupService, childService));
        commands.put(CommandType.DELETE_GROUP, new DeleteGroupCommand(groupService, childService, scanner));
        commands.put(CommandType.DELETE_CHILD, new DeleteChildCommand(childService, groupService, scanner));
        commands.put(CommandType.EDIT_GROUP, new EditGroupCommand(groupService, scanner));
        commands.put(CommandType.EDIT_CHILD, new EditChildCommand(childService, groupService, scanner));
        commands.put(CommandType.EXIT, new ExitCommand());

        return commands;
    }
}