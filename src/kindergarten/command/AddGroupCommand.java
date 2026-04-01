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
        System.out.print("Название группы: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым!");
            return;
        }

        System.out.print("Номер группы: ");
        int number;
        try {
            number = Integer.parseInt(scanner.nextLine());
            if (number <= 0) {
                System.out.println("Номер группы должен быть положительным!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат номера!");
            return;
        }

        try {
            groupService.createGroup(name, number);
            System.out.println("Группа добавлена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Добавить группу";
    }
}