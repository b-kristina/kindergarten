package kindergarten.ui;

import kindergarten.command.Command;
import kindergarten.command.CommandType;
import kindergarten.command.ExitCommand;

import java.util.Map;
import java.util.Scanner;

public class CommandInvoker {
    private final Map<CommandType, Command> commands;
    private final ConsoleMenu menu;
    private final Scanner scanner;
    private final ExitCommand exitCommand;

    public CommandInvoker(Map<CommandType, Command> commands, ExitCommand exitCommand) {
        this.commands = commands;
        this.exitCommand = exitCommand;
        this.menu = new ConsoleMenu(commands);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Детский сад ===");
        while (!exitCommand.isExitRequested()) {
            menu.printMenu();
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Введите команду!");
                continue;
            }

            Command command = menu.getCommandByKey(input);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Неверная команда. Введите одну из предложенных.");
            }
        }
    }
}