package kindergarten.ui;

import kindergarten.command.Command;
import kindergarten.command.ExitCommand;

import java.util.Map;
import java.util.Scanner;

public class CommandInvoker {
    private final Map<Integer, Command> commands;
    private final ConsoleMenu menu;
    private final Scanner scanner;
    private final ExitCommand exitCommand;

    public CommandInvoker(Map<Integer, Command> commands, ExitCommand exitCommand) {
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

            try {
                int commandKey = Integer.parseInt(input);
                Command command = commands.get(commandKey);
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("Неверная команда.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }
}