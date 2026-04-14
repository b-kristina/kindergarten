package kindergarten.command;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Optional;
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
        if (!hasAvailableChildren()) {
            return;
        }

        Optional<Integer> childIdOpt = selectChildId();
        if (childIdOpt.isEmpty()) {
            return;
        }

        ChildInputData inputData = readChildInputData();
        if (inputData == null) {
            return;
        }

        Optional<Integer> groupIdOpt = selectGroupId();
        if (groupIdOpt.isEmpty()) {
            return;
        }

        saveChild(childIdOpt.get(), inputData, groupIdOpt.get());
    }

    @Override
    public CommandType getType() {
        return CommandType.EDIT_CHILD;
    }

    private boolean hasAvailableChildren() {
        List<Child> children = childService.getAllChildren();
        if (children.isEmpty()) {
            System.out.println("Детей для редактирования нет.");
            return false;
        }
        displayChildrenList();
        return true;
    }

    private void displayChildrenList() {
        System.out.println("\n--- Доступные дети ---");
        for (Child child : childService.getAllChildren()) {
            String groupName = getGroupName(child.getGroupId());
            System.out.println("  [" + child.getId() + "] " + child.getFullName()
                    + ", " + child.getAge() + " лет, пол: " + child.getGender()
                    + " | Группа: " + groupName);
        }
    }

    private Optional<Integer> selectChildId() {
        System.out.print("\nВведите ID ребенка для редактирования: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат ID!");
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(input));
    }

    private ChildInputData readChildInputData() {
        String fullName = readFullName();
        if (fullName == null) {
            return null;
        }

        String gender = readGender();
        if (gender == null) {
            return null;
        }

        Integer age = readAge();
        if (age == null) {
            return null;
        }

        return new ChildInputData(fullName, gender, age);
    }

    private String readFullName() {
        System.out.print("Новое ФИО: ");
        String fullName = scanner.nextLine().trim();

        if (fullName.isEmpty()) {
            System.out.println("ФИО не может быть пустым!");
            return null;
        }
        return fullName;
    }

    private String readGender() {
        System.out.println("Выберите пол:");
        System.out.println("  М — мужской");
        System.out.println("  Ж — женский");

        boolean validInput = false;
        String gender = null;

        do {
            System.out.print("Ваш выбор (М или Ж): ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if ("М".equals(choice) || "M".equals(choice)) {
                gender = "М";
                validInput = true;
            } else if ("Ж".equals(choice) || "F".equals(choice)) {
                gender = "Ж";
                validInput = true;
            } else {
                System.out.println("Неверный выбор. Введите М или Ж.");
            }
        } while (!validInput);

        return gender;
    }

    private Integer readAge() {
        System.out.print("Новый возраст: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат возраста!");
            return null;
        }

        return Integer.parseInt(input);
    }

    private Optional<Integer> selectGroupId() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп нет, ребенок останется без группы.");
            return Optional.of(0);
        }

        displayGroupsList();
        System.out.print("Введите ID группы (0 - без группы): ");
        String groupInput = scanner.nextLine().trim();

        if (!isValidInteger(groupInput)) {
            System.out.println("Неверный формат ID!");
            return Optional.empty();
        }

        int groupId = Integer.parseInt(groupInput);
        if (groupId != 0 && !isGroupExists(groupId)) {
            return Optional.empty();
        }

        return Optional.of(groupId);
    }

    private void displayGroupsList() {
        System.out.println("\n--- Выберите новую группу ---");
        for (Group group : groupService.getAllGroups()) {
            System.out.println("  [" + group.getId() + "] "
                    + group.getName() + " №" + group.getNumber());
        }
    }

    private boolean isGroupExists(int groupId) {
        try {
            groupService.getGroupById(groupId);
            return true;
        } catch (RuntimeException e) {
            System.out.println("Группа с таким ID не найдена!");
            return false;
        }
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
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

    private void saveChild(int childId, ChildInputData inputData, int groupId) {
        try {
            childService.updateChild(childId, inputData.fullName, inputData.gender, inputData.age, groupId);
            System.out.println("Ребенок обновлен!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static class ChildInputData {
        private final String fullName;
        private final String gender;
        private final int age;

        public ChildInputData(String fullName, String gender, int age) {
            this.fullName = fullName;
            this.gender = gender;
            this.age = age;
        }
    }
}