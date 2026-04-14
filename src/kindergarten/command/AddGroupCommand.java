package kindergarten.command;

import kindergarten.service.GroupService;
import java.util.Scanner;

public class AddGroupCommand implements Command {
    private final GroupService groupService;
    private final Scanner scanner;

    public AddGroupCommand(GroupService groupService, Scanner scanner) {
        this.groupService = groupService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        String name = readGroupName();
        if (name == null) {
            return;
        }

        Integer number = readGroupNumber();
        if (number == null) {
            return;
        }

        saveGroup(name, number);
    }

    @Override
    public CommandType getType() {
        return CommandType.ADD_GROUP;
    }

    private String readGroupName() {
        System.out.print("Название группы: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым!");
            return null;
        }
        return name;
    }

    private Integer readGroupNumber() {
        System.out.print("Номер группы: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат номера!");
            return null;
        }

        int number = Integer.parseInt(input);
        if (number <= 0) {
            System.out.println("Номер группы должен быть положительным!");
            return null;
        }
        return number;
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void saveGroup(String name, int number) {
        try {
            groupService.createGroup(name, number);
            System.out.println("Группа добавлена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}