package kindergarten.ui;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final ChildService childService;
    private final GroupService groupService;
    private final Scanner scanner;

    public ConsoleMenu(ChildService childService, GroupService groupService) {
        this.childService = childService;
        this.groupService = groupService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Детский сад ===");
        while (true) {
            printMenu();
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1": addGroup(); break;
                case "2": addChild(); break;
                case "3": showAllGroups(); break;
                case "4": deleteGroup(); break;
                case "5": deleteChild(); break;
                case "6": editGroup(); break;
                case "7": editChild(); break;
                case "0":
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверная команда.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Добавить группу");
        System.out.println("2. Добавить ребенка");
        System.out.println("3. Показать все группы с детьми");
        System.out.println("4. Удалить группу");
        System.out.println("5. Удалить ребенка");
        System.out.println("6. Редактировать группу");
        System.out.println("7. Редактировать ребенка");
        System.out.println("0. Выход");
        System.out.print("Выберите команду: ");
    }

    private void addGroup() {
        System.out.print("Название группы: ");
        String name = scanner.nextLine();
        System.out.print("Номер группы: ");
        int number = Integer.parseInt(scanner.nextLine());
        groupService.createGroup(name, number);
        System.out.println("Группа добавлена!");
    }

    private void addChild() {
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
        String fullName = scanner.nextLine();

        String gender = selectGender();

        System.out.print("Возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

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

    private void showAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп пока нет.");
            return;
        }

        for (Group group : groups) {
            System.out.println("\n=== Группа: " + group.getName() + " №" + group.getNumber()
                    + " (ID: " + group.getId() + ") ===");

            List<Child> children = childService.getChildrenByGroupId(group.getId());
            if (children.isEmpty()) {
                System.out.println("  (детей нет)");
            } else {
                for (Child child : children) {
                    System.out.println("  [" + child.getId() + "] " + child.getFullName() + ", "
                            + child.getAge() + " лет, пол: " + child.getGender());
                }
            }
        }
    }

    private void showAllChildren() {
        List<Child> children = childService.getAllChildren();
        if (children.isEmpty()) {
            System.out.println("Детей пока нет.");
            return;
        }

        System.out.println("\n--- Все дети ---");
        for (Child child : children) {
            String groupName = "без группы";
            if (child.getGroupId() != null) {
                try {
                    Group group = groupService.getGroupById(child.getGroupId());
                    groupName = group.getName() + " №" + group.getNumber();
                } catch (RuntimeException e) {
                    groupName = "группа удалена";
                }
            }
            System.out.println("  [" + child.getId() + "] " + child.getFullName() + ", "
                    + child.getAge() + " лет, пол: " + child.getGender() + " | Группа: " + groupName);
        }
    }

    private void deleteGroup() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп для удаления нет.");
            return;
        }

        System.out.println("\n--- Доступные группы ---");
        for (Group group : groups) {
            int childCount = childService.getChildrenByGroupId(group.getId()).size();
            System.out.println("  [" + group.getId() + "] " + group.getName() + " №" + group.getNumber()
                    + " (детей: " + childCount + ")");
        }

        System.out.print("\nВведите ID группы для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            groupService.deleteGroup(id);
            System.out.println("Группа удалена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteChild() {
        showAllChildren();

        System.out.print("\nВведите ID ребенка для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            childService.deleteChild(id);
            System.out.println("Ребенок удален!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void editGroup() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп для редактирования нет.");
            return;
        }

        System.out.println("\n--- Доступные группы ---");
        for (Group group : groups) {
            System.out.println("  [" + group.getId() + "] " + group.getName() + " №" + group.getNumber());
        }

        System.out.print("\nВведите ID группы для редактирования: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Новое название: ");
        String name = scanner.nextLine();
        System.out.print("Новый номер: ");
        int number = Integer.parseInt(scanner.nextLine());
        try {
            groupService.updateGroup(id, name, number);
            System.out.println("Группа обновлена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void editChild() {
        List<Child> children = childService.getAllChildren();
        if (children.isEmpty()) {
            System.out.println("Детей для редактирования нет.");
            return;
        }

        System.out.println("\n--- Доступные дети ---");
        for (Child child : children) {
            String groupName = "без группы";
            if (child.getGroupId() != null) {
                try {
                    Group group = groupService.getGroupById(child.getGroupId());
                    groupName = group.getName() + " №" + group.getNumber();
                } catch (RuntimeException e) {
                    groupName = "группа удалена";
                }
            }
            System.out.println("  [" + child.getId() + "] " + child.getFullName() + ", "
                    + child.getAge() + " лет, пол: " + child.getGender() + " | Группа: " + groupName);
        }

        System.out.print("\nВведите ID ребенка для редактирования: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Новое ФИО: ");
        String fullName = scanner.nextLine();

        String gender = selectGender();

        System.out.print("Новый возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        List<Group> groups = groupService.getAllGroups();
        int groupId = 0;
        if (!groups.isEmpty()) {
            System.out.println("\n--- Выберите новую группу ---");
            for (Group group : groups) {
                System.out.println("  [" + group.getId() + "] " + group.getName() + " №" + group.getNumber());
            }
            System.out.print("Введите ID группы (0 - без группы): ");
            groupId = Integer.parseInt(scanner.nextLine());
            if (groupId != 0) {
                try {
                    groupService.getGroupById(groupId);
                } catch (RuntimeException e) {
                    System.out.println("Группа с таким ID не найдена!");
                    return;
                }
            }
        } else {
            System.out.println("Групп нет, ребенок останется без группы.");
        }

        try {
            childService.updateChild(id, fullName, gender, age, groupId);
            System.out.println("Ребенок обновлен!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}