package kindergarten.command;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Scanner;

public class EditChildCommand implements Command {
    private final ChildService childService;
    private final GroupService groupService;
    private final Scanner scanner;

    public EditChildCommand(ChildService childService, GroupService groupService, Scanner scanner) {
        this.childService = childService;
        this.groupService = groupService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Child> children = childService.getAllChildren();
        if (children.isEmpty()) {
            System.out.println("Детей для редактирования нет.");
            return;
        }

        System.out.println("\n--- Доступные дети ---");
        for (Child child : children) {
            String groupName = getGroupName(child.getGroupId());
            System.out.println("  [" + child.getId() + "] " + child.getFullName()
                    + ", " + child.getAge() + " лет, пол: " + child.getGender()
                    + " | Группа: " + groupName);
        }

        System.out.print("\nВведите ID ребенка для редактирования: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
            return;
        }

        System.out.print("Новое ФИО: ");
        String fullName = scanner.nextLine().trim();

        if (fullName.isEmpty()) {
            System.out.println("ФИО не может быть пустым!");
            return;
        }

        String gender = selectGender();

        System.out.print("Новый возраст: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат возраста!");
            return;
        }

        List<Group> groups = groupService.getAllGroups();
        int groupId = 0;
        if (!groups.isEmpty()) {
            System.out.println("\n--- Выберите новую группу ---");
            for (Group group : groups) {
                System.out.println("  [" + group.getId() + "] " + group.getName()
                        + " №" + group.getNumber());
            }
            System.out.print("Введите ID группы (0 - без группы): ");
            String groupInput = scanner.nextLine();
            try {
                groupId = Integer.parseInt(groupInput);
                if (groupId != 0) {
                    groupService.getGroupById(groupId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ID!");
                return;
            } catch (RuntimeException e) {
                System.out.println("Группа с таким ID не найдена!");
                return;
            }
        }

        try {
            childService.updateChild(id, fullName, gender, age, groupId);
            System.out.println("Ребенок обновлен!");
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

    private String getGroupName(Integer groupId) {
        if (groupId == null || groupId == 0) {
            return "без группы";
        }
        try {
            Group group = groupService.getGroupById(groupId);
            return group.getName() + " №" + group.getNumber();
        } catch (RuntimeException e) {
            return "группа удалена";
        }
    }

    @Override
    public String getDescription() {
        return "Редактировать ребенка";
    }
}