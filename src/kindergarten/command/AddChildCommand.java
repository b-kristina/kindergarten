package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Optional;
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
        if (!hasAvailableGroups()) {
            return;
        }

        Optional<Integer> groupIdOpt = selectGroupId();
        if (groupIdOpt.isEmpty()) {
            return;
        }

        ChildInputData inputData = readChildInputData();
        if (inputData == null) {
            return;
        }

        saveChild(inputData, groupIdOpt.get());
    }

    @Override
    public CommandType getType() {
        return CommandType.ADD_CHILD;
    }

    private boolean hasAvailableGroups() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Сначала создайте группу!");
            return false;
        }
        return true;
    }

    private Optional<Integer> selectGroupId() {
        displayGroupsList();

        System.out.print("Введите ID группы: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат ID!");
            return Optional.empty();
        }

        int groupId = Integer.parseInt(input);

        if (!isGroupExists(groupId)) {
            return Optional.empty();
        }

        return Optional.of(groupId);
    }

    private void displayGroupsList() {
        System.out.println("\n--- Выберите группу ---");
        for (Group group : groupService.getAllGroups()) {
            System.out.println("  [" + group.getId() + "] "
                    + group.getName() + " №" + group.getNumber());
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

    private boolean isGroupExists(int groupId) {
        try {
            groupService.getGroupById(groupId);
            return true;
        } catch (RuntimeException e) {
            System.out.println("Группа с таким ID не найдена!");
            return false;
        }
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
        System.out.print("ФИО ребенка: ");
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
        System.out.print("Возраст: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат возраста!");
            return null;
        }

        return Integer.parseInt(input);
    }

    private void saveChild(ChildInputData inputData, Integer groupId) {
        try {
            childService.createChild(inputData.fullName, inputData.gender, inputData.age, groupId);
            System.out.println("Ребенок добавлен!");
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