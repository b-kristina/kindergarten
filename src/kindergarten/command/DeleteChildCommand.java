package kindergarten.command;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DeleteChildCommand implements Command {
    private final ChildService childService;
    private final GroupService groupService;
    private final Scanner scanner;

    public DeleteChildCommand(ChildService childService, GroupService groupService, Scanner scanner) {
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

        deleteChild(childIdOpt.get());
    }

    @Override
    public CommandType getType() {
        return CommandType.DELETE_CHILD;
    }

    private boolean hasAvailableChildren() {
        List<Child> children = childService.getAllChildren();
        if (children.isEmpty()) {
            System.out.println("Детей для удаления нет.");
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
        System.out.print("\nВведите ID ребенка для удаления: ");
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

    private void deleteChild(int childId) {
        try {
            childService.deleteChild(childId);
            System.out.println("Ребенок удален!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}