package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        Integer groupId = parseIntegerOrNull(input);
        if (groupId == null) {
            System.out.println("Неверный формат ID!");
            return Optional.empty();
        }

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

    private Integer parseIntegerOrNull(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
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

        try {
            Integer age = readAge();
            return new ChildInputData(fullName, gender, age);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
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
        for (GenderOption option : GenderOption.values()) {
            System.out.println("  " + option.getLabel() + " — " + option.getDescription());
        }

        while (true) {
            System.out.print("Ваш выбор (" + GenderOption.allowedChoicesDescription() + "): ");
            String choice = scanner.nextLine().trim().toUpperCase();

            GenderOption genderOption = GenderOption.fromChoice(choice);
            if (genderOption != null) {
                return genderOption.getLabel();
            }

            System.out.println("Неверный выбор. Введите " + GenderOption.allowedChoicesDescription() + ".");
        }
    }

    private Integer readAge() {
        System.out.print("Возраст: ");
        Integer age = parseIntegerOrNull(scanner.nextLine().trim());

        if (age == null) {
            System.out.println("Неверный формат возраста!");
            return null;
        }

        return age;
    }

    private void saveChild(ChildInputData inputData, Integer groupId) {
        try {
            childService.validateChildInput(inputData.fullName, inputData.gender, inputData.age, groupId);
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

    private enum GenderOption {
        MALE("М", "мужской", "M"),
        FEMALE("Ж", "женский", "F");

        private final String label;
        private final String description;
        private final String[] aliases;

        GenderOption(String label, String description, String... aliases) {
            this.label = label;
            this.description = description;
            this.aliases = aliases;
        }

        private String getLabel() {
            return label;
        }

        private String getDescription() {
            return description;
        }

        private boolean matches(String choice) {
            if (label.equals(choice)) {
                return true;
            }

            for (String alias : aliases) {
                if (alias.equals(choice)) {
                    return true;
                }
            }

            return false;
        }

        private static GenderOption fromChoice(String choice) {
            for (GenderOption option : values()) {
                if (option.matches(choice)) {
                    return option;
                }
            }

            return null;
        }

        private static String allowedChoicesDescription() {
            return Arrays.stream(values())
                    .map(GenderOption::getLabel)
                    .collect(Collectors.joining(" или "));
        }
    }
}