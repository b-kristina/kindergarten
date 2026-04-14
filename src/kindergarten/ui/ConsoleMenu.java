package kindergarten.ui;

import kindergarten.command.Command;
import kindergarten.command.CommandType;

import java.util.Map;

public class ConsoleMenu {
    private final Map<CommandType, Command> commands;

    public ConsoleMenu(Map<CommandType, Command> commands) {
        this.commands = commands;
    }

    public void printMenu() {
        System.out.println("\n--- Меню ---");
        for (CommandType type : commands.keySet()) {
            System.out.println("  " + type.getCommandKey() + " — " + type.getDescription());
        }
        System.out.print("Введите команду: ");
    }

    public Command getCommandByKey(String key) {
        CommandType type = CommandType.fromKey(key);
        if (type != null) {
            return commands.get(type);
        }
        return null;
    }
}