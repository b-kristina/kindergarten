package kindergarten.ui;

import kindergarten.command.Command;

import java.util.Map;

public class ConsoleMenu {
    private final Map<Integer, Command> commands;

    public ConsoleMenu(Map<Integer, Command> commands) {
        this.commands = commands;
    }

    public void printMenu() {
        System.out.println("\n--- Меню ---");
        for (Map.Entry<Integer, Command> entry : commands.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getDescription());
        }
        System.out.print("Выберите команду: ");
    }
}