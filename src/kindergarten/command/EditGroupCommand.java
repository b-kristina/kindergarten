package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Optional;
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
        if (!hasAvailableGroups()) {
            return;
        }

        Optional<Integer> groupIdOpt = selectGroupId();
        if (groupIdOpt.isEmpty()) {
            return;
        }

        GroupInputData inputData = readGroupInputData();
        if (inputData == null) {
            return;
        }

        saveGroup(groupIdOpt.get(), inputData);
    }

    @Override
    public CommandType getType() {
        return CommandType.EDIT_GROUP;
    }

    private boolean hasAvailableGroups() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп для редактирования нет.");
            return false;
        }
        displayGroupsList();
        return true;
    }

    private void displayGroupsList() {
        System.out.println("\n--- Доступные группы ---");
        for (Group group : groupService.getAllGroups()) {
            System.out.println("  [" + group.getId() + "] "
                    + group.getName() + " №" + group.getNumber());
        }
    }

    private Optional<Integer> selectGroupId() {
        System.out.print("\nВведите ID группы для редактирования: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат ID!");
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(input));
    }

    private GroupInputData readGroupInputData() {
        String name = readGroupName();
        if (name == null) {
            return null;
        }

        Integer number = readGroupNumber();
        if (number == null) {
            return null;
        }

        return new GroupInputData(name, number);
    }

    private String readGroupName() {
        System.out.print("Новое название: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым!");
            return null;
        }
        return name;
    }

    private Integer readGroupNumber() {
        System.out.print("Новый номер: ");
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

    private void saveGroup(int groupId, GroupInputData inputData) {
        try {
            groupService.updateGroup(groupId, inputData.name, inputData.number);
            System.out.println("Группа обновлена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static class GroupInputData {
        private final String name;
        private final int number;

        public GroupInputData(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }
}