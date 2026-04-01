package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Scanner;

public class EditGroupCommand implements Command {
    private final GroupService groupService;
    private final Scanner scanner;

    public EditGroupCommand(GroupService groupService, Scanner scanner) {
        this.groupService = groupService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп для редактирования нет.");
            return;
        }

        System.out.println("\n--- Доступные группы ---");
        for (Group group : groups) {
            System.out.println("  [" + group.getId() + "] " + group.getName()
                    + " №" + group.getNumber());
        }

        System.out.print("\nВведите ID группы для редактирования: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
            return;
        }

        System.out.print("Новое название: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым!");
            return;
        }

        System.out.print("Новый номер: ");
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
            groupService.updateGroup(id, name, number);
            System.out.println("Группа обновлена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Редактировать группу";
    }
}