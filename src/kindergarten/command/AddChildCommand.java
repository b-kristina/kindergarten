package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Scanner;

public class AddChildCommand implements Command {
    private final ChildService childService;
    private final GroupService groupService;
    private final Scanner scanner;

    public AddChildCommand(ChildService childService, GroupService groupService, Scanner scanner) {
        this.childService = childService;
        this.groupService = groupService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Сначала создайте группу!");
            return;
        }

        System.out.println("\n--- Выберите группу ---");
        for (Group group : groups) {
            System.out.println("  [" + group.getId() + "] " + group.getName() + " №" + group.getNumber());
        }

        System.out.print("Введите ID группы: ");
        int groupId;
        try {
            groupId = Integer.parseInt(scanner.nextLine());
            groupService.getGroupById(groupId);
        } catch (RuntimeException e) {
            System.out.println("Группа с таким ID не найдена!");
            return;
        }

        System.out.print("ФИО ребенка: ");
        String fullName = scanner.nextLine().trim();

        if (fullName.isEmpty()) {
            System.out.println("ФИО не может быть пустым!");
            return;
        }

        String gender = selectGender();

        System.out.print("Возраст: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат возраста!");
            return;
        }

        try {
            childService.createChild(fullName, gender, age, groupId);
            System.out.println("Ребенок добавлен!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private String selectGender() {
        while (true) {
            System.out.println("Выберите пол:");
            System.out.println("  1. М (мужской)");
            System.out.println("  2. Ж (женский)");
            System.out.print("Ваш выбор (1 или 2): ");
            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) {
                return "М";
            } else if ("2".equals(choice)) {
                return "Ж";
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "Добавить ребенка";
    }
}