package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DeleteGroupCommand implements Command {
    private final GroupService groupService;
    private final ChildService childService;
    private final Scanner scanner;

    public DeleteGroupCommand(GroupService groupService, ChildService childService, Scanner scanner) {
        this.groupService = groupService;
        this.childService = childService;
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

        deleteGroup(groupIdOpt.get());
    }

    @Override
    public CommandType getType() {
        return CommandType.DELETE_GROUP;
    }

    private boolean hasAvailableGroups() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп для удаления нет.");
            return false;
        }
        displayGroupsList();
        return true;
    }

    private void displayGroupsList() {
        System.out.println("\n--- Доступные группы ---");
        for (Group group : groupService.getAllGroups()) {
            int childCount = childService.getChildrenByGroupId(group.getId()).size();
            System.out.println("  [" + group.getId() + "] " + group.getName()
                    + " №" + group.getNumber() + " (детей: " + childCount + ")");
        }
    }

    private Optional<Integer> selectGroupId() {
        System.out.print("\nВведите ID группы для удаления: ");
        String input = scanner.nextLine().trim();

        if (!isValidInteger(input)) {
            System.out.println("Неверный формат ID!");
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(input));
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void deleteGroup(int groupId) {
        try {
            groupService.deleteGroup(groupId);
            System.out.println("Группа удалена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}